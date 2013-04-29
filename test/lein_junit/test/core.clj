(ns lein-junit.test.core
  (:refer-clojure :exclude [read])
  (:import [org.apache.tools.ant.types FileSet]
           [org.apache.tools.ant.taskdefs.optional.junit
            BatchTest BriefJUnitResultFormatter FormatterElement JUnitTask JUnitResultFormatter
            PlainJUnitResultFormatter SummaryJUnitResultFormatter XMLJUnitResultFormatter]
           java.io.File)
  (:require [clojure.test :refer :all]
            [lancet.core :as lancet]
            [lein-junit.core :refer :all]
            [leiningen.core.project :refer [read]]))

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

(deftest test-junit-extract-formatter
  ;; FormatterElement.getUseFile is declared package private, so we
  ;; need to make it accessible before we can invoke it.
  (let [call-get-use-file (fn [obj]
                              (-> FormatterElement (.getDeclaredMethod (name "getUseFile")
                                                            (into-array Class nil))
                                  (doto (.setAccessible true))
                                  (.invoke obj (into-array Object nil))))
        with-file (junit-formatter-element :plain "on")
        without-file (junit-formatter-element :plain "off")
        formatter-file-off-by-default (junit-formatter-element :plain)]
    (is (call-get-use-file with-file) true)
    (is (call-get-use-file without-file) false)
    (is (call-get-use-file formatter-file-off-by-default) false)))

(deftest test-extract-task
  (let [task (extract-task project)]
    (is (isa? (class task) JUnitTask)))
  (let [task (extract-task project "com.example")]
    (is (isa? (class task) JUnitTask))))

(deftest test-testcase-fileset
  (are [fileset expected]
       (is (= (sort expected)
              (sort (seq (.getIncludedFiles (.getDirectoryScanner fileset))))))
       (testcase-fileset project)
       ["com/example/SubscriptionTest.class"
        "com/other/SubscriptionTest.class"]
       (testcase-fileset project "com.example")
       ["com/example/SubscriptionTest.class"]
       (testcase-fileset project "com.example.Subscription")
       ["com/example/SubscriptionTest.class"]
       (testcase-fileset project "com.example" "com.other")
       ["com/example/SubscriptionTest.class"
        "com/other/SubscriptionTest.class"]
       (testcase-fileset project "com.another") nil))

(deftest test-junit-all
  (try
    (junit project)
    (catch clojure.lang.ExceptionInfo e
      (is (= "Suppressed exit" (.getMessage e))))))

(deftest test-junit-selector
  (try
    (junit project "com.example" "com.other")
    (catch clojure.lang.ExceptionInfo e
      (is (= "Suppressed exit" (.getMessage e))))))
