(ns invaders.client.unit
  (:require
   [clojure.string :as string]
   [invaders.client.state :as state]
   [invaders.client.sprite :as sprite]
   [invaders.client.stage :as stage]))

(defn unit-from-sprite [unit-sprite]
  (get-in @state/game [:units (unit-id (.-sprite-id unit-sprite))]))

(defn unit-id [sprite-id]
  (subs sprite-id 5))

(defn sprite-id [unit-id]
  (str "unit:" unit-id))

(defn get-sprite [id]
  (get-in @state/ui [:sprites id]))

(defn sprite-exists? [id]
  (not (nil? (get-sprite id))))

(defn click [unit-sprite click-data]
  (sprite/select unit-sprite))

(defn create-sprite [id texture-name]
  (let [sprite (sprite/create id texture-name)]
    (swap! state/ui assoc-in [:sprites id] sprite)
    (stage/add-sprite-to-stage sprite)
    (sprite/click sprite #(click sprite %))
    sprite))

(defn get-or-create-sprite [id texture-name]
  (when-not (sprite-exists? id) (create-sprite id texture-name))
  (get-sprite id))

(defn render [id unit]
  (let [sprite (get-or-create-sprite id (:type unit))]
    (sprite/position sprite (:x unit) (:y unit))
    (if (some #{sprite} (sprite/selected))
      (sprite/hshade sprite)
      (sprite/hclear sprite))))

(defn tile-id [unit-sprite]
  (let [unit (unit-from-sprite unit-sprite)]
    (str "tile:" (:x unit) ":" (:y unit))))

(defn tile [unit-sprite]
  (get-in @state/ui [:sprites (tile-id unit-sprite)]))

(defn move [unit-sprite x y]
  (swap! state/game assoc-in [:units (unit-id (.-sprite-id unit-sprite)) :x] x)
  (swap! state/game assoc-in [:units (unit-id (.-sprite-id unit-sprite)) :y] y))
