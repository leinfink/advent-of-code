(ns aoc22.day10
  (:require [clojure.string :as str]))

(defn parse [s]
  (for [[_ cmd val] (re-seq #"(\w+)(?: (\-?\d+))?" s)]
    [(keyword cmd) (parse-long (or val ""))]))

(def cmd-times {:noop 1, :addx 2})
(def cmd-func {:noop (fn [x _] x), :addx +})

(defn run [[cycl state] [cmd val]]
  [(+ cycl (cmd cmd-times)), ((cmd cmd-func) state val)])

(defn exec [s]
  (apply hash-map (flatten (reductions run [0 1] (parse s)))))

(defn register [regs cycl]
  (some regs (range (dec cycl) -1 -1)))

(defn part1 [s]
  (reduce + (map #(* % (register (exec s) %)) [20 60 100 140 180 220])))

(defn part2 [s]
  (let [regs (exec s)]
    (->> (for [row (range 0 6)]
           (->> (for [px (range 0 40)
                      :let [cycl (inc (+ px (* row 40)))
                            dist (abs (- (register regs cycl) px))]]
                  (if (< dist 2) "#" "."))
                str/join))
         (str/join "\n"))))
