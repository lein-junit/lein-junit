(ns leiningen.test-java
  (:require lancet)
  (:use [leiningen.compile :only [make-path find-lib-jars]])
  (:import [org.apache.tools.ant.types FileSet])
  (:import [org.apache.tools.ant.taskdefs.optional.junit FormatterElement]))

(defn unit-tests [project]
  (-> project :java-tests :unit))

(defn test-path [project category]
  (apply make-path
	 (or (:compile-path project)
	     (:root project) "/classes")
	 (:compile-path (category (:java-tests project)))
	 (:fixture-path (category (:java-tests project)))
         (:resources-path project)
         (find-lib-jars project)))

(defn plain-formatter []
  (doto (FormatterElement.)
    (.setClassname "org.apache.tools.ant.taskdefs.optional.junit.PlainJUnitResultFormatter")
    (.setUseFile false)))

(defn run-tests [test-compile-dir test-path]
  (let [fs (lancet/fileset {:dir test-compile-dir :includes "**/*Test.class"})
	jt (lancet/junit {})
	bt (.createBatchTest jt)]
    (.addFileSet bt fs)
    (.addFormatter bt (plain-formatter))
    (.. jt createClasspath (addExisting test-path))
    (.execute jt)))

(defn test-java [project & params]
  (run-tests (:compile-path (unit-tests project))
	     (test-path project :unit)))
