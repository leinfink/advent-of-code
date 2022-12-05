(require '[clojure.string :as str])

(defn read-strategy [input]
  (map #(str/split % #" ")
       (str/split-lines input)))

(defn round-score [[enemy, myself]]
  (let [enemy-points ({"A" 1, "B" 2, "C" 3} enemy)
        my-points  ({"X" 1, "Y" 2, "Z" 3} myself)]
    (cond
      (= enemy-points my-points) (+ 3 my-points)
      (= my-points (case enemy-points
                     1 2
                     2 3
                     3 1)) (+ 6 my-points)
      :else my-points)))

(defn get-scores [strategy]
  (reduce (fn [score next-round]
            (+ score (round-score next-round)))
          0
          strategy))

(defn apply-part2-strategy [strategy]
  (map (fn [[enemy, myself]]
         (conj [enemy]
               (case myself
                 "X" (case enemy
                       "A" "Z"
                       "B" "X"
                       "C" "Y")
                 "Y" (case enemy
                       "A" "X"
                       "B" "Y"
                       "C" "Z")
                 "Z" (case enemy
                       "A" "Y"
                       "B" "Z"
                       "C" "X"))))
         strategy))
