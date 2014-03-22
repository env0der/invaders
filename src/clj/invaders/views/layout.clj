(ns invaders.views.layout
  (:require [hiccup.page :as h]))

(defn common [title & body]
  (h/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:title title]
    (h/include-css "/stylesheets/styles.css")]
   [:body
    [:div {:id "header"}
     [:h1 "Invaders"]]
    [:div {:id "content"} body]]))
