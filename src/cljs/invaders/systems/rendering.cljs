(ns invaders.systems.rendering
  (:use [invaders.logging :only [log]])
  (:require [invaders.components :as components]
            [brute.entity :as entity]
            [invaders.stage :as stage]
            [clojure.set :as set]))

(defn stage-drawable-entity [system entity]
  (let [drawable (entity/get-component system entity components/Drawable)
        drawable-parent-name (:group drawable)
        position (entity/get-component system entity components/Position)
        sprite (stage/create-sprite (:image drawable) (:x position) (:y position))]
    (if-not (nil? drawable-parent-name)
      (let [parent (get-in system [:staged-groups drawable-parent-name])]
        (.addChild parent sprite))
      (stage/add-child sprite))
    (update-in system [:staged] merge {entity sprite})))

(defn stage-not-staged-drawables [system]
  (let [entities (set/intersection
                  (set (entity/get-all-entities-with-component system components/Drawable))
                  (set (entity/get-all-entities-with-component system components/Position)))]
    (->> entities
         (filter #(nil? (get-in system [:staged %])))
         (sort-by #(:z-index (entity/get-component system % components/Drawable)))
         (reduce stage-drawable-entity system))))

(defn stage-group [system group]
  (let [container (stage/create-container)]
    (stage/add-child container)
    (update-in system [:staged-groups] merge {group container})))

(defn add-groups [system]
  (let [entities (entity/get-all-entities-with-component system components/Drawable)
        drawables (map #(entity/get-component system % components/Drawable) entities)
        groups (distinct (filter (comp not nil?) (map :group drawables)))]
    (->> groups
         (filter #(nil? (get-in system [:staged-groups %])))
         (reduce stage-group system))))


(defn process-one-game-tick [system time-delta]
  (-> system
      (add-groups)
      (stage-not-staged-drawables)))
