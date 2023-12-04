(ns aoc23.day04
  (:require [clojure.string :as str]))

(defn prepare-line [str]
  (let [[id numbers] (str/split str #": +")
        [winners mine] (str/split numbers #" \| +")]
    {:id (parse-long (re-find #"\d+$" id))
     :winners (map parse-long (str/split winners #" +"))
     :mine (map parse-long (str/split mine #" +"))}))

(defn search-line [line]
  (filter #(contains? (set (:winners line)) %) (:mine line)))

(defn calc-line [line]
  (int (reduce (fn [v _] (* v 2)) 1/2 (search-line line))))

(defn part1 [input]
  (->> (str/split-lines input)
       (map prepare-line)
       (map calc-line)
       (reduce +)))

(defn count-line [line]
  {:id (:id line)
   :count (count (search-line line))})

(defn add-cards [card cards]
  (apply conj cards
         (for [i (range (:count card))]
           (some #(when (= (:id %) (+ (:id card) (inc i))) %)
                 cards))))

(defn get-cards [coll cards]
  (if (empty? cards) coll
      (recur 
       (conj coll (first cards))
       (add-cards (first cards) (rest cards)))))

(defn part2 [input]
  (->> (str/split-lines input)
       (map prepare-line)
       (map count-line)
       (get-cards '())
       count))
