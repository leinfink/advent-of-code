(ns aoc22.day9
  (:require [clojure.math :as math]))

(defn parse [s]
  (for [[_ dir len] (re-seq #"(\w) (\d+)" s)]
    [(keyword dir) (parse-long len)]))

(def dirs {:R [1 0], :L [-1 0], :U [0 -1], :D [0 1]})

(defn catch-up [h t]
  (let [delta (mapv - h t)
        delta-norm (map (if (> (reduce + (map abs delta)) 2)
                      math/signum
                      #(- % (math/signum %)))
                    delta)]
    (mapv + t (map long delta-norm))))

(defn move [[h ts] [dir len]]
  (if (zero? len)
    [h ts]
    (let [new-h (mapv + (dirs dir) h)
          new-ts (conj ts (catch-up new-h (peek ts)))]
      (recur [new-h, new-ts] [dir, (dec len)]))))

(defn move2 [[h ks ts][dir len]]
  (if (zero? len)
    [h ks ts]
    (let [new-h (mapv + (dirs dir) h)
          new-ks (rest (reductions #(catch-up %1 %2) new-h ks))
          new-ts (conj ts (catch-up (last new-ks) (peek ts)))]
      (recur [new-h new-ks new-ts] [dir, (dec len)]))))

(defn solve1 [s]
  (let [[_ ts] (reduce move [[0 0] '([0 0])] (parse s))]
    (count (distinct ts))))

(defn solve2 [s]
  (let [[_ _ ts] (reduce move2 [[0 0] (repeat 8 [0 0]) '([0 0])] (parse s))]
    (count (distinct ts))))

(defn part1 [s] (solve1 s))

(defn part2 [s] (solve2 s))



(comment (defn pprint [h t]
          (doseq [y (range -4 1)]
            (doseq [x (range 0 6)]
              (printf
               (condp = [x, y]
                 h "H"
                 t  "T"
                 [0, 0] "s"
                 ".")))
            (prn))
          (prn)))
