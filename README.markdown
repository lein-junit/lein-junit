# lein-junit

This is a Leiningen plugin that allows to run JUnit test suites. If
you want to compile java tests with Leiningen as well have a look at
my [lein-javac](http://github.com/febeling/lein-javac) branch.

## Configuration

The directories containing the compiled java tests can be specified by
the "junit" option in the project.clj file. 

Example:

    (defproject sample-project "0.0.1-SNAPSHOT"
      :dependencies [[org.clojure/clojure "1.2.0-RC1"]]
      :dev-dependencies [[lein-javac "1.2.1-SNAPSHOT"]
                         [lein-junit "0.0.3-SNAPSHOT"]
                         [junit/junit "4.8.1"]]
      :source-path "src/clojure"
      :java-source-path [["src/java"] ["test/java"]]
      :junit [["classes"]]
      :jvm-opts ["-XX:MaxPermSize=128m"])


## Usage

Run all junit test.

    $ lein junit

Run all junit test in a specific directory.

    $ lein junit src/java

## Installation

Do it yourself, until this project has been put on clojars.

## License

Same as Clojure, EPL1.0.
