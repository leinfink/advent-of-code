(ns aoc22.day12
  (:require [clojure.string :as str]))

(defn parse [s] (mapv vec (str/split-lines s)))

(defn grid-val [grid [x, y]] (get (get grid x) y))

(defn height [grid pos]
  (first (replace {83 97, 69 122} [(int (or (grid-val grid pos) 123))])))

(defn possible-moves [grid pos]
  (->> [[1 0] [-1 0] [0 1] [0 -1]]
       (filter #(>= 1 (- (height grid (mapv + pos %)) (height grid pos))))
       (map #(mapv + pos %))))

(defn find-in [grid char]
  (->> (for [x (range (count grid)), y (range (count (grid x)))]
         [x y])
       (filter #(= char (grid-val grid %)))
       set))

(defn get-distance [grid start-char goal-char]
  (let [starts (find-in grid start-char), goals (find-in grid goal-char)]
    (loop [choices starts, checked starts, counter 0]
      (if (some #(contains? goals %) choices)
        counter
        (recur (remove checked
                       (set (mapcat #(possible-moves grid %) choices)))
               (into checked choices)
               (inc counter))))))

(defn part1 [s] (get-distance (parse s) \S \E))

(defn part2 [s] (get-distance (parse s) \a \E))
