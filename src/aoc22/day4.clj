(ns aoc22.day4
  (:require [clojure.string :as str]))

(defmacro for->
  [x & forms]
  (if forms
      (if (seq (rest forms))
        (let [form (first forms), new-x (gensym)]
          `(for [~new-x
                 ~(if (seq? form)
                    (with-meta `(~(first form) ~x ~@(next form)) (meta form))
                    (list form x))]
             (for-> ~new-x ~@(next forms))))
        (list (first forms) x))
    x))

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
