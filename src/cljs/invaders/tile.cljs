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
    (set! (.-map-y sprite) y)))
