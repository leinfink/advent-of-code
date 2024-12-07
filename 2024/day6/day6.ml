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
  | Up -> Right | Down -> Left | Left -> Up | Right -> Down

let next (x, y) = function
  | Up -> (x, y-1) | Down -> (x, y+1) | Left -> (x-1, y) | Right -> (x+1, y)

let out_of_bounds (x, y) (len_x, len_y) =
  x >= len_x || y >= len_y || x < 0 || y < 0

let rec step obsts max acc ((x, y) as pos) dir =
  let next = next pos dir in
  if out_of_bounds next max then (PosSet.add pos acc) else
  if PosSet.mem next obsts
  then step obsts max acc (x, y) (turn_right dir)
  else step obsts max (PosSet.add pos acc) next dir

let max = (* (10, 10) *) (130, 130)

let read_lines file =
  let contents = In_channel.with_open_bin file In_channel.input_all in
  String.split_on_char '\n' contents

let parse_pos find y (x, char) = if char = find then Some (x, y) else None

let parse_line find y line =
  line |> String.to_seqi |> List.of_seq |>
  List.map (parse_pos find y) |> List.filter_map (fun x -> x)

let parse find = let lines = read_lines "input.txt" in
  List.mapi (parse_line find) lines |> List.flatten |> PosSet.of_list

let obstacles = parse '#'
let start = PosSet.max_elt (parse '^')

let visited = step obstacles max PosSet.empty start Up

module PositionWithDirection = struct
  type t = int * int * dir
  let order dir = match dir with Up -> 1 | Down -> 2 | Left -> 3 | Right -> 4
 
  let compare (x0, y0, dir0) (x1, y1, dir1) =
    match Position.compare (x0, y0) (x1, y1) with
    | 0 -> Stdlib.compare (order dir0) (order dir1)
    | c -> c
end

module PosDirSet = Set.Make(PositionWithDirection)

let rec step2 obsts max (acc : PosDirSet.t) ((x, y) as pos) dir =
  let next = next pos dir in
  if PosDirSet.mem (x, y, dir) acc then Some true else
  if out_of_bounds next max then None else
  if PosSet.mem next obsts
  then step2 obsts max acc (x, y) (turn_right dir)
  else step2 obsts max (PosDirSet.add (x, y, dir) acc) next dir

let try_loops visited =
  PosSet.to_list visited |>
  List.filter_map (fun (x, y) ->
      let obsts = PosSet.add (x, y) obstacles in
      step2 obsts max PosDirSet.empty start Up)
  |> List.length

let _ = PosSet.cardinal visited |> string_of_int |> print_endline
let _ = try_loops visited |> string_of_int |> print_endline
