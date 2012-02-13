;; This project is used for lein-junit's test suite, so don't change
;; any of these values without updating the relevant tests. If you
;; just want a basic project to work from, generate a new one with
;; "lein new".

(defproject sample-project "0.0.1-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.2.0-RC1"]]
  :dev-dependencies [[lein-junit "0.0.4-SNAPSHOT"]
                     [junit/junit "4.8.1"]]
  :source-path "src/clojure"
  :java-source-path [["src/java"] ["test/java"]]
  :junit [["classes"]]
  :jvm-opts ["-XX:MaxPermSize=128m"])
