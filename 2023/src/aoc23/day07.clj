(ns aoc23.day07
  (:require [clojure.string :as str]))

(defn parse [input]
  (->> (str/split-lines input)
       (map #(str/split % #" +"))
       (map (fn [x] {:hand (x 0) :bid (parse-long (x 1))}))))

(def ^:dynamic cards (reverse "AKQJT98765432"))

(def types (reverse [:five :four :full-house :three :two-pair :one-pair :high-card]))

(defn compare-type [type-a type-b]
  (compare (.indexOf types type-a) (.indexOf types type-b)))

(defn compare-highest-cards [a b cards]
  (let [comparison (compare (str/index-of cards (first a))
                            (str/index-of cards (first b)))]
    (if (= 0 comparison)
      (compare-highest-cards (rest a) (rest b) cards)
      comparison)))

(defn calc-hand-type [freqs]
  (cond
      (freqs 5) :five
      (freqs 4) :four
      (and (freqs 3) (freqs 2)) :full-house
      (freqs 3) :three
      (= 2 (freqs 2)) :two-pair
      (freqs 2) :one-pair
      :else :high-card))

(defn ^:dynamic hand-type [hand]
  (calc-hand-type (frequencies (vals (frequencies hand)))))

(defn compare-hands [a b]
  (let [type-comparison (compare-type (hand-type a) (hand-type b))]
    (if (= 0 type-comparison)
      (compare-highest-cards a b cards)
      type-comparison)))

(defn part1 [input]
  (->> (parse input)
       (sort #(compare-hands (:hand %1) (:hand %2)))
       (map-indexed (fn [rank item] (* (inc rank) (:bid item))))
       (reduce +)))

(def cards2 (reverse "AKQT98765432J"))

(defn hand-type2 [hand]
  (let [joker-count (or ((frequencies hand) \J) 0)
        v (sort > (vals (frequencies (str/replace hand "J" ""))))
        joker-update (conj (rest v) (+ joker-count (or (first v) 0)))]
    (calc-hand-type (frequencies joker-update))))

(defn part2 [input]
  (binding [hand-type hand-type2
            cards cards2]
    (part1 input)))
