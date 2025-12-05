(* SML '97 *)

(* Tested on Poly/ML and MLTon. *)

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
fun num_of_digits (n:L.int) : int = trunc (Math.log10 (Real.fromLargeInt n)) + 1

fun is_invalid_part1 (n:L.int) : bool =
    let val ndigits = num_of_digits n
        val n = Real.fromLargeInt n
    in
        even ndigits
        andalso
        let val pow10 = Math.pow (10.0, Real.fromInt (ndigits div 2))
            val quot = trunc (n / pow10) and rem = trunc (Real.rem (n, pow10))
        in quot = rem
        end
    end

fun digit_at_pos (digs: int list) (p:int) : int = List.nth (digs, p)

fun digits (n:L.int) : int list =
    let val chars = explode (L.toString n)
    in map (fn c => valOf (Int.fromString (Char.toString c))) chars
    end

fun is_invalid_part2 (n:L.int) : bool =
    let val digs = digits n and ndigits = num_of_digits n
        val n = Real.fromLargeInt n
        fun repeated pos cur =
            cur >= ndigits
            orelse
            (digit_at_pos digs cur = (digit_at_pos digs (cur mod (pos+1)))
             andalso repeated pos (cur+1))
        fun recurse pos =
            pos < (ndigits-1)
            andalso
            ((ndigits mod (pos+1) = 0 andalso repeated pos 0)
             orelse
             recurse (pos + 1))
    in
        recurse 0        
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
val part2 = sum_invalids is_invalid_part2 ranges
fun main () = (* main() for polyc *)
    let val _ = print ((L.toString part1) ^ "\n")
        val _ = print((L.toString part2) ^ "\n")
    in ()
    end
val _ = main() (* for MLTon *)
