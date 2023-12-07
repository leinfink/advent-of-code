(comment
  (def input (slurp "inputs/input7.txt"))
  (def input (slurp "inputs/example7.txt"))
  )

(ns aoc23.day07
  (:require [clojure.string :as str]))

(defn parse [input]
  (->> (str/split-lines input)
       (map #(str/split % #" +"))
       (map (fn [x] {:hand (x 0) :bid (parse-long (x 1))}))))

(def cards "AKQJT98765432")
(def types [:five :four :full-house :three :two-pair :one-pair :high-card])

(defn compare-type [type-a type-b]
  (compare (.indexOf types type-a) (.indexOf types type-b)))

(defn compare-highest-cards [a b]
  (let [comparison (compare (str/index-of cards (first a))
                            (str/index-of cards (first b)))]
    (if (= 0 comparison)
      (compare-highest-cards (rest a) (rest b))
      comparison)))

(defn hand-type [hand]
  (let [freqs (frequencies (vals (frequencies hand)))]
    (cond
      (freqs 5) :five
      (freqs 4) :four
      (and (freqs 3) (freqs 2)) :full-house
      (freqs 3) :three
      (= 2 (freqs 2)) :two-pair
      (freqs 2) :one-pair
      :else :high-card)))

(defn compare-hands [a b]
  (let [type-comparison (compare-type (hand-type a) (hand-type b))]
    (if (= 0 type-comparison)
      (compare-highest-cards a b)
      type-comparison)))

(defn part1 [input]
  (->> (parse input)
       (sort #(compare-hands (:hand %1) (:hand %2)))
       (map-indexed (fn [rank item] (* (inc rank) (:bid item))))
       (reduce +)))

(part1 input)
