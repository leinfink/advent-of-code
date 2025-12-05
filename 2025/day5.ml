(* OCaml 5.4.0 *)

type range = (int * int)
type range_for_merge = {low: int; mutable high: int; mutable active: bool}

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

let prepare_ranges (ranges: range list) : range_for_merge array =
  let transform (l, h) = {low = l; high = h; active = true} in
  let ranges = ranges |> List.map transform |> Array.of_list in
  let highest = ref 0 and highest_index = ref 0 in
  Array.fast_sort compare ranges;
  for i = 0 to Array.length ranges - 1 do
    let cur = ranges.(i) in
    if cur.low <= !highest then
      (ranges.(i).active <- false;
       if cur.high > !highest then
         (highest := cur.high;
          ranges.(!highest_index).high <- cur.high))
    else
      (highest := cur.high;
       highest_index := i)
  done;
  ranges

let count_ranges (ranges: range_for_merge array) : int  =
  let count n {high; low; active} = n + if active then high - low + 1 else 0 in
  Array.fold_left count 0 ranges

(* Execute. *)
    
let (ranges, ids) = parse "input5.txt"
let part1 = count_fresh_ids ranges ids
let part2  = prepare_ranges ranges |> count_ranges
  
let _ =
  Printf.printf "Part 1: %d\n" part1;
  Printf.printf "Part 2: %d\n" part2
