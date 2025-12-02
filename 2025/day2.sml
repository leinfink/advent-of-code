(* SML '97 *)

fun even n = n mod 2 = 0
fun num_of_digits n = trunc (Math.log10 n) + 1
fun is_invalid_part1 n =
    let val n = Real.fromLargeInt n
        val ndigits = num_of_digits n
    in
        if even ndigits
        then let val pow10 = Math.pow (10.0, Real.fromInt (ndigits div 2))
                 val quot = trunc (n / pow10)
                 val rem = trunc (Real.rem (n, pow10))
             in quot = rem
             end
        else false
    end
        
fun check_range check_fn (from, to) =
    let fun recurse count n =
            if n > to
            then count
            else recurse (count + (if check_fn n then n else 0)) (n + 1)
    in recurse 0 from
    end

fun sum_invalids_in_ranges check_fn ranges =
    let val sum = foldl (op +) 0
    in sum (map (check_range check_fn) ranges)
    end

fun parse_string s =
    let fun eq_char a b = a = b
        val els = String.tokens (eq_char #",") s
        fun parse_range el =
            let val range = String.tokens (eq_char #"-") el
                fun parse s = valOf (LargeInt.fromString s)
            in case range of
                from :: to :: [] => (parse from, parse to)
                | _ => raise Fail "Input malformed."
            end
    in map parse_range els
    end

fun parse_input_file path =
    let val file = TextIO.openIn path
        val input = TextIO.inputAll file
        val _ = TextIO.closeIn file
    in parse_string input
    end
        
val ranges = parse_input_file "input2.txt"
val part1 = sum_invalids_in_ranges is_invalid_part1 ranges
fun main () = print ((LargeInt.toString part1) ^ "\n")
val _ = main()
