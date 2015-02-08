(ns invaders.client.systems.rendering
  (:require [invaders.client.components :as components]
            [brute.entity :as entity]
            [invaders.client.stage :as stage]
            [clojure.set :as set]))

(defn stage-drawable-entity [system entity]
  (let [drawable (entity/get-component system entity components/Drawable)
        position (entity/get-component system entity components/Position)
        sprite (stage/create-sprite (:image drawable) (:x position) (:y position))]
    (stage/add-child sprite)
    (update-in system [:staged] merge {entity sprite})))

(defn stage-not-staged-drawables [system]
  (let [entities (set/intersection
                  (set (entity/get-all-entities-with-component system components/Drawable))
                  (set (entity/get-all-entities-with-component system components/Position)))]
    (->> entities
         (filter #(nil? (get-in system [:staged %])))
         (reduce stage-drawable-entity system))))

(defn process-one-game-tick [system time-delta]
  (stage-not-staged-drawables system))
