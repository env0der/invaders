(ns invaders.client.state
  (:require
    [invaders.client.maps :as maps]))

(def game-map (:clearshore maps/maps))

(defn game-map-to-grid [game-map]
  (vec (flatten (map-indexed (fn [y v]
                               (map-indexed (fn [x type] {:x x :y y :type type}) v)
                               ) game-map))))

(def ui (atom { :map {}
                :sprites {}
                :selected (list) }))

(def game (atom {:units { 1 {:type :marsman :x 1 :y 7}
                          2 {:type :marsman :x 2 :y 5}
                          3 {:type :marsman :x 6 :y 4}
                          4 {:type :marsman :x 2 :y 2} }}))
