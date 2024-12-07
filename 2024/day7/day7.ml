type operator = Add | Multiply
type expr = Operation of operation | Int of int
and operation = {op : operator; lh : expr; rh: int} 

let calc = function | Add -> ( + ) | Multiply -> ( * )
let rec evaluate expr : int = match expr with
  | Operation {op; lh; rh} -> calc op (evaluate lh) rh
  | Int n -> n
                                                 
let fill_eq (eq: int list) (ops: operator list) : expr =
  List.fold_left2 (fun acc e o -> Operation {op = o; lh = acc; rh = e})
    (Int 0) eq (Add :: ops)

let prepend_option os = List.map (fun o -> o :: os) [Add; Multiply]
let rec possible_op_orders len = match len with
  | 0 -> [[]]
  | _ -> List.concat_map prepend_option (possible_op_orders (len-1))

let test_eq test_val eq ops = (test_val = evaluate (fill_eq eq ops))
let fixable_eq (test_val, eq) =
  let pops = possible_op_orders ((List.length eq) - 1) in 
  if List.exists (test_eq test_val eq) pops then Some test_val else None


(* Parsing *)

let read_lines file =
  let contents = In_channel.with_open_bin file In_channel.input_all in
  String.split_on_char '\n' contents

let del_empty = List.filter_map (function "" -> None | s -> Some s)
let parse_eq s = String.split_on_char ' ' s |> del_empty |> List.map int_of_string
let parse lines =
  del_empty lines
  |> List.map (String.split_on_char ':')
  |> List.map (fun l -> (int_of_string (List.hd l), parse_eq (List.nth l 1)))

let part1 =
  let eq_tests = parse (read_lines "input.txt") in
  List.filter_map fixable_eq eq_tests |> List.fold_left (+) 0
