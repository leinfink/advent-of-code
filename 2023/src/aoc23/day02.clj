(ns aoc22.day02
  (:require [clojure.string :as str]))

(defn parse [input]
  (->> (str/split-lines input)
       (map prepare-line)))

(defn prepare-line [line]
  (let [[id reveals] (str/split line #":")]
    {:id (parse-long (re-find #"\d+$" id))
     :reveals
     (for [rev (str/split reveals #";")
           :let [balls (str/split (str/trim rev) #",? ")
                 [counts colors] (apply map list (partition 2 balls))]]
           (zipmap colors (map parse-long counts)))}))

(defn possible-reveal? [config reveal]
  (every? (fn [[color count]] (>= (get config color 0) count)) reveal))
       
(defn possible-config? [config reveals]
  (every? #(possible-reveal? config %) reveals))

(defn solve [input config]
  (->> (str/split-lines input)
       (map prepare-line)
       (filter #(possible-config? config (:reveals %)))
       (map :id)
       (reduce +)))
