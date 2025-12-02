;; Scheme R7RS-small

(import (scheme base)
        (scheme file)
        (scheme write))

(define MAX 100)
(define START 50)

;; Parse.

(define (parse-line line sig)
  ((if (char=? sig #\L) - +) 0 (string->number line)))

(define (parse-rotations input-path)
  (with-input-from-file input-path
    (lambda ()
      (let loop ((rotations '()))
        (let ((sig (read-char)))
          (if (eof-object? sig)
              (reverse rotations)
              (loop (cons (parse-line (read-line) sig) rotations))))))))

;; Solve.

(define (move-dial cur delta)
  (let ((val (+ cur (floor-remainder delta MAX))))
    (if (< val 0)
        (+ MAX val)
        (floor-remainder val MAX))))

(define (do-rotations-with-fn rotations fn)
  (let loop ((cur START)
             (rotations rotations)
             (counts 0))
    (if (null? rotations)
        counts
        (loop (move-dial cur (car rotations))
              (cdr rotations)
              (+ counts (fn cur (car rotations)))))))

;; Execute!

(define part1
  (do-rotations-with-fn (parse-rotations "input1.txt")
                        (lambda (cur _) (if (zero? cur) 1 0))))

(define part2
  (do-rotations-with-fn (parse-rotations "input1.txt")
                        (lambda (cur delta)
                          (abs (floor-quotient (+ cur delta) MAX)))))

(display part1) (newline)
(display part2) (newline)
