type page = int
type rule = page * page
type update = page list

let relevant u (l, r) = List.mem l u && List.mem r u
let satisfied u (l, r) = (List.find_index ((=) l) u) < (List.find_index ((=) r) u)
let valid rs u = List.filter (relevant u) rs |> List.for_all (satisfied u)
let mid_page u = List.nth u ((List.length u) / 2)
let valid_mid_page rs u = if valid rs u then Some (mid_page u) else None

let part_1 (rs: rule list) (us: update list) =
  List.filter_map (valid_mid_page rs) us
  |> List.fold_left (+) 0

let rec parse_rules ch acc =
  match Scanf.bscanf ch "%d|%d\n" (fun a b -> (a, b)) with
  | r -> parse_rules ch (r :: acc)
  | exception _ -> acc

let rec parse_updates ch acc =
  match Scanf.bscanf ch "\n%s" (fun s -> String.split_on_char ',' s) with
  | [""] -> parse_updates ch acc
  | u ->  parse_updates ch ((List.map int_of_string u) :: acc)
  | exception _ -> acc

let parse file =
  let ch = Scanf.Scanning.open_in file in
  let rules = parse_rules ch [] in
  let updates = parse_updates ch [] in
  Scanf.Scanning.close_in ch;
  (rules, updates)
      
let rs, us = parse "input.txt"

let _ = part_1 rs us
