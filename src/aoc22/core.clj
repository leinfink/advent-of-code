(ns aoc22.core)

(defmacro for->
  "Threads the expr through nested list comprehensions, using the forms as collection-exprs.
  Inserts the last form in the body of the innermost list comprehension. For instance:

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
