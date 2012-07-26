(defproject lein-junit "1.0.4-SNAPSHOT"
  :author "Caspar Florian Ebeling, Roman Scherer"
  :description "JUnit plugin for Leiningen"
  :url "http://github.com/febeling/lein-junit"
  :min-lein-version "2.0.0"
  :dependencies [[ant/ant-junit "1.6.5"]
                 [lancet "1.0.1"]
                 [junit/junit "4.10"]]
  :eval-in-leiningen true)
