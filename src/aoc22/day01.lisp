(require :asdf)

(defun split-at (p seq &optional (f #'identity))
  (reduce (lambda (acc val)
            (if (funcall p val)
                (cons () acc)
                (cons (cons (funcall f val) (car acc))
                      (cdr acc))))
          seq
          :initial-value ()))

(defun parse (input)
  (split-at #'uiop:emptyp
            (uiop:split-string input :separator '(#\Newline))
            #'parse-integer))

(defun highest (n coll)
  (reduce (lambda (acc val)
            (if (> val (car acc))
                (cdr (sort (cons val acc) #'<))
                acc))
          coll
          :initial-value (make-list n :initial-element 0)))

(defun solve (n elves)
  (highest n (mapcar (lambda (x) (apply #'+ x)) elves)))

(defun part1 (input)
  (car (solve 1 (parse input))))

(defun part2 (input)
  (apply #'+ (solve 3 (parse input))))
