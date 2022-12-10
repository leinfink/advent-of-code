(ns aoc22.day10)

(defn parse [s]
  (for [[_ cmd val] (re-seq #"(\w+)(?: (\-?\d+))?" s)]
    [(keyword cmd) (parse-long (or val ""))]))

(def cmd-times {:noop 1, :addx 2})
(def cmd-func {:noop (fn [x _] x), :addx +})

(defn run [[cycle state] [cmd val]]
  [(+ cycle (cmd cmd-times)), ((cmd cmd-func) state val)])

(defn exec [s]
  (apply hash-map (flatten (reductions run [0 1] (parse s)))))

(defn signal [vals n]
    (* n (some vals (range (dec n) 0 -1))))

(defn part1 [s]
  (let [res (exec s)]
    (reduce + (map (partial signal res) [20 60 100 140 180 220]))))
