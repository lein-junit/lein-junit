(ns lein-junit.task-args
  (:require [clojure.string :refer [join]]))


(def ^:const arg-keys
  #{:junit-formatter :junit-results-dir :junit-test-file-pattern})

(defn keywordify-args [task-args]
  (mapv (fn [token]
          (if (= \: (first token))
            (keyword (join (rest token)))
            token))
        task-args))

(defn parse-task-args
  [project task-args]
  (let [options (select-keys project arg-keys)
        task-args (keywordify-args task-args)
        parsed-args {:options options :selectors []}
        opt-key (volatile! nil)]
    (reduce (fn [parsed-args token]
              (cond
               (contains? arg-keys token) (do (vreset! opt-key token)
                                              parsed-args)
               @opt-key (let [opt-key-val @opt-key]
                          (vreset! opt-key nil)
                          (assoc-in parsed-args [:options opt-key-val] token))
               :else (update parsed-args :selectors conj token)))
            parsed-args
            task-args)))
