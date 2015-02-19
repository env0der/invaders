(ns invaders.main
  (:use [invaders.logging :only [log]])
  (:require
   [clojure.browser.repl :as repl]
   [brute.entity :as entity]
   [brute.system :as system]
   [invaders.stage :as stage]
   [invaders.components :as components]
   [invaders.systems.rendering :as rendering-system]
   [invaders.maps :as maps]))

(defn create-invader [system x y image]
  (let [invader (entity/create-entity)]
    (-> system
        (entity/add-entity invader)
        (entity/add-component invader (components/->Position x y))
        (entity/add-component invader (components/->Drawable image nil nil)))))

;; (repl/connect "http://localhost:9000/repl")

(defn init-system [system]
  (-> system
      (maps/create :swallownest)
      (create-invader 500 500 "/images/marsman.png")
      (system/add-system-fn rendering-system/process-one-game-tick)))


(def sys (atom (init-system (entity/create-system))))
(stage/init (fn [time-delta] (reset! sys (system/process-one-game-tick @sys time-delta))))
