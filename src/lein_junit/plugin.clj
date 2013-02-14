(ns lein-junit.plugin
  (:require [leiningen.junit :refer [junit]]
            [leiningen.test :as test]
            [robert.hooke :refer [add-hook]]))

(defn junit-hook [task & args]
  (apply task args)
  (apply junit args))

(defn hooks []
  (add-hook #'test/test junit-hook))
