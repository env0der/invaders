(ns invaders.client.main
  (:use-macros
   [dommy.macros :only [node sel sel1]])
  (:require
   [dommy.core :as dommy]
   [clojure.browser.repl :as repl]))

(repl/connect "http://localhost:9000/repl")


;; sprite functions

(defn create-sprite [texture]
  (js/PIXI.Sprite. texture))

(defn create-texture [path]
  (js/PIXI.Texture.fromImage path))

(defn set-sprite-position [sprite x y]
  (set! (.-position.x sprite) x)
  (set! (.-position.y sprite) y))

(defn add-sprite-to-stage [sprite]
  (. stage addChild sprite))


;; setup and rendering loop

(def stage (js/PIXI.Stage. 0xaaaaaa))
(def renderer (js/PIXI.autoDetectRenderer 1000 500))
(.appendChild (.-body js/document) (.-view renderer))

(defn render-stage []
  (js/requestAnimFrame render-stage)
  (update-world)
  (. renderer render stage))

(js/requestAnimFrame render-stage)


;; drawing game map
(def tiles-textures {:w (create-texture "/images/water.png")
                     :g (create-texture "/images/grass.png")})

(def units-textures {:marsman (create-texture "/images/marsman.png")})

(def game-map [
               [:g :g :g :g :g :g :g :g :g]
               [:g :g :g :g :g :w :w :w :g]
               [:g :g :g :g :g :w :w :w :g]
               [:w :w :g :g :g :w :w :g :g]
               [:w :w :g :g :g :g :g :g :g]
               [:g :g :g :g :w :g :g :g :g]
               [:g :g :g :g :g :w :g :g :g]
               [:g :g :g :g :g :w :w :g :g]
               ])

(defn game-map-to-grid [game-map]
  (vec (flatten (map-indexed (fn [y v]
                               (map-indexed (fn [x type] {:x x :y y :type type}) v)
                               ) game-map))))
(def ui-state (atom {}))
(def game-state (atom {:units {
                               1 {:type :marsman :x 3 :y 4}
                               2 {:type :marsman :x 2 :y 4}
                               3 {:type :marsman :x 3 :y 2}
                               4 {:type :marsman :x 2 :y 3}
                       }}))

(defn highlight-tile [tile tint]
  (set! (.-tint tile) tint))

(defn remove-tile-highlight [tile]
  (highlight-tile tile 0xFFFFFF))

(defn draw-tile [texture x y]
  (let [sprite (create-sprite texture)]
    (add-sprite-to-stage sprite)
    (set! (.-interactive sprite) true)
    (set! (.-click sprite) (fn [clickData]
                             (let [selected-tile (:selected-tile @ui-state)]
                               (if selected-tile
                                 (remove-tile-highlight selected-tile)))
                             (highlight-tile sprite 0xBBBBBB)
                             (swap! ui-state assoc :selected-tile sprite)))
    (set-sprite-position sprite (+ (* (mod y 2) 40) (* 80 x)) (* 50 y))))

(defn draw-grid [grid]
  (doseq [cell grid]
    (draw-tile ((:type cell) tiles-textures) (:x cell) (:y cell))))

(defn draw-units [units]
  (doseq [unit (vals (:units @game-state))]
    (let [sprite (create-sprite ((:type unit) units-textures))]
      (add-sprite-to-stage sprite)
      (set-unit-position sprite (:x unit) (:y unit)))))

(defn set-unit-position [unit x y]
  (set-sprite-position unit
                       (+ (* (mod y 2) 40) 40 (* 80 x)) (+ 10 (* 50 y))))

;; TODO: it would be better to draw a pre-rendered map image instead of drawing it cell by cell
(draw-grid (game-map-to-grid game-map))
(draw-units (:units @game-state))

(defn update-world [])
