(ns leiningen.junit
  (:require lancet)
  (:use [leiningen.classpath :only [find-lib-jars make-path]]
        [clojure.contrib.def :ony (defvar)])
  (:import [org.apache.tools.ant.types FileSet]
           [org.apache.tools.ant.taskdefs.optional.junit BriefJUnitResultFormatter
            FormatterElement SummaryJUnitResultFormatter PlainJUnitResultFormatter XMLJUnitResultFormatter]))

;; org.apache.tools.ant.taskdefs.optional.junit.JUnitTask
;; (lancet/junit {})

(defvar *junit-options*
  { }
  "The default options for the JUnit task.")

(defn- extract-fileset
  "Extract the fileset from the spec."
  [[dir & options]]  
  (lancet/fileset
   (merge {:dir dir :includes "**/*Test.class"}
          (apply hash-map options))))

(defn- junit-formatter-class
  "Returns a JUnit formatter for the given type. Type can be a string
  or a keyword."
  [type]
  (condp = (keyword type)
      :brief BriefJUnitResultFormatter
      :plain PlainJUnitResultFormatter
      :xml XMLJUnitResultFormatter
      :summary SummaryJUnitResultFormatter))

(defn- junit-formatter-element [type & [use-file]]
  (doto (FormatterElement.)
    (.setClassname (str (junit-formatter-class type)))
    (.setUseFile false)))

(defn extract-formatter [project]
  (junit-formatter-element (or (:junit-formatter project) :summary)))

(defn- junit-options
  "Returns the JUnit options of the project."
  [project] (merge *junit-options* (:junit-options project)))

(defn- extract-task [project task-spec]
  (let [task (lancet/junit (junit-options project))]
    (doto (.createBatchTest task)
      (.addFileSet (extract-fileset task-spec))
      (.addFormatter (extract-formatter project)))
    task))

(defn- extract-tasks [project]
  (let [tasks (map #(extract-task project %) (:junit project))]
    (if (empty? tasks)
      [(extract-task project [(:compile-path project) :includes "**/*Test.class"])]
      tasks)))

(defn junit [project & [directory]]
  (println (extract-tasks project))
  (doseq [task (extract-tasks project)]    
    (.execute task)))

;; ;; (extract-fileset ["sample/classes" :includes "**/*TestCase.class"])

;; ;; (defn extract-tasks [project spec]
;; ;;   (let [task (lancet/junit {})
;; ;;         batch-test (.createBatchTest task)]
;; ;;     (->> batch-test
;; ;;          (add-filesets project)
;; ;;          (add-formatter project))
;; ;;     task))

;; ;; (defn- run-tasks
;; ;;   "Execute all tasks."
;; ;;   [tasks] (map #(.execute %) tasks))

;; (defn plain-formatter []
;;   (doto (FormatterElement.)
;;     (.setClassname "org.apache.tools.ant.taskdefs.optional.junit.SummaryJUnitResultFormatter")
;;     (.setUseFile false)))


;; (defn unit-tests [project]
;;   (-> project :java-tests :unit))

;; (defn test-path [project category]
;;   (apply make-path
;; 	 (or (:compile-path project)
;; 	     (:root project) "/classes")
;; 	 (:compile-path (category (:java-tests project)))
;; 	 (:fixture-path (category (:java-tests project)))
;;          (:resources-path project)
;;          (find-lib-jars project)))



;; (defn run-junit [test-compile-dir test-path]
;;   (let [fs (lancet/fileset {:dir test-compile-dir :includes "**/*Test.class"})
;; 	jt (lancet/junit {})
;; 	bt (.createBatchTest jt)]
;;     (.addFileSet bt fs)
;;     (.addFormatter bt (plain-formatter))
;;     (.. jt createClasspath (addExisting test-path))
;;     (.execute jt)))

;; (defn test-java [project & params]
;;   (run-junit (:compile-path (unit-tests project))
;; 	     (test-path project :unit)))
