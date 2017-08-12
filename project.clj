(defproject clj-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [compojure "1.6.0"]
                 [cheshire "5.7.1"]
                 [ring/ring-defaults "0.3.1"]
                 [ring/ring-json "0.5.0-beta1"]
                 [com.novemberain/monger "3.1.0"]]
  :plugins [[lein-ring "0.12.0"]
            [lein-ancient "0.6.10"]]
  :ring {:handler clj-api.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.1"]]}})
