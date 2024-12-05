type rule = int * int
type update = int list

let relevant u (l, r) = List.mem l u && List.mem r u
let satisfied u (l, r) = (List.find_index ((=) l) u) < (List.find_index ((=) r) u)
let valid rs u = List.filter (relevant u) rs |> List.for_all (satisfied u)
let mid_page u = List.nth u ((List.length u) / 2)
let valid_mid_page rs u = if valid rs u then Some (mid_page u) else None

let part_1 (rs: rule list) (us: update list) =
  List.filter_map (valid_mid_page rs) us
  |> List.fold_left (+) 0
