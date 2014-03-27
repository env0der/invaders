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

(defn draw-tile [texture x y]
  (let [sprite (create-sprite texture)]
    (add-sprite-to-stage sprite)
    (set-sprite-position sprite (+ (* (mod y 2) 40) (* 80 x)) (* 50 y))))

(defn draw-grid [game-map]
  (let [grid (game-map-to-grid game-map)]
    (doseq [cell grid]
      (draw-tile ((:type cell) tiles-textures) (:x cell) (:y cell)))))

(draw-grid game-map)

(defn update-world [])
