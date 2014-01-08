(defproject clojure-chat "0.1.0-SNAPSHOT"
  :description "Clojure + websocket demo"
  :url "https://github.com/patteri/clojure-chat"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [http-kit "2.1.16"]
                 [compojure "1.1.6"]
                 [org.clojure/data.json "0.2.1"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler clojure-chat.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})