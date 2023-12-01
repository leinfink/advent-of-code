(ns aoc23.day01
  (:require [clojure.string :as str]))

(defn parse [input]
  (str/split-lines input))

(defn get-digits [line]
  (remove nil? (map #(parse-long (str %)) line)))

(defn find-first-n-digits [n line]
  (take n (get-digits line)))

(defn find-last-n-digits [n line]
  (take-last n (get-digits line)))

(defn result-of-line [line]
  (->> (concat (find-first-n-digits 1 line)
               (find-last-n-digits 1 line))
       str/join
       parse-long))

  (defn result-of-results [line-results]
  (reduce + line-results))

(defn solve [input]
  (->> (parse input)
       (map #(result-of-line %))
       result-of-results))
