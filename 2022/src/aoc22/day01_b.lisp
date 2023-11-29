(require :asdf)

(defun put-sorted (itm coll)
  (if (> itm (car coll))
      (cdr (sort (cons itm coll) #'<))
      coll))

(defun update-cur-cals (cur-cals line)
  (if (uiop:emptyp line)
      0
      (+ cur-cals (parse-integer line))))

(defun update-max-cals (max-cals cur-cals line)
  (if (uiop:emptyp line)
      (put-sorted cur-cals max-cals)
      max-cals))

(defun top-cals (input-file)
  (with-open-file (stream input-file)
    (do ((line (read-line stream nil) (read-line stream nil))
         (cur-cals 0 (update-cur-cals cur-cals line))
         (max-cals (list 0 0 0) (update-max-cals max-cals cur-cals line)))
        ((null line) max-cals))))

(defun main ()
  (let* ((input-file (car (uiop:command-line-arguments)))
         (tops (top-cals input-file)))
    (format t "Part1: ~d~%" (car (last tops)))
    (format t "Part2: ~d~%" (reduce #'+ tops))))
