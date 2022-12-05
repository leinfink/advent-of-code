(ns aoc22.day1
  (:require [clojure.string :as str]))

(defn parse [input]
  (for [elves (str/split input #"\n\n")
        :let [calories (str/split-lines elves)]]
    (map #(Integer/parseInt %) calories)))

(defn sorted-calories [elves]
  (sort > (map #(reduce + %) elves)))

(defn part1 [input]
  (first (sorted-calories (parse input))))

(defn part2 [input]
  (reduce + (take 3 (sorted-calories (parse input)))))
