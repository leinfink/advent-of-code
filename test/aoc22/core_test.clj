(ns aoc22.core-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest testing is]]))

(defn- read-file [day type]
  (slurp (str "inputs/aoc22/day" day "." (name type))))

(defn- parse-answer [input]
  (str/split-lines input))

(defn read-puzzle
  "Returns a hashmap with the puzzle input and answers for `day`.
  Input is read without modification. Answers are split into a list."
  [day]
  (into {} (for [[type modifier] {:input identity, :answer parse-answer}]
             [type (modifier (read-file day type))])))

(defn- sym-concat [s & xs]
  (symbol (str/join (conj xs s))))

(defmacro test-part [ns puzzle part]
  `(testing ~(str "Part " part)
     (is (= (nth (:answer ~puzzle) (dec ~part))
            (str (~(sym-concat ns "/part" part)
                  (:input ~puzzle)))))))

(defmacro def-daytest
  "Writes a test for both parts of the puzzle for `day`.
  The tested functions have to be called `part1` and `part2`
  and be within a `day`-specific namespace."
  [day]
  (let [ns (str "aoc22.day" day)]
    `(do
       (require '~(symbol ns))
       (deftest ~(sym-concat ns "-test")
         (let [puzzle# (read-puzzle ~day)]
           (test-part ~ns puzzle# 1)
           (test-part ~ns puzzle# 2))))))

(defmacro generate-daytests
  ([max-day] `(generate-daytests 1 ~max-day))
  ([min-day max-day]
  `(do
     ~@(for [day (range min-day (inc max-day))]
         `(def-daytest ~day)))))

(generate-daytests 5)
(generate-daytests 9 9)
