(ns aoc23.day01
  (:require [clojure.string :as str]))

(defn digits [line]
  (->> (map #(parse-long (str %)) line)
       (remove nil?)))

(defn compute-line [line]
  (->> (digits line)
       ((fn [x] [(first x) (last x)]))
       str/join
       parse-long))

(defn solve [input]
  (->> (str/split-lines input))
       (map compute-line)
       (reduce +)))

(defn part1 [input] (solve input))

(defn part2 [input]
  (let [replacer {"one" "1"
                  "two" "2"
                  "three" "3"
                  "four" "4" 
                  "five" "5"
                  "six" "6"
                  "seven" "7"
                  "eight" "8"
                  "nine" "9"}
        regex (re-pattern (str/join "|" (keys replacer)))]
    (solve (str/replace input regex replacer))))
