(ns invaders.client.main
  (:use-macros
   [dommy.macros :only [node sel sel1]])
  (:require
   [dommy.core :as dommy]))

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

(defn transform-map [game-map]
  (vec (flatten (map-indexed (fn [y v]
                               (map-indexed (fn [x type] {:x x :y y :type type}) v)
                               ) game-map))))

(defn load-images-and-do [callback]
  (let [grass (new js/Image)]
    (set! (.-src grass) "/images/grass.png")
    (set! (.-onload grass) (fn []
                             (let [water (new js/Image)]
                               (set! (.-src water) "/images/water.png")
                               (set! (.-onload water) #(callback {:w water :g grass})))
                             ))))

(defn draw-cell [context img x y]
  (.drawImage context img (+ (* (mod y 2) 40) (* 80 x)) (* 50 y)))

(defn draw-grid [context resources game-map]
  (let [grid (transform-map game-map)]
    (doseq [cell grid]
      (draw-cell context ((:type cell) resources) (:x cell) (:y cell)))
    ))

(defn init[]
  (let [canvas (sel1 :#gamefield)]
    (let [context (.getContext canvas "2d")]
      (load-images-and-do (fn [resources] (draw-grid context resources game-map))))))

(set! (.-onload js/window) init)

(:require [clojure.browser.repl :as repl])

(repl/connect "http://localhost:9000/repl")

(.write js/document "Invaders from ClojureScript!")
