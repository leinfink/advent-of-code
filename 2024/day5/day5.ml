type page = int
type rule = page * page
type update = page list

let rec parse_rules ch acc =
  match Scanf.bscanf ch "%d|%d\n" (fun a b -> (a, b)) with
  | r -> parse_rules ch (r :: acc)
  | exception _ -> acc

let rec parse_updates ch acc =
  match Scanf.bscanf ch "\n%s" (fun s -> String.split_on_char ',' s) with
  | [""] -> parse_updates ch acc
  | u ->  parse_updates ch ((List.map int_of_string u) :: acc)
  | exception _ -> acc

let parse file : (rule list * update list) =
  let ch = Scanf.Scanning.open_in file in
  let rules = parse_rules ch [] in
  let updates = parse_updates ch [] in
  Scanf.Scanning.close_in ch;
  (rules, updates)

let relevant u (l, r) = List.mem l u && List.mem r u
let satisfied u (l, r) = (List.find_index ((=) l) u) < (List.find_index ((=) r) u)
let valid rs u = List.filter (relevant u) rs |> List.for_all (satisfied u)
let mid_page u = List.nth u ((List.length u) / 2)
let valid_mid_page rs u = if valid rs u then Some (mid_page u) else None

let part_1 (rs: rule list) (us: update list) : int =
  List.filter_map (valid_mid_page rs) us
  |> List.fold_left (+) 0
      
let rs, us = parse "input.txt"
let _ = part_1 rs us |> string_of_int |> print_endline

open Printf

let correct (rs: rule list) (u: update) =
  let relevant_rs = List.filter (relevant u) rs in
  let rec construct (order: page list) (rules: rule list) nums = match nums with
    | [] -> List.rev order
    | _ :: _ as nums ->
      let n = List.find (fun x -> (not (List.mem x (List.map (fun (_, r) -> r) rules)))) nums
      in construct (n :: order) (List.filter (fun (l, _) -> l <> n) rules)
        (List.filter (fun x -> not (x = n)) nums)
  in construct [] relevant_rs u

let part_2 rs us =
  List.filter_map (fun u -> if valid rs u then None else Some u) us
  |> List.map (correct rs)
  |> List.map mid_page
  |> List.fold_left (+) 0

let _ = part_2 rs us
    
