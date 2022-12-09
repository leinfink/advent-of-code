(ns aoc22.day5
  (:require
   [aoc22.util :refer [for-> str-replace->]]
   [clojure.string :as str]))

(defn parse-rows [s]
  (for [line (str/split-lines (str/trimr (first (str/split s #"1"))))]
    (flatten (partition 1 4 (rest line)))))

(defn parse-stacks [s]
  (reduce (fn [prev-row, row]
            (map #(if (#{\space} %2) %1 (conj %1 %2)) prev-row row))
          (repeat '()) ; Use lists, since we operate with stacks.
          (reverse (parse-rows s)))) ; But starting stacks need to be flipped.

(defn parse-moves [s]
  (for [line (str/split-lines s)]
    (map #(Integer/parseInt %)
         (-> (str-replace-> line, #"move " "", #" from " " ", #" to " " ")
             (str/split #" ")))))

(defn move-combined [stacks [cnt, from, to]]
  (let [peek-itm (take cnt (nth stacks from))]
    (map-indexed (fn [idx itm] (condp = idx
                                 from (nthnext itm cnt)
                                 to (concat peek-itm itm)
                                 itm))
                 stacks)))

(defn move-single [stacks [cnt, from, to]]
  (loop [i cnt, stacks stacks]
    (if (zero? i)
      stacks
      (recur (dec i) (move-combined stacks [1, from, to])))))

(defn make-moves [starting-stacks moves mv-fn]
  (reduce (fn [stk [cnt, from, to]] (mv-fn stk [cnt, (dec from) (dec to)]))
          starting-stacks
          moves))

(defn solve [s & {:keys [move-fn]}]
  (let [[stk, mvs] (str/split s #"\n\n")]
    (->> (make-moves (parse-stacks stk) (parse-moves mvs) move-fn)
         (map first)
         (apply str))))

(defn part1 [input] (solve input :move-fn move-single))

(defn part2 [input] (solve input :move-fn move-combined))
