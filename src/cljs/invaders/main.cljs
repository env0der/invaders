(ns invaders.client.main
  (:use-macros
   [dommy.macros :only [node sel sel1]])
  (:require
   [dommy.core :as dommy]
   [clojure.browser.repl :as repl]
   [invaders.client.maps :as maps]
   [invaders.client.stage :as stage]
   [invaders.client.textures :as textures]
   [invaders.client.sprite :as sprite]
   [invaders.client.state :as state]))

(repl/connect "http://localhost:9000/repl")

(defn log [& items]
  (.log js/console (apply str items)))

(def game-map (:clearshore maps/maps))

(defn game-map-to-grid [game-map]
  (vec (flatten (map-indexed (fn [y v]
                               (map-indexed (fn [x type] {:x x :y y :type type}) v)
                               ) game-map))))

; tile <- tile
; tile <- unit
; tile <- nothing
(defn tile-click [tile clickData]
  (case (sprite/selected-type)
    "tile" (sprite/select tile)
    "unit" (let [selected (sprite/selected)]
             (sprite/deselect)
             (move-unit selected (.-map-x tile) (.-map-y tile)))
    "nothing" (sprite/select tile)))

; unit <- tile
; unit <- unit
; unit <- nothing
(defn unit-click [unit clickData]
  (sprite/select unit))

(defn draw-tile [texture x y]
  (let [sprite (stage/create-sprite texture)]
    (stage/add-sprite-to-stage sprite)
    (swap! state/ui assoc-in [:map x y] sprite)
    (set! (.-sprite-type sprite) "tile")
    (set! (.-interactive sprite) true)
    (set! (.-click sprite) #(tile-click sprite %))
    (sprite/grid-position sprite x y)
    (set! (.-map-x sprite) x)
    (set! (.-map-y sprite) y)))

(defn draw-grid [grid]
  (doseq [cell grid]
    (draw-tile ((:type cell) textures/tiles-textures) (:x cell) (:y cell))))

(defn unit-grid-position [unit x y]
  (sprite/grid-position unit x y :offset-x 5 :offset-y -35))

(defn draw-units [units]
  (doseq [[id unit] (:units @state/game)]
    (let [sprite (sprite/create-unit id unit)]
      (stage/add-sprite-to-stage sprite)
      (unit-grid-position sprite (:x unit) (:y unit))
      (sprite/click sprite #(unit-click sprite %)))))

(defn select-tile [tile]
  (sprite/hshade tile)
  (swap! state/ui assoc :selected-tile tile))

(defn move-unit [unit x y]
  (swap! state/game assoc-in [:units (.-unit-id unit) :x] x)
  (swap! state/game assoc-in [:units (.-unit-id unit) :y] y)
  (unit-grid-position unit x y)
)

;; TODO: it would be better to draw a pre-rendered map image instead of drawing it cell by cell
(draw-grid (game-map-to-grid game-map))
(draw-units (:units @state/game))

(js/requestAnimFrame stage/render-stage)
