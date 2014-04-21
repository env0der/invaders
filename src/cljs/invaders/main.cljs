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

(defn sprite-tint [tile tint]
  (set! (.-tint tile) tint))

(defn sprite-hclear [tile]
  (sprite-tint tile 0xFFFFFF))

(defn sprite-hshade [tile]
  (sprite-tint tile 0xBBBBBB))

(defn sprite-selected []
  (if (:selected-tile @ui-state) "tile"
    (if (:selected-unit @ui-state) "unit" "nothing")))

; tile <- tile
; tile <- unit
; tile <- nothing
(defn tile-click [tile clickData]
  (.log js/console "selecting tile " (.-map-x tile) " " (.-map-y tile))

  (case (sprite-selected)
    "tile" (let [selected-tile (:selected-tile @ui-state)]
             (log "tile -> tile")
             (sprite-hclear selected-tile)
             (sprite-hshade tile)
             (swap! ui-state assoc :selected-tile tile)
             (swap! ui-state dissoc :selected-unit))

    "unit" (let [selected-unit (:selected-unit @ui-state)]
             (log "unit -> tile")
             (sprite-hclear selected-unit)
             (sprite-hclear (unit-tile selected-unit))
             (move-unit selected-unit (.-map-x tile) (.-map-y tile))
             (swap! ui-state dissoc :selected-tile)
             (swap! ui-state dissoc :selected-unit))

    "nothing" (do
                (log "nothing -> tile")
                (sprite-hshade tile)
                (swap! ui-state assoc :selected-tile tile)
                (swap! ui-state dissoc :selected-unit))))

(defn unit-tile [unit]
  (get-in @ui-state
          [:map (get-in @game-state [:units (.-unit-id unit) :x])
                (get-in @game-state [:units (.-unit-id unit) :y])]))

; unit <- tile
; unit <- unit
; unit <- nothing
(defn unit-click [unit clickData]
  (.log js/console "selecting unit " (.-map-x unit) " " (.-map-y unit))

  (case (sprite-selected)
    "tile" (let [selected-tile (:selected-tile @ui-state)]
             (log "tile -> unit")
             (sprite-hclear selected-tile)
             (sprite-hshade (unit-tile unit))
             (sprite-hshade unit)
             (swap! ui-state dissoc :selected-tile)
             (swap! ui-state assoc :selected-unit unit))

    "unit" (let [selected-unit (:selected-unit @ui-state)]
             (log "unit -> unit")
             (sprite-hclear selected-unit)
             (sprite-hclear (unit-tile selected-unit))
             (sprite-hshade (unit-tile unit))
             (sprite-hshade unit)
             (swap! ui-state dissoc :selected-tile)
             (swap! ui-state assoc :selected-unit unit))

    "nothing" (do
                (log "nothing -> unit")
                (sprite-hshade (unit-tile unit))
                (sprite-hshade unit)
                (swap! ui-state dissoc :selected-tile)
                (swap! ui-state assoc :selected-unit unit))))

(defn draw-tile [texture x y]
  (let [sprite (stage/create-sprite texture)]
    (stage/add-sprite-to-stage sprite)
    (swap! ui-state assoc-in [:map x y] sprite)
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
