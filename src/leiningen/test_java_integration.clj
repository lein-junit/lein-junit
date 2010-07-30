(ns leiningen.test-java-integration
  (:require lancet)
  (:require leiningen.test-java)
  (:use [leiningen.classpath :only [make-path find-lib-jars]])
  (:import [org.apache.tools.ant.types FileSet])
  (:import [org.apache.tools.ant.taskdefs.optional.junit FormatterElement]))

(defn integration-tests [project]
  (-> project :java-tests :integration))

(defn test-java-integration [project & params]
  (leiningen.test-java/run-tests (:compile-path (integration-tests project))
				 (leiningen.test-java/test-path project :integration)))
