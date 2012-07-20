(ns leiningen.test.junit
  (:refer-clojure :exclude [read])
  (:import [org.apache.tools.ant.types FileSet]
           [org.apache.tools.ant.taskdefs.optional.junit
            BatchTest BriefJUnitResultFormatter FormatterElement JUnitTask JUnitResultFormatter
            PlainJUnitResultFormatter SummaryJUnitResultFormatter XMLJUnitResultFormatter]
           java.io.File)
  (:require [clojure.test :refer :all]
            [lancet.core :as lancet]
            [leiningen.core.project :refer [read]]
            [leiningen.junit :refer :all]
            [leiningen.test.helper :refer :all]))

(def project (read "sample/project.clj"))
(def fileset-spec ["classes" :includes "**/*Test.class"])

(deftest test-configure-batch-test
  (is (instance? BatchTest (configure-batch-test project (lancet/junit {}) (testcase-fileset project)))))

(deftest test-configure-classpath
  (is (configure-classpath project (lancet/junit {}))))

(deftest test-configure-jvm-args
  (configure-jvm-args project (lancet/junit {})))

(deftest test-junit-options
  (is (= (junit-options project) {:fork "on" :haltonerror "off" :haltonfailure "off"})))

(deftest test-junit-formatter-class
  (are [type expected-class]
    (is (= (junit-formatter-class type) expected-class))
    :brief BriefJUnitResultFormatter
    :plain PlainJUnitResultFormatter
    :summary SummaryJUnitResultFormatter
    :xml XMLJUnitResultFormatter
    "brief" BriefJUnitResultFormatter
    "plain" PlainJUnitResultFormatter
    "summary" SummaryJUnitResultFormatter
    "xml" XMLJUnitResultFormatter))

(deftest test-junit-formatter-element
  (are [type expected-class]
    (let [formatter-element (junit-formatter-element type)]
      (is (isa? (class formatter-element) FormatterElement))
      (is (= (.getClassname formatter-element) (.getName (junit-formatter-class type)))))
    :brief :plain :summary :xml
    "brief" "plain" "summary" "xml"))

(deftest test-extract-task
  (let [task (extract-task project)]
    (is (isa? (class task) JUnitTask)))
  (let [task (extract-task project "com.example")]
    (is (isa? (class task) JUnitTask))))

(deftest test-testcase-fileset
  (are [fileset expected]
    (is (= expected (seq (.getIncludedFiles (.getDirectoryScanner fileset)))))
    (testcase-fileset project) ["com/example/SubscriptionTest.class"]
    (testcase-fileset project "com.example") ["com/example/SubscriptionTest.class"]
    (testcase-fileset project "com.example.Subscription") ["com/example/SubscriptionTest.class"]
    (testcase-fileset project "com.example" "com.other") ["com/example/SubscriptionTest.class"]
    (testcase-fileset project "com.other") nil))

(deftest test-junit
  (junit project)
  (junit project "com.example" "com.other"))
