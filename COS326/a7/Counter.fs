module Counter

open Sequence
let numwords (key:string) (w:string list) : int =
    let c = w |>  List.countBy id |> List.filter(fun (x,y) -> x = key) in
    match c with
    | [] -> 0
    | [(k,n)] ->  n
    | _ -> 0

let fileCount (key:string) (file:(string * string list)) : (string * int) =
    let (f,w) = file in
    (f, numwords key w)

let counter (key:string) (files : (string * string list) S.t) : (string * int) option =
  let map = files |> S.map (fileCount key) in
    let (f,num) = map |> S.reduce (fun (ax,ay) (x,y) -> if y > ay then (x,y) else (ax,ay)) ("",0)in
      if num = 0 then None else Some (f,num)
