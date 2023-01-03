(ns aoc22.day20
  (:require [clojure.string :as str]))

(defn parse [s] (mapv parse-long (str/split-lines s)))

(defn mix [state num]
  (let [len (count state)
        n (.indexOf state num)
        new-n (mod (+ n (rem num (dec len))) (dec len))]
    (if (> n new-n)
      (into (conj (subvec state 0 new-n) num)
            (into (subvec state new-n n)
                  (subvec state (inc n))))
      (into (conj (into (subvec state 0 n)
                        (subvec state (inc n) (inc new-n)))
                  num)
            (subvec state (inc new-n))))))

(defn part1 [s]
  (let [arr (parse s)
        res (reduce mix arr arr) ]
    (reduce + (map #(get res %)
                   (map (fn [n] (rem (+ (rem n (count res))
                                        (.indexOf res 0))
                                     (count res)))
                        [1000 2000 3000])))))

(comment
  (parse "1
2
-3
3
-2
0
4
"))
