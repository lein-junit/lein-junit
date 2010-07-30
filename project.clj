(defproject lein-junit "0.0.3-SNAPSHOT"
  :description "JUnit plugin for Leiningen"
  :dependencies [[ant/ant-launcher "1.6.5"]
                 [org.clojure/clojure "1.2.0-beta1"]
                 [org.clojure/clojure-contrib "1.2.0-beta1"]
                 [junit/junit "4.8.1"]
                 [ant/ant-junit "1.6.5"]
                 [ant/ant "1.6.5"]]
  :dev-dependencies [[leiningen/leiningen "1.2.1-SNAPSHOT"]
                     [swank-clojure "1.2.1"]])

