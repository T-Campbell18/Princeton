  (*******************************************************)
  (* Parallel Sequences Based on an Array Representation *)
  (*******************************************************)
  module Parallel.S
    type 'a t = Rep of 'a array

    let fail() = failwith "unimplemented"

    let from_array (a:'a array) : 'a t = Rep (Array.copy a)

    let to_array (Rep a) = Array.copy a

    let length (Rep a) = a.Length

    let chunklength (n:int) (i:int) (cs:int) : int =
      if (n - i) < cs then (n-i) else cs

    let chunklength2 (n:int) (i:int) (cs:int): int =
      if (n - i) < cs then (n-1) else (i + cs - 1)

    let tabulate (f:int -> 'a) (n:int) : 'a t =
      let cores = System.Environment.ProcessorCount in
      let cs = n / cores in
        if (n < (cores*cores)) then
          Rep([|for i in 0 .. (n - 1) -> f i |])
        else
          Rep([|for i in 0 .. cs .. (n - 1) ->
                async{ return [| for j in i .. (chunklength2 n i cs) -> f j|]}|]
          |> Async.Parallel
          |> Async.RunSynchronously
          |> Array.concat)

    let nth (Rep a) i = a.[i]

    let map (f:'a -> 'b) (s:'a t) : 'b t = tabulate (fun i -> f (nth s i)) (length s)

    let iter f (Rep a) = Array.iter f a

    let reduce (f:'a -> 'a -> 'a) (b:'a) (s:'a t) : 'a =
      let s' = to_array s in
      let n = length s in
      let cores = System.Environment.ProcessorCount in
      let cs = n / cores in
      if (n < (cores*cores)) then
        Array.fold f b s'
      else
        [|for i in 0 .. cs .. (n-1) ->
            async{ return if (chunklength n i cs) = 1 then (s'.[i]) else Array.fold f (s'.[i]) (Array.sub s' (i+1) ((chunklength n i cs)-1))}|]
        |> Async.Parallel
        |> Async.RunSynchronously
        |> Array.fold f b
