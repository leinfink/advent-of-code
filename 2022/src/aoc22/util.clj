(ns aoc22.util
  (:require [clojure.string :as str]))

(defn after-some
  "Returns the item directly after the first item in coll for which
  (pred item) returns logical true. If there is no such item, returns nil."
  [pred coll]
  (when-let [s (seq coll)]
    (if (pred (first s))
      (fnext s)
      (recur pred (next s)))))

(defn before-some
  "Returns the item directly before the first item in coll for which
  (pred item) returns logical true. If there is no such item, returns nil."
  [pred coll]
  (loop [prev nil, coll coll]
    (when-let [s (seq coll)]
      (if (pred (first s))
        prev
        (recur (first s) (next s))))))

(defmacro for->
  "Threads the expr through nested list comprehensions.
  Uses the forms as collection-exprs, with the binding of the previous
  collection-expr added as the second element (as in ->).
  Inserts the last form in the body of the innermost list comprehension.
  For instance:

  (for-> expr
         form1
         form2
         form3)

  expands to (with example gensyms):

  (for [G__0001 (form1 expr)]
    (for [G__0002 (form2 G__0001)]
      (form3 G__0002)))

  Unlike for, does not support multiple binding-form/collection-expr pairs."
  [x & forms]
  (if forms
    (if (seq (rest forms))
      (let [form (first forms), new-x (gensym)]
        `(for [~new-x
               ~(if (seq? form)
                  (with-meta `(~(first form) ~x ~@(next form)) (meta form))
                  (list form x))]
           (for-> ~new-x ~@(next forms))))
      (list (first forms) x))
    x))

(defmacro str-replace->
  "Threads s through subsequent clojure.string/replace calls"
  [s & res-pairs]
  `(-> ~s ~@(for [[re, repl] (partition 2 res-pairs)]
              `(str/replace ~re ~repl))))
