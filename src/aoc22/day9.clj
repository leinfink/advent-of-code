(ns aoc22.day9
  (:require [clojure.math :as math]))

(defn parse [s]
  (for [[_ dir len] (re-seq #"(\w) (\d)" s)]
    [(keyword dir) (parse-long len)]))

(def dirs {:R [1 0], :L [-1 0], :U [0 -1], :D [0 1]})

(defn catch-up [h t]
  (->> (mapv - h t)
       ((fn [[x y]]
               (let [dy (+ y (* -1 (int (math/signum y))))
                     dx (+ x (* -1 (int (math/signum x))))]
               [(if (zero? y) dx x)
                (if (zero? x) dy y)])))
       (mapv + t)))

(defn move [[h t] [dir len]]
  (let [h (mapv + (map #(* len %) (dirs dir)) h)
        t (catch-up h t)]
    (prn [h t])
    [h t]))

(defn solve [s]
  (reduce move [[0 0] [0 0]] (parse s)))

(defn part1 [s] (solve s))

(defn part2 [s] (solve s))
