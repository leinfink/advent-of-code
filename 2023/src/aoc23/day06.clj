(comment
  (def input (slurp "inputs/input6.txt"))
  (def input (slurp "inputs/example6.txt"))
  )

(ns aoc23.day06
  (:require [clojure.string :as str]))

(defn parse [input]
  (let [[times distances] (str/split-lines input)
        times (->> (rest (str/split times #" +"))
                   (map parse-long))
        distances (->> (rest (str/split distances #" +"))
                       (map parse-long))]
    (partition 2 (interleave times distances))))

(defn parse2 [input]
  (let [[times distances] (str/split-lines input)
        times (->> (rest (str/split times #" +"))
                   (apply str)
                   parse-long)
        distances (->> (rest (str/split distances #" +"))
                       (apply str)
                       parse-long)]
    (list [times distances])))

(defn result [hold [length _]]
  (* (- length hold) hold))

(defn breaks-record [hold [_ record :as race]]
  (> (result hold race) record))

(defn winning-options [[length _ :as race]]
  (filter #(breaks-record % race) (range length)))

(defn part1 [input]
  (->> (parse input)
       (map winning-options)
       (map count)
       (reduce *)))

(defn part2 [input]
  (->> (parse2 input)
       (map winning-options)
       (map count)
       first))

