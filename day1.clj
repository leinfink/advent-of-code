(require '[clojure.string :as str])

(defn total-calories [calories-list]
  (reduce + calories-list))

(defn elf-with-most-calories [elves-with-calories]
  (reduce (fn [a-total, b]
            (let [b-total (total-calories b)]
              (if (> a-total b-total)
                a-total
                b-total)))
          0
          elves-with-calories))

(defn elves-with-calories-from-input [input]
  (map (fn [elve-calories] (map #(Integer/parseInt %)
                                (str/split-lines elve-calories)))
       (str/split input #"\n\n")))
