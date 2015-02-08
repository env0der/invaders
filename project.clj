(defproject invaders "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :source-paths ["src/clj" "src/cljs"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2760"]
                 [compojure "1.3.1"]
                 [ring/ring-devel "1.3.2"]
                 [ring/ring-core "1.3.2"]
                 [http-kit "2.1.18"]
                 [ring.middleware.logger "0.5.0"]
                 [hiccup "1.0.5"]
                 [com.novemberain/monger "2.0.1"]
                 [prismatic/dommy "1.0.0"]]
  :plugins [[lein-cljsbuild "1.0.4"] [lein-pdo "0.1.1"]]
  :aliases {"up" ["pdo" "cljsbuild" "auto," "run" "-dev"]}
  :cljsbuild {:builds
              [{:source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/build/invaders.js"
                           :output-dir "resources/public/js/build"
                           :optimizations :none
                           :pretty-print true
                           :source-map true}}]}
  :main invaders.handler)
