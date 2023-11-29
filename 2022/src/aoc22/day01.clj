(ns aoc22.day01
  (:require [clojure.string :as str]))

(defn parse [input]
  (->> (str/split-lines input)
       (map parse-long)
       (partition-by nil?)
       (take-nth 2)))

(defn highest [n coll]
  (reduce (fn [acc val] (if (> val (first acc))
                          (rest (sort (conj acc val)))
                          acc))
          (repeat n 0)
          coll))

(defn solve [n elves] (highest n (map #(reduce + %) elves)))

(defn part1 [input] (first (solve 1 (parse input))))

(defn part2 [input] (reduce + (solve 3 (parse input))))
