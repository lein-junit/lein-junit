(ns leiningen.junit
  (:require [lein-junit.core :as junit]))

(defn junit
  "Run the Java test suite via JUnit."
  [project & selectors]
  (apply junit/junit project selectors))
