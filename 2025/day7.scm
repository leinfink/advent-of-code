;; Scheme R7RS-large / with SRFIs

;; Tested on [...]

(import (scheme base))

;; Parse.

(define (parse-lines path fn)
  (with-input-from-file path
    (lambda ()
      (let iter ((lines '()))
        (let ((line (read-line)))
          (if (eof-object? line)
              lines
              (iter (cons (fn line) lines))))))))

(define (parse path)
  (reverse! (parse-lines path string->vector)))

;; Solve.

(define (vector-indexes pred? vec)
  (let ((len (vector-length vec)))
    (let loop ((i 0) (acc '()))
      (cond ((= i len) acc)
            ((pred? (vector-ref vec i)) (loop (+ i 1) (cons i acc)))
            (else (loop (+ i 1) acc))))))

(define (beams line)
  (vector-indexes (lambda (c) (or (char=? c #\S) (char=? c #\|))) line))

(define (splitters line)
  (vector-indexes (lambda (c) (char=? c #\^)) line))

(define (update-next-line cur next)
  (let ((splitters (splitters next))
        (splits 0))
    (for-each (lambda (i)
                (if (memv i splitters)
                    (begin (vector-set! next (- i 1) #\|)
                           (vector-set! next (+ i 1) #\|)
                           (set! splits (+ splits 1)))
                    (vector-set! next i #\|)))
              (beams cur))
    splits))

(define (count-splits lines)
  (let loop ((count 0) (lines lines))
    (if (null? lines)
        count
        (loop (+ count (length (beams (car lines)))) (cdr lines)))))

;; Execute.
(define (execute)
  (let loop ((lines (parse "2025/dummy.txt"))
             (splits-count 0))
    (display (car lines))
    (display "  ")
    (display splits-count)
    (newline)
    (if (null? (cdr lines))
        splits-count
        (loop (cdr lines) (+ splits-count (update-next-line (car lines) (cadr lines)))))))

(execute)
