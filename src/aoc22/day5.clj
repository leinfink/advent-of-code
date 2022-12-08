(ns aoc22.day5
  (:require
   [aoc22.util :refer [for->]]
   [clojure.string :as str]))

(defn parse-rows [input]
  (for [line (str/split-lines
              (-> input (str/split #"1") first str/trimr))]
    (flatten (partition 1 4 (rest line)))))

(defn parse-stacks [input]
  (-> (fn [prev-row, row]
        (map #(if-not (#{\space} %2) (conj %1 %2) %1)
             prev-row row))
      (reduce (repeat []) (parse-rows input))))

(defn parse-moves [input])

(defn read-input [input]
  (let [input (str/split input #"\n\n")]
    [(parse-stacks (first input)),
     (parse-moves (rest input))]))
