(ns aoc22.day21
  (:require [clojure.string :as str]))

(defn parse [s]
  (->> (str/split-lines s)
       (map #(rest (re-find #"(\w{4}): (.*)" %)))
       (map #(vector
              (first %)
              (re-find #"(?:^(\d*))$|(?:^(\w{4}) ([\*\+\-\/\=]) (\w{4})$)"
                       (second %))))
       (into {})
       ((fn [m] (update-vals m #(hash-map
                                 :num (when-let [n (nth % 1)] (parse-long n))
                                 :op (when-let [o (nth % 3)]
                                       (resolve (read-string o)))
                                 :op1 (nth % 2)
                                 :op2 (nth % 4)))))))
(defn calc [m monkey]
  (let [line (m monkey)]
    (or (:num line)
        ((:op line) (calc m (:op1 line)) (calc m (:op2 line))))))

(defn find-as-operand [m monkey]
  (filter #(some #{monkey} (map (val %) [:op1 :op2])) m))

(defn find-path [m start end path]
  (if (= start end) path
      (let [new (first(find-as-operand m start))]
        (recur m (key new) end (conj path new)))))

(defn inverse-op [op]
  (condp = op
    (var *) /
    (var /) *
    (var +) -
    (var -) +))

(defn calc-check-human [m path monkey]
  (prn (:op (path monkey)))
  (cond
    (= "humn" monkey) 0
    (not (path monkey)) (calc m monkey)
    (= (var =) (:op (path monkey)))
    )#_ (if (= "humn" monkey) 0
      (if-not (path monkey)
        (calc m monkey)
        {:op (:op (path monkey))
         :op1 (calc-check-human m path (:op1 (path monkey)))
         :op2 (calc-check-human m path (:op2 (path monkey)))})))

(defn calc-human [calculation]
  (prn (:op calculation))
  ((:op calculation) (:op1 calculation) (:op2 calculation)))

(defn part1 [s]
  (calc (parse s) "root"))

(defn part2 [s]
  (let [m (parse (str/replace s #"(root: .* )(\+)" "$1="))]
    #_(calc-human) (calc-check-human m (find-path m "humn" "root" {}) "root")))


(comment
  (defn input [](parse "root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32
")))
