(ns invaders.views.index
  (:require [invaders.views.layout :as layout]
            [hiccup.core :refer [h]]))

(defn render []
  (layout/common "Invaders"
                 [:script {:src "js/vendor/pixi.js"}]
                 [:script {:src "js/build/invaders.js"}]))
