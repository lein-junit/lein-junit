# lein-junit

This is a Leiningen plugin that allows to run JUnit test suites.

(If you have a good idea how to make whole targets optional and only
show up in 'lein help' if wanted, drop me a note.)

## Usage

Install, see below. Then declare dev-dependency in project.clj:

    :dev-dependencies [[lein-javac "0.0.3-SNAPSHOT"]
                      ...
                      ]

Run 'lein deps'. Afterwards 'lein help' should show additional targets:

    test-java
    test-java-integration

Put a section into your project.clj:

    :java-tests { :unit
                  { :source-path  "test/java/unit"
                    :compile-path "build/test/unit/classes"
                    :fixture-path "test/fixtures" }
                  :integration
                  { :source-path  "test/java/integration"
                    :compile-path "build/test/integration/classes"
                    :fixture-path "test/fixtures" }}

(If you want to compile java tests with Leiningen as well have a look
at my lein-javac branch: http://github.com/febeling/lein-javac)

## Installation

Run

    lein install

in the project's root directory, to install to local repository.

## License

Same as Clojure, EPL1.0.
