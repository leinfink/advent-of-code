(ns aoc22.core-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest testing is]]))

(defn- read-file [day type]
  (slurp (str "inputs/aoc22/day" day "." (name type))))

(defn- parse-answer [input]
  (str/split-lines input))

(defn read-puzzle
  "Returns a hashmap with the puzzle input and answers for `day`.
  Input is read without modification. Answers are split into a list."
  [day]
  (into {}
        (for [[type modifier] {:input identity :answer parse-answer}]
          [type (modifier (read-file day type))])))

(defn- ns-concat [namespace]
  (fn [& x] (symbol (apply str (conj x namespace)))))

(defmacro def-daytest
  "Writes a test for both parts of the puzzle for `day`.
  The tested functions have to be called `part1` and `part2`
  and be within a `day`-specific namespace."
  [day]
  (let [ns-day (ns-concat (str "aoc22.day" day))]
    `(do (require '~(ns-day))
         (deftest ~(ns-day "-test")
           (let [puzzle# (read-puzzle ~day)
                 input# (:input puzzle#)
                 answer# (:answer puzzle#)]
             (testing "Part 1"
               (is (= (first answer#)
                      (str (~(ns-day "/part1") input#)))))
             (testing "Part 2"
               (is (= (second answer#)
                      (str (~(ns-day "/part2") input#))))))))))

(defmacro generate-daytests [max-day]
  `(do
     ~@(for [day (range 1 (inc max-day))]
         `(def-daytest ~day))))

(generate-daytests 4)
