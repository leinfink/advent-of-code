module Position = struct
  type t = int * int

  let compare (x0, y0) (x1, y1) =
    match Stdlib.compare x0 x1 with
    | 0 -> Stdlib.compare y0 y1
    | c -> c
end
    
module PosSet = Set.Make(Position)

type dir = Up | Down | Left | Right

let turn_right = function
  | Up -> Right
  | Down -> Left
  | Left -> Up
  | Right -> Down

let next (x, y) = function
  | Up -> (x, y-1)
  | Down -> (x, y+1)
  | Left -> (x-1, y)
  | Right -> (x+1, y)

let out_of_bounds (x, y) (len_x, len_y) = x >= len_x || y >= len_y || x < 0 || y < 0

let rec step obsts max acc ((x, y) as pos) dir =
  let next = next pos dir in
  if out_of_bounds next max then acc else
  if PosSet.mem next obsts
  then step obsts max acc (x, y) (turn_right dir)
  else step obsts max (PosSet.add (x, y) acc) next dir

let read_lines file =
  let contents = In_channel.with_open_bin file In_channel.input_all in
  String.split_on_char '\n' contents

let parse_pos find y (x, char) =
  if char = find then Some (x, y) else None

let parse_line find y line =
  line |> String.to_seqi |> List.of_seq |>
  List.map (parse_pos find y) |> List.filter_map (fun x -> x)

let parse find = let lines = read_lines "input.txt" in
  List.mapi (parse_line find) lines |> List.flatten |> PosSet.of_list

let obstacles = parse '#'
let start = PosSet.max_elt (parse '^')

let visited = step obstacles (130, 130) PosSet.empty start Up
let _ = (PosSet.cardinal visited) + 1
