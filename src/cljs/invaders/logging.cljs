(ns invaders.client.logging)

(defn log [& items]
  (.log js/console (apply str items)))
