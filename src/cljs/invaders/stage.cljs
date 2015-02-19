(ns invaders.stage)

(def stage (js/PIXI.Stage. 0xE3FCFF))
(def renderer (js/PIXI.autoDetectRenderer (.-innerWidth js/window) (.-innerHeight js/window)))

(def stats (let [stats (js/Stats.)]
             (set! (-> (.-domElement stats) .-style .-position) "absolute")
             (set! (-> (.-domElement stats) .-style .-display) "inline-block")
             (set! (-> (.-domElement stats) .-style .-top) "0px")
             (set! (-> (.-domElement stats) .-style .-right) "0px")
             stats))

(def tick-processor-fn (atom (fn [delta])))

(def prev-tick-time (atom 0))

(defn- current-timestamp []
  (.getTime (js/Date.)))

(defn- render []
  (.begin stats)
  (let [time (current-timestamp)
        time-delta (- time @prev-tick-time)]
    (reset! prev-tick-time time)
    (@tick-processor-fn time-delta))
  (.end stats)
  (. renderer render stage)
  (js/requestAnimFrame render))

(defn create-sprite [image x y]
  (let [texture (js/PIXI.Texture.fromImage image)
        sprite (js/PIXI.Sprite. texture)]
    (set! (.-position.x sprite) x)
    (set! (.-position.y sprite) y)
    sprite))

(defn create-container []
  (js/PIXI.DisplayObjectContainer.))

(defn add-child [sprite]
  (.addChild stage sprite))

(defn init [on-tick-fn]
  (.appendChild (.-body js/document) (.-view renderer))
  (.appendChild (.-body js/document) (.-domElement stats))
  (reset! prev-tick-time (current-timestamp))
  (reset! tick-processor-fn on-tick-fn) ;; TODO: get rid of atom here
  (render))
