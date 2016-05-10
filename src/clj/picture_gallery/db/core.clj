(ns picture-gallery.db.core
  (:require
    [conman.core :as conman]
    [mount.core :refer [defstate]]
    [picture-gallery.config :refer [env]]))

(defstate ^:dynamic *db*
          :start (conman/connect!
                   {:datasource
                    (doto (org.h2.jdbcx.JdbcDataSource.)
                          (.setURL (env :database-url))
                          (.setUser "")
                          (.setPassword ""))})
          :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/queries.sql")

(defn delete-account! [id]
  (conman/with-transaction [*db*]
    (delete-user! {:id id})
    (delete-user-images! {:owner id})))

