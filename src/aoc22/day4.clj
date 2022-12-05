(require '[clojure.string :as str])

(defn read-pairs-from-input [input]
  (map
   (fn [pair]
     (map
      (fn [range]
        (map #(Integer/parseInt %) (str/split range #"-")))
      (str/split pair #",")))
   (str/split-lines input)))

(defn get-complete-containments [pairs]
  (filter
   (fn [[a, b]]
     (or
      (and (<= (first a) (first b))
           (>= (second a) (second b)))
      (and (<= (first b) (first a))
           (>= (second b) (second a)))))
   pairs))

(defn count-complete-containments [pairs]
  (count (get-complete-containments pairs)))

(defn get-some-overlap [pairs]
  (filter
   (fn [[a, b]]
     (or (<= (first a) (first b) (second a))
         (<= (first a) (second b) (second a))
         (<= (first b) (first a) (second b))
         (<= (first b) (second a) (second b))))
   pairs))

(defn count-some-overlap [pairs]
  (count (get-some-overlap pairs)))
