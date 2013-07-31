(defproject sample-project "0.0.1-SNAPSHOT"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.5.0"]]
  :profiles {:dev {:dependencies [[junit/junit "4.11"]]}}
  :plugins [[lein-junit "1.1.3"]]
  :hooks []
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java" "test/java"]
  :junit ["test/java"]
  :jvm-opts ["-XX:MaxPermSize=128m"])
