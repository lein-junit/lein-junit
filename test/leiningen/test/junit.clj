(ns leiningen.test.junit
  (:import [org.apache.tools.ant.types FileSet]
           [org.apache.tools.ant.taskdefs.optional.junit
            BriefJUnitResultFormatter FormatterElement JUnitTask JUnitResultFormatter 
            PlainJUnitResultFormatter SummaryJUnitResultFormatter XMLJUnitResultFormatter]
           java.io.File)
  (:use [leiningen.core :only (defproject read-project)]
        clojure.test leiningen.junit leiningen.test.helper))

(refer-private 'leiningen.junit)

(def *project* (read-project "sample/project.clj"))
(def *fileset-spec* ["classes" :includes "**/*Test.class"])

(deftest test-configure-batch-test
  (configure-batch-test *project* (lancet/junit {}) (extract-filesets *project*)))

(deftest test-configure-classpath
  (configure-classpath *project* (lancet/junit {}) (extract-filesets *project*)))

(deftest test-configure-jvm-args
  (configure-jvm-args *project* (lancet/junit {})))

(deftest test-expand-path  
  (is (= (expand-path *project* "/tmp")
         "/tmp"))
  (is (= (expand-path *project* "src")
         (str (:root *project*) File/separator "src"))))

(deftest test-extract-formatter
  (let [formatter (extract-formatter *project*)]
    (is (isa? (class formatter) FormatterElement))
    (is (= (.getClassname formatter) (.getName SummaryJUnitResultFormatter)))))

(deftest test-extract-fileset
  (is (= (extract-fileset *project* *fileset-spec*)
         {:dir (expand-path *project* "classes") :includes "**/*Test.class"})))

(deftest test-extract-applicable-filesets
  (is (= (extract-filesets *project*)
         [(extract-fileset *project* *fileset-spec*)])))

(deftest test-extract-applicable-filesets
  (is (= (extract-applicable-filesets *project*)
         [(extract-fileset *project* *fileset-spec*)]))
  (is (= (extract-applicable-filesets *project* "classes")
         [(extract-fileset *project* *fileset-spec*)]))
  (is (empty? (extract-applicable-filesets *project* "not-existing"))))

(deftest test-junit-options
  (is (= (junit-options *project*) {:fork "on" :haltonerror "off" :haltonfailure "off"})))

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
  (let [task (extract-task *project*)]
    (is (isa? (class task) JUnitTask)))
  (let [task (extract-task *project* "classes")]
    (is (isa? (class task) JUnitTask))))

(deftest test-junit
  (junit *project*))
