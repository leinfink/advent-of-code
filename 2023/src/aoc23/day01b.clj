(ns aoc23.day01
  (:require [clojure.string :as str]))

(defn parse [input]
  (str/split-lines
   (str/replace input #"one|two|three|four|five|six|seven|eight|nine"
                {"one" "1"
                 "two" "2"
                 "three" "3"
                 "four" "4" 
                 "five" "5"
                 "six" "6"
                 "seven" "7"
                 "eight" "8"
                 "nine" "9"})))

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
