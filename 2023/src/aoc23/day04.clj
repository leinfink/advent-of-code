(ns aoc23.day04
  (:require [clojure.string :as str]))

(defn prepare-line [str]
  (let [[id numbers] (str/split str #": +")
        [winners mine] (str/split numbers #" \| +")]
    {:id (parse-long (re-find #"\d+$" id))
     :winners (map parse-long (str/split winners #" +"))
     :mine (map parse-long (str/split mine #" +"))}))

(defn calc-line [line]
  (->> (set (:mine line))
       (clojure.set/intersection (set (:winners line)))
;;       (filter #(contains? (set (:winners line)) %))
       (reduce (fn [v _] (* v 2)) 1/2)
       int))

(defn part1 [input]
  (->> (str/split-lines input)
       (map prepare-line)
       (map calc-line)
       (reduce +)))
