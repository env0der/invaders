(ns invaders.logging)

(defn log [& items]
  (.log js/console (apply str items)))
