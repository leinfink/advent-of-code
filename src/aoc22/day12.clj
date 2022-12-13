(ns aoc22.day12
  (:require
   [clojure.set :as set]
   [clojure.string :as str]))

(defn parse [s]
  (let [chargrid (mapv vec (str/split-lines s))]
    [chargrid, (mapv #(mapv int (replace {\S \a, \E \z} %)) chargrid)]))

(defn height [grid pos] (or (get-in grid pos) 123)) ; (= 123 (inc (int \z)))

(defn possible-moves [grid pos]
  (->> [[1 0] [-1 0] [0 1] [0 -1]]
       (filter #(>= 1 (- (height grid (mapv + pos %)) (height grid pos))))
       (map #(mapv + pos %))))

(defn find-in [grid char]
  (->>  (for [x (range (count grid)), y (range (count (grid x)))] [x y])
        (filter #(= char (get-in grid %)))))

(defn get-distance [[chargrid, grid] start-chr end-chr]
  (let [[starts, goals] (map #(set(find-in chargrid %)) [start-chr end-chr])]
    (loop [checking starts, checked #{}, steps 0]
      (if (seq (set/intersection goals checking))
        steps
        (recur (set/difference (set (mapcat #(possible-moves grid %) checking))
                               checked)
               (set/union checked checking)
               (inc steps))))))

(defn part1 [s] (get-distance (parse s) \S \E))

(defn part2 [s] (get-distance (parse s) \a \E))
