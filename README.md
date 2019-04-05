# LEIN-JUNIT [![Build Status](https://travis-ci.org/febeling/lein-junit.png)](https://travis-ci.org/febeling/lein-junit)

A Leiningen plugin that runs Java tests via JUnit.

## Installation

Via Clojars: http://clojars.org/lein-junit

## Configuration

The following options are accepted in `defproject`:
- `:junit-formatter`: keyword or string arg, can be `:brief` (default), `:plain`, `:xml`, or `:summary`
- `:junit-results-dir`: output folder for test results. If unset, outputs to standard out
- `:junit-test-file-pattern`: regex pattern used to match tests. Defaults to `#".*Test\.java"`

These arguments are acceptable on the CLI as well, and can be given with or without the leading ":".

## Example Configuration

```clojure
(defproject sample-project "0.0.1-SNAPSHOT"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:dev {:dependencies [[junit/junit "4.11"]]}}
  :plugins [[lein-junit "1.1.9-SNAPSHOT"]]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java" "test/java"]
  :junit ["test/java"]
  :junit-formatter :plain
  :junit-results-dir "test-results"
  :jvm-opts ["-XX:MaxPermSize=128m"])
```

## Usage

Run all junit tests.

    lein junit

Run all junit tests matching a pattern.

    lein junit com.example

Run all junit tests matching a paterrn, outputting XML results to "test-results/"

    lein junit :junit-formatter xml :junit-results-dir test-results com.example


## License

Copyright (C) 2013-2015 Florian Ebeling, Roman Scherer

Distributed under the Eclipse Public License, the same as Clojure.
