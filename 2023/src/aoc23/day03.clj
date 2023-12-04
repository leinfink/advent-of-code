(ns aoc23.day03
  (:require [clojure.string :as str]))

;; https://stackoverflow.com/a/21192299
(defn re-seq-pos [pattern string] 
  (let [m (re-matcher pattern string)] 
    ((fn step [] 
      (when (. m find) 
        (cons {:start (. m start) :end (. m end) :group (. m group)} 
              (lazy-seq (step))))))))

(defn grid [input]
  (str/split-lines input))

(defn normalize [s i]
  (cond (< i 0) 0
        (>= i (count s)) (dec (count s))
        :else i))

(defn n-subs [s start end]
  (subs s (normalize s start) (normalize s end)))

(defn has-adjacent-symbol? [grid line start end re]
  (let [top (n-subs (get grid (dec line) ".") (dec start) (inc end))
        left (get (grid line) (dec start) ".")
        right (get (grid line) end ".")
        bottom (n-subs (get grid (inc line) ".") (dec start) (inc end))]
    (re-find re (str top left right bottom))))

(defn find-hits-in-line [grid line]
  (filter #(has-adjacent-symbol? grid line (:start %) (:end %) #"[^0-9.]")
          (re-seq-pos #"\d+" (grid line))))

(defn find-gear-nums-in-line [grid line]
  (filter #(has-adjacent-symbol? grid line (:start %) (:end %) #"[*]")
               (re-seq-pos #"\d+" (grid line))))

(defn find-all-hits [grid]
  (mapcat #(find-hits-in-line grid %) (range (count grid))))

(defn neighbor? [line1 pos line2 start2 end2]
  (cond (and (= line1 (inc line2)) (<= (dec start2) pos end2)) true
        (and (= line1 line2) (or (= end2 pos) (= pos (dec start2)))) true
        (and (= line1 (dec line2)) (<= (dec start2) pos end2)) true
        :else nil))

(defn multiply-if-two-neighbors [grid line pos]
  (let [neigbors (concat
                  (filter #(neighbor? line pos (dec line) (:start %) (:end %))
                          (find-gear-nums-in-line grid (dec line)))
                  (filter #(neighbor? line pos line (:start %) (:end %))
                          (find-gear-nums-in-line grid line))
                  (filter #(neighbor? line pos (dec line) (:start %) (:end %))
                          (find-gear-nums-in-line grid (inc line))))]
    (if (= 2 (count neigbors))
      (reduce * (map parse-long (map :group neigbors)))
      0)))

(defn find-gears-in-line [grid line]
  (re-seq-pos #"[*]" (grid line)))

(defn find-all-gears [grid]
  (map #(find-gears-in-line grid %) (range (count grid))))
        
(defn part1 [input]
  (->> (grid input)
       find-all-hits
       (map :group)
       (map parse-long)
       (reduce +)))

(defn part2 [input]
  (let [grid (grid input)]
    (->> (find-all-gears grid)
         (map-indexed
          (fn [i x]
            (map #(multiply-if-two-neighbors grid i (:start %)) x)))
         flatten
         (reduce +))))
