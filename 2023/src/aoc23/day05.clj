(comment
  (def input (slurp "inputs/input5.txt"))
  (def input (slurp "inputs/example5.txt"))
  )

(ns aoc23.day05
  (:require [clojure.string :as str]))

(defn parse-map [m] 
  (->> (str/split-lines m)
       (map #(str/split % #" +"))
       (map #(map parse-long %))))

(defn parse [input]
  (let [inputs (str/split input #"\n\n.*map:\n")]
    {:seeds (map parse-long
                 (second
                  (map #(str/split % #" +") (str/split (inputs 0) #": +"))))
     :seed-to-soil (parse-map (inputs 1))
     :soil-to-fertilizer (parse-map (inputs 2))
     :fertilizer-to-water (parse-map (inputs 3))
     :water-to-light (parse-map (inputs 4))
     :light-to-temperature (parse-map (inputs 5))
     :temperature-to-humidity (parse-map (inputs 6))
     :humidity-to-location (parse-map (inputs 7))}))


(defn map-fn [m source]
  (or (some (fn [[dest-start source-start length]]
              (when (<= source-start source (+ (dec length) source-start))
                (+ dest-start (- source source-start))))
            m)
      source))

(defn seed-to-loc [seed maps]
  (->> (map-fn (:seed-to-soil maps) seed)
       (map-fn (:soil-to-fertilizer maps))
       (map-fn (:fertilizer-to-water maps))
       (map-fn (:water-to-light maps))
       (map-fn (:light-to-temperature maps))
       (map-fn (:temperature-to-humidity maps))
       (map-fn (:humidity-to-location maps))
       ))

(defn part1 [input]
  (let [input (parse input)]
    (first (sort (map #(seed-to-loc % input) (:seeds input))))))
