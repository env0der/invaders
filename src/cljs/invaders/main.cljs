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

(defn tile-click [tile clickData]
  (case (sprite/selected-type)
    "tile" (sprite/select tile)
    "unit" (let [selected (sprite/selected)]
             (sprite/deselect)
             (move-unit selected (.-map-x tile) (.-map-y tile)))
    "nothing" (sprite/select tile)))

(defn move-unit [unit x y]
  (swap! state/game assoc-in [:units (.-unit-id unit) :x] x)
  (swap! state/game assoc-in [:units (.-unit-id unit) :y] y)
  (sprite/position unit x y))

(defn draw-grid [grid]
  (doseq [tile grid]
    (let [sprite (sprite/create-tile tile)]
      (stage/add-sprite-to-stage sprite)
      (sprite/click sprite #(tile-click sprite %))
      (sprite/position sprite (:x tile) (:y tile)))))

(defn draw-units [units]
  (doseq [[id unit] (:units @state/game)]
    (let [sprite (sprite/create-unit id unit)]
      (stage/add-sprite-to-stage sprite)
      (sprite/position sprite (:x unit) (:y unit))
      (sprite/click sprite #(sprite/select sprite)))))

;; TODO: it would be better to draw a pre-rendered map image instead of drawing it cell by cell
(draw-grid (game-map-to-grid game-map))
(draw-units (:units @state/game))

(js/requestAnimFrame stage/render-stage)
