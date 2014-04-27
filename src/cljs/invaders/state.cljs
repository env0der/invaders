(ns invaders.client.state)

(def ui (atom { :map {}
                :sprites {} }))

(def game (atom {:units { 1 {:type :marsman :x 1 :y 7}
                          2 {:type :marsman :x 2 :y 5}
                          3 {:type :marsman :x 6 :y 4}
                          4 {:type :marsman :x 2 :y 2} }}))
