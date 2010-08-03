(ns leiningen.hooks.junit
  (:require leiningen.test)
  (:use [leiningen.junit :only (junit)]
        robert.hooke))

(defn test-junit-hook [task & args]
  (apply junit args)
  (apply task args))

(add-hook #'leiningen.test/test test-junit-hook)
