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

(defn stage-entity [system entity]
  (let [drawable (entity/get-component system entity Drawable)
        position (entity/get-component system entity Position)
        sprite (js/PIXI.Sprite. (:image drawable))]
    (set! (.-position.x sprite) (:x position))
    (set! (.-position.y sprite) (:y position))
    (.addChild stage sprite)
    sprite))

(defn stage-all [system delta]
  (let [entities (intersection
                    (entity/get-all-entities-with-component system Drawable)
                    (entity/get-all-entities-with-component system Position))]
    (->> entities
         (filter #(nil? (get-in system [:staged %])))
         (map #(stage-entity system %))
         (reduce #(assoc-in %1 [:staged %2]) system))))

(defn start [system]
  (let [player (entity/create-entity)]
    (-> system
        (entity/add-entity player)
        (entity/add-component player (components/->Position 100 100))
        (entity/add-component player (components/->Drawable (js/PIXI.Texture.fromImage "/images/marsman.png")))
        (system/add-system-fn stage-all)))
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
