(defproject invaders "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :source-paths ["src/clj" "src/cljs"]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [compojure "1.1.6"]
                 [ring/ring-devel "1.1.8"]
                 [ring/ring-core "1.1.8"]
                 [http-kit "2.0.0"]
                 [ring.middleware.logger "0.4.0"]
                 [hiccup "1.0.5"]
                 [com.novemberain/monger "1.7.0"]
                 [prismatic/dommy "0.1.2"]]
  :plugins [[lein-cljsbuild "1.0.0"] [lein-pdo "0.1.1"]]
  :aliases {"up" ["pdo" "cljsbuild" "auto," "run" "-dev"]}
  :cljsbuild {:builds
              [{:source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/build/invaders.js"
                           :optimizations :whitespace
                           :pretty-print true}}]}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}}
  :main invaders.handler)
