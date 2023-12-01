(define (split-lines str)
  (define (recur str cur-line lines)
    (let ((add-line (lambda () (cons (list->string (reverse cur-line))
				     lines))))
      (if (null? str)
	  (if (null? cur-line) lines (add-line))	      
	  (if (char=? (car str) #\newline)
	      (recur (cdr str) '() (add-line))
	      (recur (cdr str) (cons (car str) cur-line) lines)))))
  (recur (string->list str) '() '()))

(define (digits line)
  (define (recur digs line)
    (if (null? line)
	(reverse digs)
	(let ((dig (char->integer (car line))))
	  (if (> 58 dig 47)
	      (recur (cons (- dig 48) digs) (cdr line))
	      (recur digs (cdr line))))))
  (recur '() (string->list line)))

(define (compute-line line)
  (let ((digs (digits line)))
    (+ (* 10 (car digs)) (car (reverse digs)))))

(define (solve input)
  (apply + (map compute-line (split-lines input))))

(define (part1 input) (solve input))
