(ns invaders.client.main
  (:require
   [clojure.browser.repl :as repl]
   [invaders.client.stage :as stage]))

(repl/connect "http://localhost:9000/repl")

(defn log [& items]
  (.log js/console (apply str items)))

(js/requestAnimFrame stage/render-stage)
