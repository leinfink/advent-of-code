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


(defn map-fn
  ([backwards m dest]
   (or (some (fn [[dest-start source-start length]]
               (when (<= dest-start dest (+ (dec length) dest-start))
                 (+ source-start (- dest dest-start))))
             m)
       dest))
  ([m source]
   (or (some (fn [[dest-start source-start length]]
               (when (<= source-start source (+ (dec length) source-start))
                 (+ dest-start (- source source-start))))
             m)
       source)))

(defn seed-to-loc [seed maps]
  (->> (map-fn (:seed-to-soil maps) seed)
       (map-fn (:soil-to-fertilizer maps))
       (map-fn (:fertilizer-to-water maps))
       (map-fn (:water-to-light maps))
       (map-fn (:light-to-temperature maps))
       (map-fn (:temperature-to-humidity maps))
       (map-fn (:humidity-to-location maps))
       ))

(defn loc-to-seed [loc maps]
  (->>
   (map-fn true (:humidity-to-location maps) loc)
   (map-fn true (:temperature-to-humidity maps))
   (map-fn true (:light-to-temperature maps))
   (map-fn true (:water-to-light maps))
   (map-fn true  (:fertilizer-to-water maps))
   (map-fn true (:soil-to-fertilizer maps))
   (map-fn true(:seed-to-soil maps)))

  )
  
(defn part1 [input]
  (let [input (parse input)]
    (first (sort (map #(seed-to-loc % input) (:seeds input))))))

(defn possible-locations [loc-map]
  (flatten (map (fn [[dest source r]]
                     (for [i (range dest (+ dest r))]
                       i)) loc-map)))

(defn is-in-seeds [n seeds]
  (let [pairs (partition 2 seeds)]
    (some #(when (<= (first %) n (+ (first %) (dec (second %))))
             (first %)) pairs)))

#_(apply max
         (map
          #(+ (first %) (dec (second %)))
          (partition 2 (:seeds (parse input)))))

;; 3905942342

(defn part2 [input]
  (let [input (parse input)]
    (some (fn [x]
            (when (is-in-seeds (loc-to-seed x input) (:seeds input))
                   x))
          (range))))

(loc-to-seed 1 (parse input))

;; (part2 input)

;; (loc-to-seed 0 (parse input))
;; (is-in-seeds 1693708620 (:seeds (parse input)))

