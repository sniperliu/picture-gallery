(ns picture-gallery.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [picture-gallery.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]]
            [picture-gallery.components.common :as c]
            [picture-gallery.components.registration :as reg]
            [picture-gallery.components.login :as l]
            [picture-gallery.components.upload :as u]
            [picture-gallery.components.gallery :as g])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(reset! collapsed? true)} title]])

(defn account-actions [id]
  (let [expanded? (r/atom false)]
    (fn []
      [:div.dropdown
       {:class (when @expanded? "open")
        :on-click #(swap! expanded? not)}
       [:button.btn.btn-secondary.dropdown-toggle
        {:type :button}
        [:span.glyphicon.glyphicon-user] " " id [:span.caret]]
       [:div.dropdown-menu.user-actions
        [:a.dropdown-item.btn
         {:on-click
          #(session/put!
            :modal reg/delete-account-modal)}
         "delete account"]
        [:a.dropdown-item.btn
         {:on-click
          #(POST
            "/logout"
            {:handler (fn [] (session/remove! :identity))})}
         "sign out"]]])))

(defn user-menu []
  (if-let [id (session/get :identity)]
    [:ul.nav.navbar-nav.pull-xs-right
     [:li.nav-item [u/upload-button]]
     [:li.nav-item
      [account-actions id]]]
    [:ul.nav.navbar-nav.pull-xs-right
     [:li.nav-item [l/login-button]]
     [:li.nav-item [reg/registration-button]]]))

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-light.bg-faded
       [:button.navbar-toggler.hidden-sm-up
        {:on-click #(swap! collapsed? not)} "☰"]
       [:div.collapse.navbar-toggleable-xs
        (when-not @collapsed? {:class "in"})
        [:a.navbar-brand {:href "#/"} "picture-gallery"]
        [:ul.nav.navbar-nav
         [nav-link "#/" "Home" :home collapsed?]
         (when-let [owner (session/get :identity)]
           [nav-link (str "#/gallery/" owner) "Gallery" :gallery collapsed?])
         [nav-link "#/about" "About" :about collapsed?]]]
       [user-menu]])))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     "this is the story of picture-gallery... work in progress"]]])

(defn home-page []
  [:div.container
   [:div.jumbotron
    [:h1 "Welcome to picture-gallery"]
    [:p "Time to start building your site!"]
    [:p [:a.btn.btn-primary.btn-lg {:href "http://luminusweb.net"} "Learn more »"]]]
   [:div.row
    [:div.col-md-12
     [:h2 "Welcome to ClojureScript"]]]
   (when-let [docs (session/get :docs)]
     [:div.row
      [:div.col-md-12
       [:div {:dangerouslySetInnerHTML
              {:__html (md->html docs)}}]]])])

(def pages
  {:home #'home-page
   :gallery #'g/gallery-page
   :about #'about-page})

(defn modal []
  (when-let [session-modal (session/get :modal)]
    [session-modal]))

(defn page []
  [:div
   [modal]
   [(pages (session/get :page))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/gallery/:owner" [owner]
  (g/fetch-gallery-thumbs! owner)
  (session/put! :page :gallery))

(secretary/defroute "/about" []
  (session/put! :page :about))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET (str js/context "/docs") {:handler #(session/put! :docs %)}))

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!) 
  (hook-browser-navigation!)
  (session/put! :identity js/identity)
  (mount-components))
