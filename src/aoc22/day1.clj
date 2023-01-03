(ns aoc22.day1
  (:require [clojure.string :as str])
  (:import java.util.concurrent.PriorityBlockingQueue))

(defn parse [input]
  (->> (str/split-lines input)
       (map parse-long)
       (partition-by nil?)
       (take-nth 2)))

 (defn hsort [s]
   (let [heap (PriorityBlockingQueue. s)]
     (repeatedly (count s) #(- (.take heap)))))

(defn sorted-calories [elves]
  (hsort (map #(int (- (reduce + %))) elves))) ; negate for natural ordering

(defn part1 [input]
  (first (sorted-calories (parse input))))

(defn part2 [input]
  (reduce + (take 3 (sorted-calories (parse input)))))
