(ns invaders.client.main
  :require [invaders.client.components]
  [brute.entity :as entity]
  [brute.system :as system])

(def stage (js/PIXI.Stage. 0xE3FCFF))
(def renderer (js/PIXI.autoDetectRenderer (.-innerWidth js/window) (.-innerHeight js/window)))

(def stats (js/Stats.))
(set! (-> (.-domElement stats) .-style .-position) "absolute")
(set! (-> (.-domElement stats) .-style .-display) "inline-block")
(set! (-> (.-domElement stats) .-style .-top) "0px")
(set! (-> (.-domElement stats) .-style .-right) "0px")

(.appendChild (.-body js/document) (.-view renderer))
(.appendChild (.-body js/document) (.-domElement stats))

(defn rendering-system [])

(defn start [system]
  (let [player (entity/create-entity)]
    (-> system
        (entity/add-entity player)
        (entity/add-component player (components/->Position 100 100))
        (entity/add-component player (components/->Drawable (js/PIXI.Texture.fromImage "/images/marsman.png")))))
  )

(defn render-stage []
  (js/requestAnimFrame render-stage)
  (.begin stats)
  (update)
  (.end stats)
  (. renderer render stage))

(defn update []
  )

(start (entity/create-system))
(render-stage)
