;;; Coalton 0.0.1

;;; Preamble.

(asdf:defsystem #:day6
  :class :package-inferred-system
  :depends-on (#:coalton))

(asdf:load-system :day6)

(defpackage #:day6
  (:use 
   #:coalton
   #:coalton-prelude)
  (:local-nicknames
   (#:file #:coalton-library/file)
   (#:list #:coalton-library/list)
   (#:string #:coalton-library/string)
   (#:vector #:coalton-library/vector)
   (#:cls #:coalton-library/classes)
   (#:iter #:coalton-library/iterator)))

(in-package #:day6)
(named-readtables:in-readtable coalton:coalton)

(coalton-toplevel
  ;;; Define Types.

  (define-type Operator
    Add
    Mult)

  (define-type-alias Operators (Vector Operator))
  (define-type-alias Operation (Integer -> Integer -> Integer))
  (define-type-alias Numbers (Vector Integer))

  ;;; Parse.
  
  (declare read-lines (Into :a file:Pathname => :a -> (List String)))
  (define (read-lines path) (unwrap (file:read-file-lines path)))

  (declare parse ((List String) -> (Tuple (List Numbers) Operators)))
  (define (parse lines)
    (Tuple (parse-numbers (list:init lines))
           (parse-line parse-operator (unwrap (list:last lines)))))

  (declare parse-line ((String -> (Optional :a)) -> String -> (Vector :a)))
  (define (parse-line fun line)
    (pipe (split #\Space line)
          iter:into-iter
          (iter:filter-map! fun)
          iter:collect!))

  (declare parse-operator (String -> (Optional Operator)))
  (define (parse-operator op)
    (match op
      ("+" (Some Add))
      ("*" (Some Mult))
      (_ None)))

  (declare parse-numbers ((List String) -> (List Numbers)))
  (define (parse-numbers lines)
    (map (parse-line string:parse-int) lines))

  ;;; Solve.
  
  (declare do-operation ((List Numbers) -> Operators -> UFix -> Integer))
  (define (do-operation num-list operators i)
    (match (vector:index i operators)
      ((Some (Add)) (calc num-list i + 0))
      ((Some (Mult)) (calc num-list i * 1))
      (_ 0)))
  
  (declare calc ((List Numbers) -> UFix -> Operation -> Integer -> Integer))
  (define (calc num-list i op init)
    (fold (fn (acc line)
            (op acc
                (match (vector:index i line)
                  ((Some i) i)
                  (None init))))
          init
          num-list))

  (declare calculate (Tuple (List Numbers) Operators -> Integer))
  (define (calculate (Tuple num-list operators))
    (let ((max (vector:length operators)))
      (rec % ((i 0) (sum 0))
        (if (< i max)
            (% (+ i 1) (+ sum (do-operation num-list operators i)))
            sum))))
  
  )

(coalton

 ;; Execute.

 (calculate (parse (read-lines "input6.txt")))
 
 )
