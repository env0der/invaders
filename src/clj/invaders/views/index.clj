(ns invaders.views.index
  (:require [invaders.views.layout :as layout]
            [hiccup.core :refer [h]]))

(defn render []
  (layout/common "Invaders"
                 [:script {:src "js/build/invaders.js"}]
                 [:canvas#gamefield {:height 500 :width 1000}]))
