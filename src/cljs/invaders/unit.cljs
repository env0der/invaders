(ns invaders.client.unit
  (:require
   [invaders.client.state :as state]
   [invaders.client.sprite :as sprite]))

(defn sprite-id [id]
  (str "unit:" id))

(defn get-sprite [id]
  (get-in state/ui [:sprites (sprite-id id)]))

(defn sprite-exists? [id]
  (not (nil? (get-sprite id))))

(defb create-sprite [id texture-name]
  (swap! state/ui assoc-in [:sprites (sprite-id id)] (sprite/create id texture-name)))

(defn get-or-create-sprite [id texture-name]
  (when-not (sprite-exists? id) (create-sprite id texture-name))
  (get-sprite id))

(defn render [id unit]
  (let [sprite (get-or-create-sprite id (:type unit))]
    (sprite/position sprite (:x unit) (:y unit))))
