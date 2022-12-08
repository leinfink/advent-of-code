(ns aoc22.day5
  (:require
   [aoc22.util :refer [for->]]
   [clojure.string :as str]))

(defn parse-rows [s]
  (for [line (str/split-lines (str/trimr (first (str/split s #"1"))))]
    (flatten (partition 1 4 (rest line)))))

(defn parse-stacks [s]
  (reduce (fn [prev-row, row]
            (map #(if (#{\space} %2) %1 (conj %1 %2))
                 prev-row row))
          (repeat '())
          (reverse (parse-rows s))))

(defn parse-moves [s]
  (for [line (str/split-lines s)]
    (map #(Integer/parseInt %)
         (-> line
             (str/replace #"move " "")
             (str/replace #" from " " ")
             (str/replace #" to " " ")
             (str/split #" ")))))

(defn move-fn [stacks [cnt, from, to]]
  (let [from (dec from), to (dec to)]
       (loop [i cnt, stacks stacks]
         (let [peek-itm (peek (nth stacks from))]
           (if (zero? i)
             stacks
             (recur (dec i)
                    (map-indexed (fn [idx itm] (condp = idx
                                                 from (pop itm)
                                                 to (conj itm peek-itm)
                                                 itm))
                                 stacks)))))))


(defn move-fn-2 [stacks [cnt, from, to :as moves]]
  (prn stacks)
  (let [from (dec from), to (dec to)]
    (let [peek-itm (take cnt (nth stacks from))]
      (map-indexed (fn [idx itm]
                     (condp = idx
                       from (nthnext itm cnt)
                       to (concat peek-itm itm)
                       itm))
                   stacks))))


(defn make-moves [combined starting-stacks moves]
  (reduce (if combined
            move-fn-2
            move-fn)
          starting-stacks
          moves))

(defn read-input [s]
  (let [input (str/split s #"\n\n")]
    [(parse-stacks (first input))
     (parse-moves (second input))]))

(defn part1 [input]
  (->> (for [stack (apply make-moves false (read-input input))]
         (peek stack))
       (apply str)))

(defn part2 [input]
  (->> (for [stack (apply make-moves true (read-input input))]
         (first stack))
       (apply str)))
