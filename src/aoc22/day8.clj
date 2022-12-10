(ns aoc22.day8
  (:require [clojure.string :as str]
            [clojure.math :as math]))

(defn parse [s]
  (mapv #(mapv parse-long (re-seq #"\d" %)) (str/split-lines s)))

(defn height [grid [x, y]] ((grid x) y))

(defn sightlines [grid [x, y]]
  [(reverse (take y (grid x)))
   (drop (inc y) (grid x))
   (reverse (map #(nth % y) (take x grid)))
   (map #(nth % y) (drop (inc x) grid))])

(defn visible? [grid [x, y :as me]]
  (some (fn [line] (every? #(< % (height grid me)) line))
        (sightlines grid me)))

(defn scenic-score [grid [x, y :as me]]
 (reduce *
          (map (fn [line]
                 (let [[sight, nonsight] (split-with
                                          #(< % (height grid me))
                                          line)]
                   (+ (if (seq nonsight) 1 0)
                       (count sight))))
                (sightlines grid me))))

(defn part1 [s]
  (let [grid (parse s)]
    (->> (for [x (range (count grid))
              y (range (count (first grid)))]
           (visible? grid [x, y]))
         (filter some?)
         count)))

(defn part2 [s]
  (let [grid (parse s)]
    (->> (for [x (range (count grid))
               y (range (count (first grid)))]
           (scenic-score grid [x, y]))
         (sort >)
         first)))
