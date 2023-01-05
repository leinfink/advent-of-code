(require :asdf)

(declaim (optimize (speed 3) (safety 0)))

(declaim (inline put-sorted))
(defun put-sorted (itm coll)
  (let ((lowest (car coll)))
    (declare (type fixnum itm lowest))
    (if (> itm lowest)
        (cdr (coerce (sort (coerce (cons itm coll) '(vector fixnum)) #'<)
                     'list))
        coll)))

(declaim (inline update-cur-cals))
(defun update-cur-cals (cur-cals line)
  (declare (type string line))
  (if (uiop:emptyp line)
      0
      (let ((added (parse-integer line)))
        (declare (type fixnum cur-cals added))
        (the fixnum
             (+ cur-cals added)))))

(declaim (inline update-max-cals))
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
