(ns invaders.client.main
  (:use [invaders.client.logging :only [log]])
  (:require
   [clojure.browser.repl :as repl]
   [brute.entity :as entity]
   [brute.system :as system]
   [invaders.client.stage :as stage]
   [invaders.client.components :as components]
   [invaders.client.systems.rendering :as rendering-system]))

;; (repl/connect "http://localhost:9000/repl")

(defn init-system [system]
  (let [invader (entity/create-entity)]
    (-> system
        (entity/add-entity invader)
        (entity/add-component invader (components/->Position 500 500))
        (entity/add-component invader (components/->Drawable "/images/marsman.png"))
        (system/add-system-fn rendering-system/process-one-game-tick))))


(def sys (atom (init-system (entity/create-system))))
(stage/init (fn [time-delta] (reset! sys (system/process-one-game-tick @sys time-delta))))
