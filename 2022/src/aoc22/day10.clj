(ns aoc22.day10
  (:require [clojure.string :as str]))

(defn parse [s]
  (for [[_ cmd val] (re-seq #"(\w+)(?: (\-?\d+))?" s)]
    [(keyword cmd) (parse-long (or val ""))]))

(defn run [[cycl state] [cmd val]]
  [(+ cycl (cmd {:noop 1, :addx 2})), ; cycle-number
   ((cmd {:noop (fn [x _] x), :addx +}) state val)]) ; updated state

(defn exec [s] (into {} (reductions run [0 1] (parse s))))

(defn get-reg [regs cycl] (some regs (range (dec cycl) -1 -1)))

(defn part1 [s]
  (reduce + (map #(* % (get-reg (exec s) %)) [20 60 100 140 180 220])))

(defn part2 [s]
  (let [regs (exec s), width 40]
    (->> (for [row (range 0 6), px (range 0 width)
               :let [cycl (inc (+ px (* row width)))
                     dist (abs (- (get-reg regs cycl) px))]]
           (if (< dist 2) "#" "."))
         (partition width)
         (map str/join)
         (str/join "\n"))))
