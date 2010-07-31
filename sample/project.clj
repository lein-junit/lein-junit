;; This project is used for lein-junit's test suite, so don't change
;; any of these values without updating the relevant tests. If you
;; just want a basic project to work from, generate a new one with
;; "lein new".

(defproject sample-project "0.0.1-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.2.0-RC1"]]
  :dev-dependencies [[lein-javac "1.2.1-SNAPSHOT"]
                     [lein-junit "0.0.3-SNAPSHOT"]]
  :source-path "src/clojure"
  :java-source-path [["src/java"] ["test/java"]]
  :junit [["classes" :includes "**/*Test.class"]])
