(require '[clojure.string :as str])

(defn read-rucksacks-from-input [input]
  (map #(split-at (/ (count %) 2) %)
       (str/split-lines input)))

(defn find-duplicate [[comp1, comp2]]
  (first (clojure.set/intersection (set comp1) (set comp2))))

(defn get-priority [item]
  (- (int item)
     (if (Character/isUpperCase item)
       (- 64 26)
       96)))

(defn sum-priorities-of-duplicates [rucksacks]
  (reduce + (map #(get-priority (find-duplicate %)) rucksacks)))
