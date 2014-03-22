(defproject invaders "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [ring/ring-devel "1.1.8"]
                 [ring/ring-core "1.1.8"]
                 [http-kit "2.0.0"]
                 [ring.middleware.logger "0.4.0"]
                 [hiccup "1.0.5"]
                 [com.novemberain/monger "1.7.0"]]
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}}
  :main invaders.handler)
