(* SML '97 *)

(* Tested on Poly/ML, MLTon, and SML/NJ. *)

structure L = LargeInt

(* Parse. *)

fun split (delim:char) (s:string) : string list =
    String.tokens (fn c => c = delim) s

fun parse (s:string) : (L.int * L.int) list =
    let exception InputMalformed
        fun parse_num s =
            case L.fromString s of
                SOME n => n
              | NONE => raise InputMalformed
        fun parse_range s =
            case split #"-" s of
                from :: to :: [] => (parse_num from, parse_num to)
              | _ => raise InputMalformed
    in
        map parse_range (split #"," s)
    end

fun parse_input_file (path:string) : (L.int * L.int) list =
    let val file = TextIO.openIn path
        val input = TextIO.inputAll file
        val _ = TextIO.closeIn file
    in parse input
    end

        
(* Solve. *)

fun even (n:int) : bool = n mod 2 = 0
fun num_of_digits (n:real) : int = trunc (Math.log10 n) + 1

fun is_invalid_part1 (n:L.int) : bool =
    let val n = Real.fromLargeInt n
        val ndigits = num_of_digits n
    in
        even ndigits
        andalso
        let val pow10 = Math.pow (10.0, Real.fromInt (ndigits div 2))
            val quot = trunc (n / pow10)
            val rem = trunc (Real.rem (n, pow10))
        in quot = rem
        end
    end
        
fun check_range (check: (L.int -> bool)) (from: L.int, to: L.int) : L.int =
    let fun recurse count n =
            if n > to then count
            else recurse (count + (if check n then n else 0)) (n + 1)
    in recurse 0 from
    end

fun sum_invalids (check:(L.int -> bool)) (ranges:(L.int * L.int) list) : L.int =
    let val sum = foldl (op +) 0
    in sum (map (check_range check) ranges)
    end

        
(* Execute! *)
        
val ranges = parse_input_file "input2.txt"
val part1 = sum_invalids is_invalid_part1 ranges
fun main () = print ((L.toString part1) ^ "\n") (* main() for polyc *)
val _ = main() (* for MLTon *)
