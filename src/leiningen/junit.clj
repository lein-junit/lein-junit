(ns leiningen.junit
  (:require lancet)
  (:use [leiningen.classpath :only [get-classpath find-lib-jars make-path]]
        [leiningen.compile :only (get-jvm-args)]
        [leiningen.deps :only (deps)]
        [leiningen.javac :only (javac)]
        [clojure.contrib.def :ony (defvar)])
  (:import [org.apache.tools.ant.types FileSet Path]
           [org.apache.tools.ant.taskdefs.optional.junit BriefJUnitResultFormatter
            FormatterElement SummaryJUnitResultFormatter PlainJUnitResultFormatter
            XMLJUnitResultFormatter]
           java.io.File))

(defvar *junit-options*
  {:fork "on"} "The default options for the JUnit task.")

(defmethod lancet/coerce [Path String] [_ str]
  (Path. lancet/ant-project str))

(defn- expand-path
  "Expand a path fragment relative to the project root. If path starts
  with File/separator it is treated as an absolute path and will not
  be modified."
  [project path]
  (if-not (= (str (first path)) File/separator)
    (str (:root project) File/separator path)
    path))

(defn- extract-fileset
  "Extract the fileset from the spec."
  [project [dir & options]]  
  (lancet/fileset
   (merge {:dir (expand-path project dir) :includes "**/*Test.class"}
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

(defn- junit-formatter-element
  "Returns a JUnit formatter element for the given type. Type can be a
  string or a keyword."
  [type & [use-file]]
  (doto (FormatterElement.)
    (.setClassname (.getName (junit-formatter-class type)))
    (.setUseFile false)))

(defn extract-formatter
  "Extract the Junit formatter element from the project."
  [project] (junit-formatter-element (or (:junit-formatter project) :summary)))

(defn- junit-options
  "Returns the JUnit options of the project."
  [project] (merge *junit-options* (:junit-options project)))

(defn- configure-classpath
  "Configure the classpath for the JUnit task."
  [project junit-task & paths]
  (let [classpath (.createClasspath junit-task)]
    (doseq [path (concat (get-classpath project) paths)]
      (.addExisting classpath (Path. lancet/ant-project (str path))))))

(defn- configure-jvm-args
  "Configure the JVM arguments for the JUnit task."
  [project junit-task]
  (doseq [arg (get-jvm-args project)]
      (when-not (re-matches #"^-Xbootclasspath.+" arg)
        (.setValue (.createJvmarg junit-task) arg))))

(defn- extract-task [project task-spec]
  (let [junit-task (lancet/junit (junit-options project))]
    (configure-classpath project junit-task (expand-path project (first task-spec)))
    (configure-jvm-args project junit-task)
    (doto (.createBatchTest junit-task)
      (.addFileSet (extract-fileset project task-spec))
      (.addFormatter (extract-formatter project)))
    junit-task))

(defn- extract-tasks [project]
  (let [tasks (map #(extract-task project %) (:junit project))]
    (if (empty? tasks)
      [(extract-task project ["classes" :includes "**/*Test.class"])]
      tasks)))

(defn junit [project & [directory]]
  (deps project)
  (javac project)
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
;;     (.. jt createClasspath (addExisting test-path))a
;;     (.execute jt)))

;; (defn test-java [project & params]
;;   (run-junit (:compile-path (unit-tests project))
;; 	     (test-path project :unit)))
