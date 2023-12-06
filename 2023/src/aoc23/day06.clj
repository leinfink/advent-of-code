(ns aoc23.day06
  (:require [clojure.string :as str]))

(defn parse-line-part1 [line]
  (map parse-long (rest (str/split line #" +"))))

(defn parse-part1 [input]
  (let [[times distances] (str/split-lines input)]
    (partition 2 (interleave (parse-line-part1 times)
                             (parse-line-part1 distances)))))

(defn parse-line-part2 [line]
  (parse-long (apply str (rest (str/split line #" +")))))

(defn parse-part2 [input]
  (let [[times distances] (str/split-lines input)]
    (list [(parse-line-part2 times)
           (parse-line-part2 distances)])))

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

