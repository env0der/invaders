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
(def ui-state (atom {}))
(def game-state (atom {:units {
                               1 {:type :marsman :x 1 :y 4}
                               2 {:type :marsman :x 2 :y 5}
                               3 {:type :marsman :x 6 :y 4}
                               4 {:type :marsman :x 2 :y 2}
                       }}))

(defn highlight-tile [tile tint]
  (set! (.-tint tile) tint))

(defn remove-tile-highlight [tile]
  (highlight-tile tile 0xFFFFFF))

(defn draw-tile [texture x y]
  (let [sprite (stage/create-sprite texture)]
    (stage/add-sprite-to-stage sprite)
    (set! (.-interactive sprite) true)
    (set! (.-click sprite) (fn [clickData]
                             (let [selected-tile (:selected-tile @ui-state)]
                               (if selected-tile
                                 (remove-tile-highlight selected-tile)))
                             (highlight-tile sprite 0xBBBBBB)
                             (swap! ui-state assoc :selected-tile sprite)))
    (stage/set-sprite-position sprite (+ (* (mod y 2) 40) (* 80 x)) (* 50 y))))

(defn draw-grid [grid]
  (doseq [cell grid]
    (draw-tile ((:type cell) textures/tiles-textures) (:x cell) (:y cell))))

(defn set-unit-position [unit x y]
  (stage/set-sprite-position unit
                             (+ (* (mod y 2) 40) 40 (* 80 x)) (+ 10 (* 50 y))))

(defn draw-units [units]
  (doseq [unit (vals (:units @game-state))]
    (let [sprite (stage/create-sprite ((:type unit) textures/units-textures))]
      (stage/add-sprite-to-stage sprite)
      (set-unit-position sprite (:x unit) (:y unit)))))

;; TODO: it would be better to draw a pre-rendered map image instead of drawing it cell by cell
(draw-grid (game-map-to-grid game-map))
(draw-units (:units @game-state))

(js/requestAnimFrame stage/render-stage)
