(ns leiningen.test-java)
  (:require lancet)
  (:use [leiningen.compile :only [make-path find-lib-jars]])
  (:import [org.apache.tools.ant.types FileSet])
  (:import [org.apache.tools.ant.taskdefs.optional.junit FormatterElement]))

(defn test-path [project]
  (apply make-path
	 (or (:compile-path project)
	     (str project-root "/classes"))
         (:resources-path project)
         (find-lib-jars project)))

(defn plain-formatter []
  (doto (FormatterElement.)
    (.setClassname "org.apache.tools.ant.taskdefs.optional.junit.PlainJUnitResultFormatter")
    (.setUseFile false)))

(defn test-java [project & params]
  (let [fs (lancet/fileset {:dir (or (:compile-path project) 
				     (str project-root "/classes"))
			    :includes "**/*Test.class"})
	jt (lancet/junit {})
	bt (.createBatchTest jt)]
    (.addFileSet bt fs)
    (.addFormatter bt (plain-formatter))
    (.. jt createClasspath (addExisting (test-path project)))
    (.execute jt)))
