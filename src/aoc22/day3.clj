(ns aoc22.day3
  (:require [clojure.string :as str]))

(defn read-rucksacks [input]
  (map #(split-at (/ (count %) 2) %)
       (str/split-lines input)))

(defn find-duplicate [[comp1, comp2]]
  (first (clojure.set/intersection (set comp1) (set comp2))))

(defn priority [item]
  (- (int item)
     (if (Character/isUpperCase item) (- 64 26) 96)))

(defn badges [rucksacks]
  (for [group (partition 3 rucksacks)]
    (->> (map #(set (flatten %)) group)
         (apply clojure.set/intersection)
         first)))

(defn sum-duplicate-priorities [rucksacks]
  (reduce + (map #(priority (find-duplicate %)) rucksacks)))

(defn sum-badge-priorities [rucksacks]
  (reduce + (map priority (badges rucksacks))))

(defn part1 [input]
  (sum-duplicate-priorities (read-rucksacks input)))

(defn part2 [input]
  (sum-badge-priorities (read-rucksacks input)))
