(ns invaders.client.stage)

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


(defn create-sprite [texture]
  (js/PIXI.Sprite. texture))

(defn set-sprite-position [sprite x y]
  (set! (.-position.x sprite) x)
  (set! (.-position.y sprite) y))

(defn add-sprite-to-stage [sprite]
  (.addChild stage sprite))

(defn update-world [])
