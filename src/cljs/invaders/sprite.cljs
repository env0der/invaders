(ns invaders.client.sprite
  (:require
   [clojure.string :as string]
   [invaders.client.textures :as textures]
   [invaders.client.stage :as stage]
   [invaders.client.state :as state]))

(defn tint [tile tint]
  (set! (.-tint tile) tint))

(defn hclear [tile]
  (tint tile 0xFFFFFF))

(defn hshade [tile]
  (tint tile 0xBBBBBB))

(defn selected []
  (:selected @state/ui))

(defn selected-type []
  (if-let [selected (selected)] (.-sprite-type selected) "nothing"))

(defn select [sprite]
  (deselect)
  (hshade sprite)
  (swap! state/ui assoc :selected sprite)
  (if (= "unit" (.-sprite-type sprite)) (hshade (unit-tile sprite))))

(defn deselect
  ([] (when-let [selected (selected)] (deselect selected)))
  ([sprite]
      (hclear sprite)
      (swap! state/ui dissoc :selected)
      (if (= "unit" (.-sprite-type sprite)) (hclear (unit-tile sprite)))))

(defn position [sprite x y]
  (case (.-sprite-type sprite)
    "tile" (grid-position sprite x y)
    "unit" (grid-position sprite x y :offset-x 5 :offset-y -35)))

(defn grid-position [sprite x y & {:keys [offset-x offset-y]
                                          :or {offset-x 0
                                               offset-y 0}}]
  (set! (.-position.x sprite) (+ (* (mod y 2) 40) (* 80 x) offset-x))
  (set! (.-position.y sprite) (+ (* 50 y) 35 offset-y)))

(defn unit-tile [unit]
  (get-in @state/ui
          [:map (get-in @state/game [:units (.-unit-id unit) :x])
                (get-in @state/game [:units (.-unit-id unit) :y])]))

(defn click [sprite fun]
  (set! (.-click sprite) fun))

(defn create-unit [id unit]
  (let [sprite (js/PIXI.Sprite. ((:type unit) textures/units-textures))]
    (set! (.-sprite-type sprite) "unit")
    (set! (.-unit-id sprite) id)
    (set! (.-interactive sprite) true)
    sprite))

(defn create [id texture-name]
  (let [sprite (js/PIXI.Sprite. (texture textures/tiles-textures))]
    (set! (.-sprite-type sprite) (first (string/split id #":")))
    (set! (.-sprite-id sprite) id)
    (set! (.-interactive sprite) true)
    sprite))

(defn create-tile [tile]
  (let [sprite (js/PIXI.Sprite. ((:type tile) textures/tiles-textures))
        x (:x tile)
        y (:y tile)]
    (swap! state/ui assoc-in [:map x y] sprite)
    (set! (.-sprite-type sprite) "tile")
    (set! (.-interactive sprite) true)
    (set! (.-map-x sprite) x)
    (set! (.-map-y sprite) y)
    sprite))
