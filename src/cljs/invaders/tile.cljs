(ns invaders.client.tile
  (:require
    [invaders.client.state :as state]
    [invaders.client.stage :as stage]
    [invaders.client.sprite :as sprite]))

(defn sprites []
  (:sprites @state/ui))

(defn get-sprite [id]
  (get-in @state/ui [:sprites id]))

(defn sprite-exists? [id]
  (not (nil? (get-sprite id))))

(defn create-sprite [id tile]
  (let [texture-name (:type tile) x (:x tile) y (:y tile)
        new-sprite (sprite/create id texture-name)]
    (swap! state/ui assoc-in [:sprites id] new-sprite)
    (set! (.-map-x new-sprite) x)
    (set! (.-map-y new-sprite) y)
    (stage/add-sprite-to-stage new-sprite)
    (sprite/click new-sprite #(click new-sprite %))
    (sprite/position new-sprite x y)
    new-sprite))

(defn get-or-create-sprite [id tile]
  (when-not (sprite-exists? id) (create-sprite id tile))
  (get-sprite id))

(defn render [id tile]
  (let [sprite (get-or-create-sprite id tile)]
    (if (some #{sprite} (sprite/selected))
      (sprite/hshade sprite)
      (sprite/hclear sprite))))

(defn click [tile-sprite clickData]
  (case (count (sprite/selected-type))
    1 (sprite/select tile-sprite)
    2 (let [selected (sprite/selected)]
             (sprite/deselect)
             (unit/move selected (.-map-x tile-sprite) (.-map-y tile-sprite)))
    0 (sprite/select tile-sprite)))
