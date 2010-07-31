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

(deftest test-expand-path  
  (is (= (expand-path *project* "/tmp")
         "/tmp"))
  (is (= (expand-path *project* "src")
         (str (:root *project*) File/separator "src"))))

(deftest test-extract-formatter
  (let [formatter (extract-formatter *project*)]
    (is (isa? (class formatter) FormatterElement))
    (is (= (.getClassname formatter) (str SummaryJUnitResultFormatter)))))

(deftest test-extract-fileset
  (let [fileset (extract-fileset *project* ["classes" :includes "**/*Test.class"])]
    (is (isa? (class fileset) FileSet))
    (is (= (str (.getDir fileset lancet/ant-project)) (expand-path *project* "classes")))))

(deftest test-junit-options
  (is (= (junit-options *project*) {})))

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
      (is (= (.getClassname formatter-element) (str (junit-formatter-class type)))))
    :brief :plain :summary :xml
    "brief" "plain" "summary" "xml"))

(deftest test-extract-task
  (let [task (extract-task *project* ["sample/classes" :includes "**/*Test.class"])]
    (is (isa? (class task) JUnitTask))))

(deftest test-extract-tasks
  (let [tasks (extract-tasks *project*)]
    (is (every? #(isa? (class %) JUnitTask) tasks))))

(deftest test-junit
  (junit *project*))

(extract-task *project* ["sample/classes"])

;; (.execute (first (extract-tasks *project*)))
