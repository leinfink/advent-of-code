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

;; deprecated:
;; (defn calories-of-top-elf [elves-calories]
;;   (reduce (fn [a-total, b]
;;             (let [b-total (total-calories b)]
;;               (if (> a-total b-total)
;;                 a-total
;;                 b-total)))
;;           0
;;           elves-calories))

(defn calories-of-top-elf [elves-calories]
  (first (calories-of-top-nth-elves 1 elves-calories)))

(defn sum-calories-of-top-three-elves [elves-calories]
  (total-calories (calories-of-top-nth-elves 3 elves-calories)))
