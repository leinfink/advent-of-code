(ns aoc22.day1-test
  (:require
   [clojure.test :refer [deftest is]]
   [aoc22.day1 :as day1]))

(defn read-input [day]
  (slurp (str "inputs/aoc22/day" day)))

(deftest day1-test
  (let [input (read-input 1)]
    (is (= (day1/part1 input) 70720))
    (is (= (day1/part2 input) 207148))))
