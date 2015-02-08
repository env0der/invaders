(ns invaders.client.stage)

(def stage (js/PIXI.Stage. 0xE3FCFF))
(def renderer (js/PIXI.autoDetectRenderer (.-innerWidth js/window) (.-innerHeight js/window)))

(def stats (js/Stats.))
(set! (-> (.-domElement stats) .-style .-position) "absolute")
(set! (-> (.-domElement stats) .-style .-display) "inline-block")
(set! (-> (.-domElement stats) .-style .-top) "0px")
(set! (-> (.-domElement stats) .-style .-right) "0px")

(def last-time (atom 0))

(defn- current-timestamp []
  (.getTime (js/Date.)))

(def tick-processor-fn (atom (fn [delta])))

(defn- render []
  (.begin stats)
  (let [time (current-timestamp)
        time-delta (- time @last-time)]
    (reset! last-time time)
    (@tick-processor-fn time-delta))
  (.end stats)
  (. renderer render stage)
  (js/requestAnimFrame render))

(defn add-child [sprite]
  (.addChild stage sprite))

(defn init [on-tick-fn]
  (.appendChild (.-body js/document) (.-view renderer))
  (.appendChild (.-body js/document) (.-domElement stats))
  (reset! last-time (current-timestamp))
  (reset! tick-processor-fn on-tick-fn) ;; TODO: get rid of atom here
  (render))
