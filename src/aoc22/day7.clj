(ns aoc22.day7
  (:require [clojure.string :as str]))

(defn parse [s]
  (->> (str/split s #"\$ ")
       (map #(str/split % #"\s"))
       rest))

(defn build-tree [[tree pos] [cmd & res]]
  (case cmd
    "cd" (case (first res)
           ".." [tree (pop pos)]
           "/" [tree [:/]]
           [tree (conj pos (keyword (first res)))])
    "ls" [(assoc-in tree pos (apply hash-map res)), pos]))

(defn count-dirs [[pos dir] & chosen]
  (let [{subdirs true, files false} (group-by #(keyword? (key %)) dir)
        subcounts (map count-dirs subdirs)
        chosen (into chosen (mapcat second subcounts))
        size (+ (reduce + (map first subcounts))
                (reduce + (map (fn [[f _]]
                                 (if (= f "dir") 0 (parse-long f))) files)))]
    [size, (if (< size 100000) (conj chosen size) chosen)]))

(defn solve [s]
  (let [[dirs _]  (reduce build-tree [{:/ {}}, [:/]] (parse s))
        [_ chosen-dirs]  (count-dirs [:/ dirs])]
    (reduce + chosen-dirs)))
