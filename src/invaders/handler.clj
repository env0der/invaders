(ns invaders.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.logger :as logger]
            [monger.core :as mg]
            [invaders.views.index :as index_view]))

(defroutes app-routes
  (GET "/" [] (index_view/render))
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init []
  (mg/connect!)
  (mg/set-db! (mg/get-db "invaders_dev")))

(def app
  (-> (handler/site app-routes)
      (logger/wrap-with-logger)))
