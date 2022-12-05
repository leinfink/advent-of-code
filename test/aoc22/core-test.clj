(ns aoc22.core-test
  (:require
   [aoc22.day1 :as day1]
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

(deftest day1-test
  (let [puzzle (read-puzzle 1)
        input (:input puzzle)
        answer (:answer puzzle)]
    (is (= (first answer) (str (day1/part1 input))))
    (is (= (second answer) (str (day1/part2 input))))))
