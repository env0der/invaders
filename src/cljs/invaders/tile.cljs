(ns invaders.client.tile
  (:require
    [invaders.client.state :as state]))

(defn sprites []
  (:sprites @state/ui))

(defn render-target [id texture-name]
  (if-let [sprite (id sprites)]
    sprite
    (let [new-sprite (sprite/create id texture-name)]
      (swap! sprites assoc-in id new-sprite)
      new-sprite)))

(defn render [id tile]
  (let [sprite (render-target id (:type tile))
        x (:x tile)
        y (:y tile)]
    (swap! state/ui assoc-in id sprite)
    (set! (.-map-x sprite) x)
    (set! (.-map-y sprite) y)
    (stage/add-sprite-to-stage sprite)
    (sprite/click sprite #(click sprite %))
    (sprite/position sprite x y)))

(defn click [tile-sprite clickData]
  (case (sprite/selected-type)
    "tile" (sprite/select tile-sprite)
    "unit" (let [selected (sprite/selected)]
             (sprite/deselect)
             (unit/move selected (.-map-x tile-sprite) (.-map-y tile-sprite)))
    "nothing" (sprite/select tile-sprite)))
