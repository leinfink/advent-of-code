(ns aoc22.day4
  (:require
   [aoc22.util :refer [for->]]
   [clojure.string :as str]))

(defn read-pairs [input]
  (for-> input
         str/split-lines
         (#(str/split % #","))
         (#(str/split % #"-"))
         Integer/parseInt))

(defn containments [[[a1, a2], [b1, b2]]]
  (or (<= a1 b1 b2 a2)
      (<= b1 a1 a2 b2)))

(defn overlap [[[a1, a2], [b1, b2]]]
  (or (<= a1 b1 a2)
      (<= b1 a1 b2)))

(defn- count-pred [pred coll]
  (count (filter pred coll)))

(defn part1 [input]
  (count-pred containments (read-pairs input)))

(defn part2 [input]
  (count-pred overlap (read-pairs input)))
