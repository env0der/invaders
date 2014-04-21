(ns invaders.views.index
  (:require [invaders.views.layout :as layout]
            [hiccup.core :refer [h]]))

(defn render []
  (layout/common "Invaders"
                 [:script {:src "js/vendor/pixi.js"}]
                 [:script {:src "js/vendor/stats.js"}]
                 [:script {:src "js/build/goog/base.js"}]
                 [:script {:src "js/build/invaders.js"}]
                 [:script "goog.require('invaders.client.main');"]))
