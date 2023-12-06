(ns aoc23.day06
  (:require [clojure.string :as str]))

(defn parse-line-part1 [line]
  (->> (rest (str/split line #" +"))
       (map parse-long)))

(defn parse-part1 [input]
  (->> (str/split-lines input)
       (map parse-line-part1)
       (apply interleave)
       (partition 2)))

(defn parse-line-part2 [line]
  (->> (rest (str/split line #" +"))
       (apply str)
       parse-long))

(defn parse-part2 [input]
  (->> (str/split-lines input)
       (map parse-line-part2)
       list))

(defn result [hold [length _]]
  (* (- length hold) hold))

(defn breaks-record [hold [_ record :as race]]
  (> (result hold race) record))

(defn winning-options [[length _ :as race]]
  (filter #(breaks-record % race) (range length)))

(defn part1 [input]
  (->> (parse-part1 input)
       (map winning-options)
       (map count)
       (reduce *)))

(defn part2 [input]
  (->> (parse-part2 input)
       (map winning-options)
       (map count)
       first))
