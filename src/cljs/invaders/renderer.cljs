(ns invaders.client.renderer)

(defn renderer [entities]
  (doseq [entity entities]
    (render entity)))
