(ns aoc22.day08
  (:require [clojure.string :as str]))

(defn parse [s]
  (mapv #(mapv parse-long (re-seq #"\d" %)) (str/split-lines s)))

(defn height [grid [x, y]] ((grid x) y))

(defn sightlines [grid [x, y]]
  [(reverse (take y (grid x)))
   (drop (inc y) (grid x))
   (reverse (map #(nth % y) (take x grid)))
   (map #(nth % y) (drop (inc x) grid))])

(defn visible? [grid me]
  (some (fn [line] (every? #(< % (height grid me)) line))
        (sightlines grid me)))

(defn scenic-score [grid me]
  (->> (sightlines grid me)
       (map (fn [line]
              (let [[vis nvis] (split-with #(< % (height grid me)) line)]
                (+ (count vis) (if (seq nvis) 1 0)))))
       (reduce *)))

(defn treemap [mapfn grid]
  (for [x (range (count grid)), y (range (count (first grid)))]
    (mapfn grid [x, y])))

(defn part1 [s]
  (count (filter some? (treemap visible? (parse s)))))

(defn part2 [s]
  (first (sort > (treemap scenic-score (parse s)))))
