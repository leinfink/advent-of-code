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
        (for [[type modifier] {:input identity :answer parse-answer}]
          [type (modifier (read-file day type))])))

(defn- ns-concat [namespace]
  (fn [x] (symbol (str namespace x))))

(defmacro def-daytest [day]
  (let [day-ns (ns-concat (str "aoc22.day" day))]
    `(deftest ~(day-ns "-test")
       (let [puzzle# (read-puzzle ~day)
             input# (:input puzzle#)
             answer# (:answer puzzle#)]
         (is (= (first answer#) (str (~(day-ns "/part1") input#))))
         (is (= (second answer#) (str (~(day-ns "/part2") input#))))))))

(defmacro generate-daytests [max-day]
  `(do
     ~@(for [day (range 1 (inc max-day))]
         `(def-daytest ~day))))

(generate-daytests 3)
