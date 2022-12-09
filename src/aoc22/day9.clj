(ns aoc22.day9
  (:require [clojure.math :as math]))

(defn parse [s]
  (for [[_ dir len] (re-seq #"(\w) (\d+)" s)]
    [(keyword dir) (parse-long len)]))

(def dirs {:R [1 0], :L [-1 0], :U [0 -1], :D [0 1]})

(defn catch-up [h t]
  (let [delta (map - h t)]
    (->> (if (<= (reduce + (map abs delta)) 2)
                 (replace {1 0, -1 0} delta)
                 delta)
         (map #(long (math/signum %)))
         (mapv + t))))

(defn move [[head knots tail-hist :as state] [dir len]]
  (if (zero? len)
    state
    (let [head (mapv + (dirs dir) head)
          knots (rest (reductions catch-up head knots))
          tail-hist (conj tail-hist (last knots))]
      (recur [head knots tail-hist] [dir (dec len)]))))

(defn solve
  ([s]
   (solve s 1))
  ([s n]
   (let [[_ _ tail-hist] (reduce move
                                 [[0 0] (repeat n [0 0]) [[0 0]]]
                                 (parse s))]
     (count (distinct tail-hist)))))

(defn part1 [s] (solve s))

(defn part2 [s] (solve s 9))
