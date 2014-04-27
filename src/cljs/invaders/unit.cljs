(ns invaders.client.unit
  (:require
   [invaders.client.state :as state]
   [invaders.client.sprite :as sprite]
   [invaders.client.stage :as stage]))

(defn sprite-id [id]
  (str "unit:" id))

(defn get-sprite [id]
  (get-in @state/ui [:sprites (sprite-id id)]))

(defn sprite-exists? [id]
  (not (nil? (get-sprite id))))

(defn click [unit-sprite click-data]
  (sprite/select unit-sprite))

(defn create-sprite [id texture-name]
  (let [sprite (sprite/create id texture-name)]
    (swap! state/ui assoc-in [:sprites (sprite-id id)] sprite)
    (stage/add-sprite-to-stage sprite)
    (sprite/click sprite #(click sprite %))
    sprite))

(defn get-or-create-sprite [id texture-name]
  (when-not (sprite-exists? id) (create-sprite id texture-name))
  (get-sprite id))

(defn render [id unit]
  (let [sprite (get-or-create-sprite id (:type unit))]
    (sprite/position sprite (:x unit) (:y unit))))

(defn tile [unit]
  (get (str "tile:" (:x unit) ":" (:y unit)) (:sprites @state/ui)))

(defn move [unit x y]
  (swap! state/game assoc-in [:units (.-unit-id unit) :x] x)
  (swap! state/game assoc-in [:units (.-unit-id unit) :y] y)
  (sprite/position unit x y))
