;; This project is used for lein-junit's test suite, so don't change
;; any of these values without updating the relevant tests. If you
;; just want a basic project to work from, generate a new one with
;; "lein new".

(defproject sample-project "0.0.1-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.3.0"]]
  :plugins [[lein-javac "1.3.0"]
            [lein-junit "1.0.0"]]
  :hooks [leiningen.hooks.javac leiningen.hooks.junit]
  :source-path "src/clojure"
  :javac-source-path [["src/java"] ["test/java"]]
  :junit [["classes"]]
  :jvm-opts ["-XX:MaxPermSize=128m"])
