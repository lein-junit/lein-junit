(defproject lein-junit "0.0.3-SNAPSHOT"
  :author "Caspar Florian Ebeling, Roman Scherer"
  :description "JUnit plugin for Leiningen"
  :url "http://github.com/febeling/lein-junit"
  :dependencies [[ant/ant-junit "1.6.5"]
                 [org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]]
  :dev-dependencies [[leiningen/leiningen "1.3.0-SNAPSHOT"]
                     [lein-javac "1.2.1-SNAPSHOT"]
                     [swank-clojure "1.2.1"]])

