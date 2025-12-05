(* OCaml 5.4.0 *)
type range = (int * int)

(* Parse. *)

let rec parse_ranges (ch: Scanf.Scanning.in_channel) (acc:range list) : range list =
  match Scanf.bscanf_opt ch "%u-%u\n" Pair.make with
  | Some(range) -> parse_ranges ch (range :: acc)
  | None -> acc

let rec parse_ids (ch: Scanf.Scanning.in_channel) (acc:int list) : int list =
  match Scanf.bscanf_opt ch "%u\n" Fun.id with
  | Some(id) -> parse_ids ch (id :: acc)
  | None -> acc

let parse (file:string) : range list * int list =
  let ch = Scanf.Scanning.open_in file in
  let ranges = parse_ranges ch [] in
  Scanf.bscanf ch "\n" ();
  let ids = parse_ids ch [] in
  Scanf.Scanning.close_in ch;
  (ranges, ids)

(* Solve. *)

let fresh (id:int) (ranges:range list) : bool =
  let in_bounds (low, high) = low <= id && id <= high in
  List.exists in_bounds ranges

let count_fresh_ids (ranges: range list) (ids:int list) : int =
  let add1_if_fresh n id = n + if fresh id ranges then 1 else 0 in
  List.fold_left add1_if_fresh 0 ids

(* Execute. *)
    
let (ranges, ids) = parse "input5.txt"
let part1 = count_fresh_ids ranges ids
