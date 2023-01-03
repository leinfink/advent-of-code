(ns aoc22.day3
  (:require
   [clojure.set :as set]
   [clojure.string :as str]))

(defn read-rucksacks [input]
  (map #(split-at (/ (count %) 2) %) (str/split-lines input)))

(defn find-duplicate [[comp1, comp2]]
  (first (set/intersection (set comp1)
                           (set comp2))))

(defn priority [item]
  (- (int item)
     (if (Character/isUpperCase item)
       (- 64 26) 96)))

(defn badges [rucksacks]
  (for [group (partition 3 rucksacks)]
    (->> group
         (map #(set (flatten %)))
         (apply set/intersection)
         first)))

(defn sum-duplicate-priorities [rucksacks]
  (->> rucksacks
       (map #(priority (find-duplicate %)))
       (reduce +)))

(defn sum-badge-priorities [rucksacks]
  (->> (badges rucksacks)
       (map priority)
       (reduce +)))

(defn part1 [input]
  (sum-duplicate-priorities (read-rucksacks input)))

(defn part2 [input]
  (sum-badge-priorities (read-rucksacks input)))
