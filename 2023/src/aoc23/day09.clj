(comment
  (def input (slurp "inputs/input9.txt"))
  (def input (slurp "inputs/example9.txt"))
  
  )

(ns aoc23.day09
  (:require [clojure.string :as str]))

(defn parse [input]
  (->> (str/split-lines input)
       (map #(str/split % #" "))
       (map #(map parse-long %))))

(defn analyse-recur [line cur-coll past-colls]
  (if (< (count line) 2)
    (if (= (set cur-coll) #{0})
      past-colls
      (recur cur-coll [] (conj past-colls cur-coll)))
    (recur (rest line)
           (conj cur-coll (- (second line) (first line)))
           past-colls)))

(defn analyse [line]
  (analyse-recur line [] (list (into [] line))))

(defn predict [colls]
  (reduce (fn [v i] (+ v (peek i))) 0 colls))

(defn part1 [input]
  (->> (parse input)
       (map analyse)
       (map predict)
       ))

(part1 input)


  


 
