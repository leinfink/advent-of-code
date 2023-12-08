(comment
  (def input (slurp "inputs/input8.txt"))
  (def input (slurp "inputs/example8.txt"))
  )

(ns aoc23.day08
  (:require [clojure.string :as str]))

(defn parse [input]
  (let [[instructions _ & nodes] (str/split-lines input)]
    {:instructions instructions
     :nodes
     (->> (map #(str/split % #" = ") nodes)
          (map
           (fn [[node connections]]
             [node (str/split
                    (subs connections 1 (dec (count connections)))
                    #", ")]))
          (into {}))}))

(defn follow-instructions [pos instructions nodes walks]
  (if (= pos "ZZZ")
    walks
    (recur (if (= (first instructions) \L)
             (nth (get nodes pos) 0)
             (nth (get nodes pos) 1))
           (rest instructions)
           nodes
           (conj walks pos))))

(defn part1 [input]
  (let [{:keys [instructions nodes]} (parse input)]
    (count (follow-instructions "AAA" (cycle instructions) nodes []))))

(part1 input)
(apply hash-map {} [[2 3] [4 5]])
