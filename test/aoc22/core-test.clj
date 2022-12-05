(ns aoc22.core-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest is]]))

(defn- read-file [day type]
  (slurp (str "inputs/aoc22/day" day "." (name type))))

(defn- parse-answer [input]
  (str/split-lines input))

(defn read-puzzle
  "Returns a hashmap with the input and the answers for the day."
  [day]
  (into {}
        (for [[type modifier] {:input identity
                               :answer parse-answer}]
          [type (modifier (read-file day type))])))

(defmacro def-daytest [day]
  (let [namespace (str "aoc22.day" day)
        testname (symbol (str namespace "-test"))
        part1 (symbol (str namespace "/part1"))
        part2 (symbol (str namespace "/part2"))]
    `(deftest ~testname
       (let [puzzle# (read-puzzle ~day)
             input# (:input puzzle#)
             answer# (:answer puzzle#)]
         (is (= (first answer#) (str (~part1 input#))))
         (is (= (second answer#) (str (~part2 input#))))))))

(defmacro generate-daytests [max-day]
  `(do
     ~@(for [day (range 1 (inc max-day))]
         `(def-daytest ~day))))

(generate-daytests 2)
