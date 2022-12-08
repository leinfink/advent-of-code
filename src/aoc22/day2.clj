(ns aoc22.day2
  (:require
   [aoc22.util :refer [after-first before-first]]
   [clojure.string :as str]))

;; Infinite lazy-seq of the shapes in ascending order.
(defn ranking [] (cycle [:rock :paper :scissors]))

(defn superior [x] (after-first #{x} (ranking)))

(defn inferior [x]
  (or (before-first #{x} (ranking))
      (before-first #{x} (rest (ranking))))) ; Get the first shape's inferior.

(defn outcome [[x, y]]
  (condp = x
    (superior y) :win
    (inferior y) :loss
    :draw))

(defn score [[enemy, me]]
  (+ (me {:rock 1, :paper 2, :scissors 3})
     ((outcome [me, enemy]) {:win 6, :draw 3, :loss 0})))

(defn read-strategy [input read-fn]
  (for [round (str/split-lines input)]
    (read-fn (str/split round #" "))))

(defn solve [input read-fn]
  (reduce + (map score (read-strategy input read-fn))))

(def read-enemy {"A" :rock, "B" :paper, "C" :scissors})

(defn part1 [input]
  (let [read-me {"X" :rock, "Y" :paper, "Z" :scissors}]
    (solve input (fn [[x, y]] [(read-enemy x), (read-me y)]))))

(defn part2 [input]
  (let [read-me-fn {"X" inferior, "Y" identity, "Z" superior}]
    (solve input (fn [[x, y]]
                   (let [enemy (read-enemy x)]
                     [enemy, ((read-me-fn y) enemy)])))))
