(ns invaders.client.sprite
  (:require
   [clojure.string :as string]
   [invaders.client.textures :as textures]
   [invaders.client.stage :as stage]
   [invaders.client.state :as state]
   [invaders.client.unit :as unit]))

(defn tint [sprite tint]
  (set! (.-tint sprite) tint))

(defn hclear [sprite]
  (tint sprite 0xFFFFFF))

(defn hshade [sprite]
  (tint sprite 0xBBBBBB))

(defn selected []
  (:selected @state/ui))

(defn select [sprite]
  (swap! state/ui assoc :selected
    (remove nil?
      (list sprite (if (= "unit" (.-sprite-type sprite)) (unit/tile sprite))))))

(defn deselect []
  (swap! state/ui assoc :selected (list)))

(defn position [sprite x y]
  (case (.-sprite-type sprite)
    "tile" (grid-position sprite x y)
    "unit" (grid-position sprite x y :offset-x 5 :offset-y -35)))

(defn grid-position [sprite x y & {:keys [offset-x offset-y]
                                          :or {offset-x 0
                                               offset-y 0}}]
  (set! (.-position.x sprite) (+ (* (mod y 2) 40) (* 80 x) offset-x))
  (set! (.-position.y sprite) (+ (* 50 y) 35 offset-y)))

(defn click [sprite fun]
  (set! (.-click sprite) fun))

(defn create [id texture-name]
  (let [sprite (js/PIXI.Sprite. (texture-name textures/textures))]
    (set! (.-sprite-type sprite) (first (string/split id #":")))
    (set! (.-sprite-id sprite) id)
    (set! (.-interactive sprite) true)
    sprite))
