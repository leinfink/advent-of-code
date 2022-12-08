(ns aoc22.day1
  (:require
   [aoc22.util :refer [for->]]
   [clojure.string :as str]))

(defn parse [input]
  (for-> input
         (#(str/split % #"\n\n"))
         str/split-lines
         Integer/parseInt))

(defn sorted-calories [elves]
  (sort > (map #(reduce + %) elves)))

(defn part1 [input]
  (first (sorted-calories (parse input))))

(defn part2 [input]
  (reduce + (take 3 (sorted-calories (parse input)))))
