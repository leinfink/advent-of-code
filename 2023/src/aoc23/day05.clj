(ns aoc23.day05
  (:require [clojure.string :as str]))

(defn parse-mapping [s] 
  (->> (str/split-lines s)
       (map #(str/split % #" +"))
       (map #(map parse-long %))))

(defn parse-seeds [s]
  (->> (str/split s #": +")
       (map #(str/split % #" +") )
       second
       (map parse-long)))

(defn parse [input]
  (let [[seeds & mappings] (str/split input #"\n\n.*map:\n")]
    {:seeds (parse-seeds seeds)
     :mappings (map parse-mapping mappings)}))

(defn mapping-helper-fn  [source [dest-start source-start length]]
  (when (<= source-start source (+ (dec length) source-start))
    (+ dest-start (- source source-start))))

(defn mapping-fn [source mapping]
  (or (some #(mapping-helper-fn source %) mapping)
      source))

(defn mapping-fn-backwards [dest mapping]
  (or (some #(mapping-helper-fn dest [(nth % 1) (nth % 0) (nth % 2)])
            mapping)
      dest))

(defn seed-to-loc [seed mappings]
  (reduce mapping-fn seed mappings))

(defn loc-to-seed [loc mappings]
  (reduce mapping-fn-backwards loc (reverse mappings)))

(defn is-in-seeds [n seeds]
  (let [pairs (partition 2 seeds)]
    (some (fn [[start length]] (<= start n (+ start (dec length))))
          pairs)))

(defn part1 [input]
  (let [{:keys [seeds mappings]} (parse input)]
    (first (sort (map #(seed-to-loc % mappings) seeds)))))

(defn part2 [input]
  (let [{:keys [seeds mappings]} (parse input)]
    (some #(when (is-in-seeds (loc-to-seed % mappings) seeds) %)
          (range))))
