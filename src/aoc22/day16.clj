(ns aoc22.day16
  (:require [clojure.math.combinatorics :as combo]))

(defn parse [s]
  (->> (re-seq #"Valve ([A-Z]{2}).*rate=(\d+);.*valve[s]? (.*)" s)
       (map (fn [[_ v r t]]
              [(keyword v),
               (hash-map :rate (parse-long r)
                         :tunnels (map keyword (re-seq #"[A-Z]{2}" t))
                         :open false)]))
       (#(zipmap (map first %) (map fnext %)))))

(defn shortest-path
  ([vs start end] (shortest-path vs start end []))
  ([vs start end path]
   (cond
     (= start end) []
     (some #{end} (:tunnels (start vs))) (conj path end)
     :else (->> (remove (set path) (:tunnels (start vs)))
                (map #(shortest-path vs % end (conj path %)))
                (filter some?)
                (sort-by count)
                first))))

(defn distance [vs start end] (count (shortest-path vs start end)))

(defn candidates [vs] (remove #(zero? (:rate (val %))) vs))

(defn choices [vs pos]
  (->> (candidates vs)
       (map #(shortest-path vs pos (key %)))
       (map first)
       distinct))

(defn search [vs strats]
  (map #(search vs (choices vs (last %))) strats))

(defn create-strat [vs mins pos]
  ()
  (let [vs (assoc-in vs [pos :open] true)]
    ,,,))




(defn current-flow [vs]
  (reduce + 0 (map :rate (filter :open (vals vs)))))

(defn added-pressure [valve time] (* time (:rate valve)))


(defn added-pressure-diff [vs valve time pos]
  (added-pressure (valve vs)
                  (- time (count (shortest-path vs pos valve)))))

(defn calc-path [vs path]
  (first (reduce (fn [[flow, vs] {:keys [switch]}]
                   [(+ flow (current-flow vs))
                    (if switch (assoc-in vs [switch :open] true)
                        vs)])
                 [0, vs]
                 path)))



(defn combis [xs]
  (if (> (count xs) 1)
    (->> xs
         (mapcat (fn [itm]
                   (mapcat #(conj % itm)
                           (combis (remove #{itm} xs)))))
        (partition (count xs)))
    [xs]))

(defn dev-path [vs order start]
  (reduce (fn [path target]
            (conj (into path
                       (map  #(hash-map :move %)
                             (shortest-path vs
                                            (or (or (:move (last path))
                                                    (:switch (last path)))
                                                start)
                                            target)))
                  {:switch target}))
          [] order))

(defn possible-paths [vs minutes start]
  (let [candidates (keys
                    (sort-by
                     #(:rate (val %))
                     <
                     (remove #(zero? (:rate (val %))) vs)))
        paths (map #(dev-path vs (reverse %) start) (take 100 (combo/permutations candidates)))]
    (map #(take minutes (concat % (repeat {})) ) paths)))

(defn highest [vs minutes start]
  (apply max (map #(calc-path vs %) (possible-paths vs minutes start))))
