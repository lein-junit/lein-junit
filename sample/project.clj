(defproject sample-project "0.0.1-SNAPSHOT"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :profiles {:dev {:dependencies [[junit/junit "4.10"]]}}
  :plugins [[lein-junit "1.0.2-SNAPSHOT"]]
  :hooks [leiningen.hooks.junit]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java" "test/java"]
  :junit ["test/java"]
  :jvm-opts ["-XX:MaxPermSize=128m"])
