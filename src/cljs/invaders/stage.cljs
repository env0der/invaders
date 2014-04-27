(ns invaders.client.stage
  (:require
    [invaders.client.state :as state]
    [invaders.client.tile :as tile]
    [invaders.client.unit :as unit]))

(def stage (js/PIXI.Stage. 0xE3FCFF))
(def renderer (js/PIXI.autoDetectRenderer (.-innerWidth js/window) (.-innerHeight js/window)))

(def stats (js/Stats.))
(set! (-> (.-domElement stats) .-style .-position) "absolute")
(set! (-> (.-domElement stats) .-style .-display) "inline-block")
(set! (-> (.-domElement stats) .-style .-top) "0px")
(set! (-> (.-domElement stats) .-style .-right) "0px")

(.appendChild (.-body js/document) (.-view renderer))
(.appendChild (.-body js/document) (.-domElement stats))

(defn render-stage []
  (js/requestAnimFrame render-stage)
  (.begin stats)
  (update-world)
  (.end stats)
  (. renderer render stage))

(defn add-sprite-to-stage [sprite]
  (.addChild stage sprite))

(defn update-world []
  (let [tiles (state/game-map-to-grid state/game-map)
        units (:units @state/game)]
    (doseq [tile tiles]
      (tile/render (str "tile:" (:x tile) ":" (:y tile)) tile))))
