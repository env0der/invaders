(ns invaders.views.index
  (:require [invaders.views.layout :as layout]
            [hiccup.core :refer [h]]))

(defn render []
  (layout/common "Invaders"
                 [:div "We came in peace!"]))
