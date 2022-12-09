(ns aoc22.day9
  (:require [clojure.math :as math]))

(defn parse [s]
  (for [[_ dir len] (re-seq #"(\w) (\d+)" s)]
    [(keyword dir) (parse-long len)]))

(def dirs {:R [1 0], :L [-1 0], :U [0 -1], :D [0 1]})

(defn catch-up [h t]
  #_(prn h t (mapv - t h))
  (let [d (mapv - h t)
        dd (if (> (reduce + (map abs d)) 2)
             (map {-2 -1, -1 -1, 0 0, 1 1, 2 1} d)
             (map {-2 -1, -1 0, 0 0, 1 0, 2 1} d))
        res (mapv + dd t)]
    res))

(defn pprint [h t]
  (doseq [y (range -4 1)]
    (doseq [x (range 0 6)]
      (printf
       (condp = [x, y]
         h "H"
         t  "T"
         [0, 0] "s"
         ".")))
    (prn))
  (prn))

(defn move [[h ts] [dir len]]

  (if (zero? len)
    (do (prn h (peek ts))[h ts])
    (let [new-h (mapv + (dirs dir) h)
                                        ;_ (prn (dirs dir) new-h (peek ts))
          new-ts (conj ts (catch-up new-h (peek ts)))]
      #_(pprint new-h (peek new-ts))
      (recur [new-h, new-ts]
             [dir, (dec len)]))))

(defn solve [s]
  (pprint [0 0] [0 0])
  (let [[_ ts] (reduce #(do (print "---" %2 "---\n") (move %1 %2)) [[0 0] '([0 0])] (parse s))]
    (prn (peek ts))
    (count (distinct ts))))



(defn part1 [s] (solve s))

(defn part2 [s] (solve s))
