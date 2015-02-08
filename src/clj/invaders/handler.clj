(ns invaders.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.reload :as reload]
            [ring.middleware.logger :as logger]
            [org.httpkit.server :as http-kit]
            [invaders.views.index :as index_view]))

(defroutes app-routes
  (GET "/" [] (index_view/render))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (logger/wrap-with-logger)
      (reload/wrap-reload)))

(defn -main [& args]
  (http-kit/run-server app {:port 8080}))
