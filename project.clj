(defproject lein-junit "1.1.5"
  :author "Caspar Florian Ebeling, Roman Scherer"
  :description "JUnit plugin for Leiningen"
  :url "http://github.com/febeling/lein-junit"
  :min-lein-version "2.0.0"
  :lein-release {:deploy-via :clojars}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.apache.ant/ant-junit "1.8.4"]
                 [org.clojure/clojure "1.5.0"]
                 [lancet "1.0.1"]
                 [junit/junit "4.11"]]
  :eval-in-leiningen true)
