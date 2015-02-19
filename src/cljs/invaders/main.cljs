(ns invaders.main
  (:use [invaders.logging :only [log]])
  (:require
   [clojure.browser.repl :as repl]
   [brute.entity :as entity]
   [brute.system :as system]
   [invaders.stage :as stage]
   [invaders.components :as components]
   [invaders.systems.rendering :as rendering-system]))

(defn create-invader [system x y image]
  (-> system
      (entity/add-entity invader)
      (entity/add-component invader (components/->Position x y))
      (entity/add-component invader (components/->Drawable image))))

(defn create-map [system]
  system)

;; (repl/connect "http://localhost:9000/repl")

(defn init-system [system]
  (let [invader (entity/create-entity)]
    (-> system
        (create-map)
        (create-invader 500 500 "/images/marsman.png")
        (system/add-system-fn rendering-system/process-one-game-tick))))


(def sys (atom (init-system (entity/create-system))))
(stage/init (fn [time-delta] (reset! sys (system/process-one-game-tick @sys time-delta))))
