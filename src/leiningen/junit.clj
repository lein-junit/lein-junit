(ns leiningen.junit
  (:require lancet)
  (:use [leiningen.classpath :only [get-classpath find-lib-jars make-path]]
        [leiningen.compile :only (get-jvm-args)]
        [leiningen.deps :only (deps)]
        [leiningen.javac :only (javac)]
        [clojure.contrib.def :only (defvar)]
        [clojure.contrib.seq :only (includes?)])
  (:import [org.apache.tools.ant.types FileSet Path]
           [org.apache.tools.ant.taskdefs.optional.junit BriefJUnitResultFormatter
            FormatterElement SummaryJUnitResultFormatter PlainJUnitResultFormatter XMLJUnitResultFormatter]
           java.io.File))

(defvar *junit-options*
  {:fork "on" :haltonerror "off" :haltonfailure "off"}
  "The default options for the JUnit task.")

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
  "Extract the fileset from the specification."
  [project [dir & options]]    
  (merge {:dir (expand-path project dir) :includes "**/*Test.class"}
         (apply hash-map options)))

(defn- extract-filesets
  "Extract the filesets from the project."
  [project] (map #(extract-fileset project %) (:junit project)))

(defn- extract-applicable-filesets
  "Extract the applicable filesets from the project."
  [project & directories]
  (let [directories (map #(expand-path project %) directories)]
    (filter #(or (empty? directories) (includes? directories (:dir %)))
            (extract-filesets project))))

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

(defn- extract-formatter
  "Extract the Junit formatter element from the project."
  [project] (junit-formatter-element (or (:junit-formatter project) :brief)))

(defn- junit-options
  "Returns the JUnit options of the project."
  [project] (merge *junit-options* (:junit-options project)))

(defn- configure-batch-test
  "Configure the JUnit batch test."
  [project junit-task filesets]
  (let [batch-task (.createBatchTest junit-task)
        junit-options (junit-options project)]
    (doseq [fileset filesets]
      (.addFileSet batch-task (lancet/fileset fileset)))
    (doto batch-task      
      (.addFormatter (extract-formatter project))
      (.setFork (lancet/coerce Boolean/TYPE (:fork junit-options)))
      (.setHaltonerror (lancet/coerce Boolean/TYPE (:haltonerror junit-options)))
      (.setHaltonfailure (lancet/coerce Boolean/TYPE (:haltonfailure junit-options))))))

(defn- configure-classpath
  "Configure the classpath for the JUnit task."
  [project junit-task filesets]
  (let [classpath (.createClasspath junit-task)]
    (doseq [path (concat (get-classpath project) (map :dir filesets))]
      (.addExisting classpath (Path. lancet/ant-project (str path))))))

(defn- configure-jvm-args
  "Configure the JVM arguments for the JUnit task."
  [project junit-task]
  (doseq [arg (get-jvm-args project)]
    (when-not (re-matches #"^-Xbootclasspath.+" arg) ; Copied from Leiningen
      (.setValue (.createJvmarg junit-task) arg))))

(defn- extract-task [project & directories]
  (let [filesets (apply extract-applicable-filesets project directories)
        junit-task (lancet/junit (junit-options project))]
    (configure-batch-test project junit-task filesets)
    (configure-classpath project junit-task filesets)
    (configure-jvm-args project junit-task)
    junit-task))

(defn junit [project & directories]
  (when (empty? (find-lib-jars project))
    (deps project))
  (javac project)
  (.execute (apply extract-task project directories)))
