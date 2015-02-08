(ns invaders.client.systems.rendering
  (:require [invaders.client.components :as components]
            [brute.entity :as entity]
            [invaders.client.stage :as stage]
            [clojure.set :as set]))

(defn stage-drawable-entity [system entity]
  (let [drawable (entity/get-component system entity components/Drawable)
        position (entity/get-component system entity components/Position)
        sprite (js/PIXI.Sprite. (:image drawable))]
    (set! (.-position.x sprite) (:x position))
    (set! (.-position.y sprite) (:y position))
    (stage/add-child sprite)
    [entity sprite]))

(defn stage-not-staged-drawables [system delta]
  (let [entities (set/intersection
                  (set (entity/get-all-entities-with-component system components/Drawable))
                  (set (entity/get-all-entities-with-component system components/Position)))]
    (->> entities
         (filter #(nil? (get-in system [:staged %])))
         (map #(stage-drawable-entity system %))
         (reduce #(assoc-in %1 [:staged (first %2)] (second %2)) system))))

(defn process-one-game-tick [system delta]
  (stage-not-staged-drawables system delta))
