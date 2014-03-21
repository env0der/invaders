(defproject invaders "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [ring.middleware.logger "0.4.0"]
                 [hiccup "1.0.5"]
                 [com.novemberain/monger "1.7.0"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler invaders.handler/app
         :init invaders.handler/init
         :auto_reload? true
         :auto_refresh? false}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
