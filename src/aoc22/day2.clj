(ns aoc22.day2
  (:require [clojure.string :as str]))

;; easily extensible, infinite lazy-seq of the shapes in ascending order
(defn ranking [] (cycle [:rock :paper :scissors]))

(def scores-outcome {:win 6, :draw 3, :loss 0})
(def scores-shape {:rock 1, :paper 2, :scissors 3})

(defn- first-after [pred coll]
  (when-let [s (seq coll)]
    (if (pred (first s))
      (fnext s)
      (recur pred (next s)))))

(defn- last-before [pred coll]
  (loop [prev nil, coll coll]
    (when-let [s (seq coll)]
      ;; if prev is nil, we are at the start and should wait for the next cycle
      (if (and (pred (first s)) (some? prev))
       prev
       (recur (first s) (next s))))))

(defn superior
  "Return the shape that wins against `x`."
  [x]
  (first-after #{x} (ranking)))

(defn inferior
  "Return the shape that loses against `x`."
  [x]
  (last-before #{x} (ranking)))

(defn beats? [x y] (= x (superior y)))

(defn outcome [[x, y]]
  (cond
    (beats? x y) :win
    (beats? y x) :loss
    :else :draw))

(defn score [[enemy, me]]
  (+ (scores-shape me)
     (scores-outcome (outcome [me, enemy]))))

(defn sum-scores [strategy]
  (reduce + (map score strategy)))

(defn read-strategy [input read-fn]
  (for [round (str/split-lines input)]
    (read-fn (str/split round #" "))))

(defn solve [input read-fn]
  (sum-scores (read-strategy input read-fn)))

(def read-enemy {"A" :rock, "B" :paper, "C" :scissors})

(defn part1-reader [[x, y]]
  (let [read-me {"X" :rock, "Y" :paper, "Z" :scissors}]
    [(read-enemy x), (read-me y)]))

(defn part2-reader [[x, y]]
  (let [read-me {"X" inferior, "Y" identity, "Z" superior}
        enemy (read-enemy x)]
     [enemy, ((read-me y) enemy)]))

(defn part1 [input] (solve input part1-reader))
(defn part2 [input] (solve input part2-reader))
