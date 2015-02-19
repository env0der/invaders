(ns invaders.maps
  (:use [invaders.logging :only [log]])
  (:require [brute.entity :as entity]
            [invaders.components :as components]))

(def maps {
           :swallownest [
                         [:g :g :g :g :g :g :g :g :g]
                         [:g :g :g :g :g :w :w :w :g]
                         [:g :g :g :g :g :w :w :w :g]
                         [:w :w :g :g :g :w :w :g :g]
                         [:w :w :g :g :g :g :g :g :g]
                         [:g :g :g :g :w :g :g :g :g]
                         [:g :g :g :g :g :w :g :g :g]
                         [:g :g :g :g :g :w :w :g :g]
                         ]
           :clearshore [
                        [:g :g :g :s :w :s :g :g :g :g :g :s :w :w :w :w :w :s]
                        [:g :g :g :s :s :s :g :g :g :g :g :s :w :w :w :w :w :w]
                        [:g :g :g :s :w :s :g :g :g :g :g :s :w :w :w :s :s :s]
                        [:s :s :s :s :w :s :s :s :s :s :s :s :w :w :s :s :s :s]
                        [:w :w :w :s :w :w :w :w :s :w :w :w :w :w :w :w :w :w]
                        [:s :s :s :s :w :s :s :s :s :s :s :s :s :s :w :s :s :s]
                        [:g :g :g :s :s :s :g :g :g :g :g :g :s :w :w :s :s :s]
                        [:g :g :g :s :w :s :g :g :g :s :s :s :w :w :w :s :s :s]
                        [:g :g :g :s :w :s :g :g :g :s :w :w :w :s :s :s :s :s]
                        [:w :w :w :w :w :s :g :g :g :s :w :w :w :s :g :g :g :g]
                        [:w :w :g :s :w :s :g :g :g :s :w :w :w :s :g :g :g :g]
                        [:w :w :g :s :w :s :g :s :s :s :w :w :w :w :w :g :g :g]
                        [:w :g :g :s :w :s :g :s :s :s :w :w :w :s :w :w :g :g]
                        [:w :w :g :s :w :s :g :s :s :s :w :w :w :s :s :w :w :g]
                        [:w :w :g :s :w :s :g :s :s :s :w :w :w :s :s :s :w :w]
                        ]
           :rosewald [
                      [:g :g :g :g :g :g :g :g :g]
                      [:g :g :g :g :g :w :w :w :g]
                      [:g :g :g :g :g :w :w :w :g]
                      [:w :w :g :g :g :w :w :g :g]
                      [:w :w :g :g :g :g :g :g :g]
                      [:g :g :g :g :w :g :g :g :g]
                      [:g :g :g :g :g :w :g :g :g]
                      [:g :g :g :g :g :w :w :g :g]
                      ]
           })

(def tiles {:w "/images/water.png"
            :g "/images/grass.png"
            :s "/images/sand.png"})

(defn game-map-to-grid [game-map]
  (vec (map-indexed #(merge %2 {:z-index %1}) (flatten (map-indexed (fn [y v] (map-indexed (fn [x type] {:x x :y y :type type}) v)) game-map)))))

(defn map-to-stage-coord [map-x map-y]
  [(+ (* (mod map-y 2) 40) (* 80 map-x)) (+ (* 50 map-y) 35)])

(defn create-tile [system {map-x :x map-y :y type :type z-index :z-index}]
  (let [tile (entity/create-entity)
        [x y] (map-to-stage-coord map-x map-y)
        image (get tiles type)]
    (-> system
        (entity/add-entity tile)
        (entity/add-component tile (components/->Position x y))
        (entity/add-component tile (components/->Drawable image :map z-index)))))

(defn create [system map-name]
  (let [vmap (game-map-to-grid (map-name maps))]
    (reduce create-tile system vmap)))
