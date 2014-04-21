(ns invaders.client.main
  (:use-macros
   [dommy.macros :only [node sel sel1]])
  (:require
   [dommy.core :as dommy]
   [clojure.browser.repl :as repl]
   [invaders.client.maps :as maps]
   [invaders.client.stage :as stage]
   [invaders.client.textures :as textures]))

(repl/connect "http://localhost:9000/repl")

(defn log [& items]
  (.log js/console (apply str items)))

(def game-map (:clearshore maps/maps))

(defn game-map-to-grid [game-map]
  (vec (flatten (map-indexed (fn [y v]
                               (map-indexed (fn [x type] {:x x :y y :type type}) v)
                               ) game-map))))
(def ui-state (atom {:map {}}))
(def game-state (atom {:units {
                               1 {:type :marsman :x 1 :y 7}
                               2 {:type :marsman :x 2 :y 5}
                               3 {:type :marsman :x 6 :y 4}
                               4 {:type :marsman :x 2 :y 2}
                               }}))

(defn unit-tile [unit]
  (get-in @ui-state
          [:map (get-in @game-state [:units (.-unit-id unit) :x])
                (get-in @game-state [:units (.-unit-id unit) :y])]))

(defn sprite-tint [tile tint]
  (set! (.-tint tile) tint))

(defn sprite-hclear [tile]
  (sprite-tint tile 0xFFFFFF))

(defn sprite-hshade [tile]
  (sprite-tint tile 0xBBBBBB))

(defn sprite-selected []
  (:sprite-selected @ui-state))

(defn sprite-selected-type []
  (if-let [selected (sprite-selected)] (.-sprite-type selected) "nothing"))

(defn sprite-select [sprite]
  (sprite-deselect)
  (sprite-hshade sprite)
  (swap! ui-state assoc :sprite-selected sprite)
  (if (= "unit" (.-sprite-type sprite)) (sprite-hshade (unit-tile sprite))))

(defn sprite-deselect
  ([] (when-let [selected (sprite-selected)] (sprite-deselect selected)))
  ([sprite]
      (sprite-hclear sprite)
      (swap! ui-state dissoc :sprite-selected)
      (if (= "unit" (.-sprite-type sprite)) (sprite-hclear (unit-tile sprite)))))

; tile <- tile
; tile <- unit
; tile <- nothing
(defn tile-click [tile clickData]
  (case (sprite-selected-type)
    "tile" (sprite-select tile)
    "unit" (do
             (sprite-deselect)
             (move-unit (sprite-selected) (.-map-x tile) (.-map-y tile)))
    "nothing" (sprite-select tile)))

; unit <- tile
; unit <- unit
; unit <- nothing
(defn unit-click [unit clickData]
  (sprite-select unit))

(defn draw-tile [texture x y]
  (let [sprite (stage/create-sprite texture)]
    (stage/add-sprite-to-stage sprite)
    (swap! ui-state assoc-in [:map x y] sprite)
    (set! (.-sprite-type sprite) "tile")
    (set! (.-interactive sprite) true)
    (set! (.-click sprite) #(tile-click sprite %))
    (sprite-grid-position sprite x y)
    (set! (.-map-x sprite) x)
    (set! (.-map-y sprite) y)))

(defn draw-grid [grid]
  (doseq [cell grid]
    (draw-tile ((:type cell) textures/tiles-textures) (:x cell) (:y cell))))

(defn unit-grid-position [unit x y]
  (sprite-grid-position unit x y :offset-x 5 :offset-y -35))

(defn sprite-grid-position [sprite x y & {:keys [offset-x offset-y]
                                          :or {offset-x 0
                                               offset-y 0}}]
  (stage/set-sprite-position
    sprite
    (+ (* (mod y 2) 40) (* 80 x) offset-x)
    (+ (* 50 y) 35 offset-y) ))

(defn draw-units [units]
  (doseq [[id unit] (:units @game-state)]
    (let [sprite (stage/create-sprite ((:type unit) textures/units-textures))]
      (set! (.-sprite-type sprite) "unit")
      (set! (.-unit-id sprite) id)
      (stage/add-sprite-to-stage sprite)
      (unit-grid-position sprite (:x unit) (:y unit))
      (set! (.-interactive sprite) true)
      (set! (.-click sprite) #(unit-click sprite %)))))

(defn select-tile [tile]
  (sprite-hshade tile)
  (swap! ui-state assoc :selected-tile tile))

(defn move-unit [unit x y]
  (swap! game-state assoc-in [:units (.-unit-id unit) :x] x)
  (swap! game-state assoc-in [:units (.-unit-id unit) :y] y)
  (unit-grid-position unit x y)
)

;; TODO: it would be better to draw a pre-rendered map image instead of drawing it cell by cell
(draw-grid (game-map-to-grid game-map))
(draw-units (:units @game-state))

(js/requestAnimFrame stage/render-stage)
