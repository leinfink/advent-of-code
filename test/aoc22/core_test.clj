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
  (into {}
        (for [[type modifier] {:input identity :answer parse-answer}]
          [type (modifier (read-file day type))])))

(defn- ns-concat [namespace]
  (fn [& xs] (symbol (apply str (conj xs namespace)))))

(defn- test-part [ns-day puzzle part]
  `(testing ~(str "Part " part)
     (is (= (nth (:answer ~puzzle) (dec ~part))
            (str (~(ns-day "/part" part)
                  (:input ~puzzle)))))))

(defmacro def-daytest
  "Writes a test for both parts of the puzzle for `day`.
  The tested functions have to be called `part1` and `part2`
  and be within a `day`-specific namespace."
  [day]
  (let [ns-day (ns-concat (str "aoc22.day" day))
        puzzle (gensym)]
    `(do (require '~(ns-day))
         (deftest ~(ns-day "-test")
           (let [~puzzle (read-puzzle ~day)]
             ~@(for [part [1 2]]
                 (test-part ns-day puzzle part)))))))

(defmacro generate-daytests [max-day]
  `(do
     ~@(for [day (range 1 (inc max-day))]
         `(def-daytest ~day))))

(generate-daytests 4)
