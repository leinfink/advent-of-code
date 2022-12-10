(ns aoc22.day10)

(defn parse [s]
  (for [[_ cmd val] (re-seq #"(\w+)(?: (\-?\d+))?" s)]
    [(keyword cmd) (parse-long (or val ""))]))

(def cmd-times {:noop 0, :addx 1})
(def cmd-func {:noop (fn [x _] x), :addx +})

(defn exec [state [cmd val]]
  ((cmd cmd-func) state val))

(defn run [[state future] [cmd val]]
  (let [cmd-time (cmd cmd-times)
        [n1, n2 :as new-f] (split-at (count future)
                                     (conj (vec (repeat cmd-time [:noop nil]))
                                           [cmd val]))
        future (concat (mapv conj future n1) (map vector n2))
        state (reduce exec state (first future))]
    [state, (rest future)]))

(defn solve [s]
  (let [history (reductions run [1 []] (parse s))
        [state, future] (last history)]
    (concat history (reductions #(reduce exec %1 %2) state future))))
