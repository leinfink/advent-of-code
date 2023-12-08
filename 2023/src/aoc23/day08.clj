(comment
  (def input (slurp "inputs/input8.txt"))
  (def input (slurp "inputs/example8.txt"))
  )

(ns aoc23.day08
  (:require [clojure.string :as str]
            [clojure.math.numeric-tower :as math]))

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

(defn follow-instructions [pos instructions nodes walks check-fn] 
  (if (check-fn pos)
    walks
    (recur (if (= (first instructions) \L)
             (nth (get nodes pos) 0)
             (nth (get nodes pos) 1))
           (rest instructions)
           nodes
           (conj walks pos)
           check-fn)))

(defn follow-instructions-2 [pos instructions nodes walks steps check-fn]
  (if (check-fn pos)
    (if (contains? walks pos) [steps walks]
        (recur  (if (= (first instructions) \L)
                  (nth (get nodes pos) 0)
                  (nth (get nodes pos) 1))
                (rest instructions)
                nodes
                (assoc walks pos steps)
                1
                check-fn))
    (recur  (if (= (first instructions) \L)
             (nth (get nodes pos) 0)
             (nth (get nodes pos) 1))
            (rest instructions)
           nodes
           walks
           (inc steps)
           check-fn)))

(contains? (assoc {} "hi" 0) "hi")

(defn part1 [input]
  (let [{:keys [instructions nodes]} (parse input)]
    (count (follow-instructions "AAA" (cycle instructions) nodes []
                                #(= % "ZZZ")))))

(defn find-commons [vals]
  (let [found (apply clojure.set/intersection (map set (map #(nth % 2) vals)))]
    (if (empty? found)
      (recur (map (fn [[start repeat coll]]
                    [start repeat (conj coll (+ (first coll) repeat))]) vals))
      found)))

(defn part2 [input]
  (let [{:keys [instructions nodes]} (parse input)]
    (let [vals (->>
                (map #(follow-instructions-2 %
                                             (cycle instructions) nodes {} 0
                                             (fn [x] (str/ends-with? x "Z")))
                     (filter #(str/ends-with? % "A") (keys nodes)))
                (map (fn [[start repeats]] [start
                                            (first (vals repeats))
                                            (list start)]))
                (sort-by #(second %) >)
                (map first))]
      (reduce (fn [v i] (math/lcm v i)) vals))))
     #_(first (find-commons vals)))))


