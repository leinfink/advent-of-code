(ns aoc22.day6
  (:require [clojure.string :as str]))

(defn solve [n s]
   (+ n (count (take-while #(> n (count (distinct %)))
                           (partition n 1 (str/trim s))))))

(defn part1 [s] (solve 4 s))

(defn part2 [s] (solve 14 s))
