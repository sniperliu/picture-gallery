(ns picture-gallery.ajax
  (:require [ajax.core :as ajax]
            [reagent.session :as session]))

(defn default-headers [request]
  (-> request
      (update :uri #(str js/context %))
      (update
       :headers
       #(merge
         %
         {"Accept" "application/transit+json"
          "x-csrf-token" js/csrfToken}))))

(defn user-action [request]
  (session/put! :user-event true))

(defn load-interceptors! []
  (swap! ajax/default-interceptors
         conj
         (ajax/to-interceptor {:name "default headers"
                               :request default-headers}
                              {:name "user action"
                               :request user-action})))


