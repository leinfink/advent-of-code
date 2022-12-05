(require '[clojure.string :as str])

(defn elves-with-calories-from-input [input]
  (map (fn [elve-calories] (map #(Integer/parseInt %)
                                (str/split-lines elve-calories)))
       (str/split input #"\n\n")))

(defn total-calories [calories-list]
  (reduce + calories-list))

(defn calories-of-top-nth-elves
  "Return the calories of the n elves with most calories."
  [n elves-calories]
  (reduce (fn [highest next]
            (let [next-total (total-calories next)]
              (take n (sort > (conj highest next-total)))))
          (repeat n 0)
          elves-calories))

(defn read-input [input]
  (->> (str/split input #"\n\n")
       (map str/split-lines)
       ((fn [x] (for [i x] (map #(Integer/parseInt %) i))))))

(defn sorted-calories-per-elf [input]
          ()

(defn calories-of-top-elf [elves-calories]
  (first (calories-of-top-nth-elves 1 elves-calories)))

(defn sum-calories-of-top-three-elves [elves-calories]
  (total-calories (calories-of-top-nth-elves 3 elves-calories)))
