(*** COS 326 Problem Set 2 ***)
(*** Tyler Campbell ***)
(*** tylercc ***)

(* Box office analysis *)

(* Contents:
    -- the movie type
    -- the studio_gross type
    -- functions for querying and transforming lists of movies
*)

(* a movie is a tuple of (title, studio, gross in millions, year) *)
type movie = string * string * float * int

(* a studio_gross is a pair of (studio, gross in millions) *)
type studio_gross = string * float

(* call bad_argument if your function receives a bad argument *)
(* do not change this exception or function                   *)
exception Bad_arg of string
let bad_arg (s:string) = raise (Bad_arg s)

(* a useful debugging routine *)
let debug s = print_string s; flush_all()

(* *** DO NOT CHANGE DEFINITIONS ABOVE THIS LINE! *** *)

(* you may add "rec" after any of the let declarations below that you
 * wish if you find doing so useful. *)


(* find the average gross of the movies in the list                  *)
(* return 0.0 if the list is empty                                   *)
(* hint: you may need to use functions float_of_int and int_of_float *)
(* hint: if you don't know what those functions do,                  *)
(*       type them in to ocaml toplevel                              *)
(* hint: recall the difference between +. and + also 0. and 0        *)
let average (movies : movie list) : float =
  let length = List.length movies in
    let rec sum (ls:movie list) : float =
      match ls with
      | [] -> 0.
      | (_,_,g,_) :: tl -> g +. sum tl in
  if length = 0 then 0. else sum movies /. float_of_int length



(* return a list containing only the movies from the given decade *)
(* call bad_arg if n is not 20, 30, ..., 90, 00, 10               *)
(* Treat 0 as 00 (this is unavoidable as 00 is not represented    *)
(*   differently from 0).                                         *)
(* Note: movies from any years outside the range 1920-2019 will   *)
(* always be discarded but should not raise an error condition    *)

let decade (n:int) (ms:movie list) : movie list =
  if n mod 10 != 0 then bad_arg ""
  else
    let get_dec (year:int) : int =
      year / 10 mod 10
    in
    let rec aux (a:int)(ls:movie list) : movie list =
      match ls with
      | [] -> []
      | hd :: tl -> let (_,_,_,d) = hd in
        if (d < 1920 || d > 2019) then aux a tl
        else if a / 10 = get_dec d then hd :: aux a tl
        else aux a tl
in aux n ms


(* return the first n items from the list *)
(* if there are fewer than n items, return all of them *)
(* call bad_arg if n is negative *)
let rec take (n:int) (l:'a list)  : 'a list =
  if n < 0 then bad_arg ""
  else
    match (n,l) with
    | (0, _) -> []
    | (_,[]) -> []
    | (_, hd :: tl) -> hd :: take (n-1) tl

(* return everything but the first n items from the list *)
(* if there are fewer than n items, return the empty list *)
(* call bad_arg if n is negative *)
let rec drop (n:int) (l:'a list)  : 'a list =
  if n < 0 then bad_arg ""
  else
    match (n,l) with
    | (0, _) -> l
    | (_,[]) -> []
    | (_, hd :: tl) -> drop (n-1) tl


(* return a list [x1; x2; ...; xn] with the same elements as the input l
   and where:
     leq xn xn-1
     ...
     leq x3 x2
     leq x2 x1
     are all true
*)
(* hint: define an auxiliary function "select" *)
type 'a less = 'a -> 'a -> bool
let selection_sort (leq:'a less) (l:'a list) : 'a list =
  let rec select (leq: 'a less) (l : 'a list) : ('a option * 'a list) =
    match l with
    | [] -> (None,[])
    | [x] -> (Some x,[])
    | hd :: tl -> let value = select leq tl in
      match value with
      | (None, _) -> (None, [])
      | (Some min, tl2) ->
        if leq min hd then (Some hd , min :: tl2)
        else (Some min , hd :: tl2)
  in
  let rec sort (leq: 'a less) (l : 'a list): 'a list =
    match l with
    | [] -> []
    | list -> match select leq list with
      | (None, _) -> []
      | (Some min, tl) -> min :: sort leq tl
 in sort leq l

(* ASIDE:  Why does this assignment ask you to implement selection sort?
   Insertion sort is almost always preferable to selection sort,
   if you have to implement a quadratic-time sorting algorithm.
   Insertion sort is faster, it's simpler to implement, and it's
   easier to reason about.  For smallish inputs (less than 5 or 8),
   insertion sort is typically faster than quicksort or any
   other NlogN sorting algorithm.  So, why do we ask you to implement
   selection sort?  Answer: we already showed you insertion sort
   in the lecture notes.

   ASIDE 2: But at least selection sort is better than bubble sort.
   Even Barack Obama knows that. https://www.youtube.com/watch?v=k4RRi_ntQc8
*)

(* return list of movies sorted by gross (largest gross first) *)
let sort_by_gross (movies : movie list) : movie list =
  selection_sort (fun (_, _, x, _) (_, _, y, _) -> x < y) movies


(* return list of movies sorted by year produced (largest year first) *)
let sort_by_year (movies : movie list) : movie list =
  selection_sort (fun (_, _, _, x) (_, _, _, y) -> x < y) movies


(* sort list of (studio, gross in millions) by gross in millions
 * with the largest gross first *)
let sort_by_studio (studio_grosses : studio_gross list) : studio_gross list =
  selection_sort (fun (_, x) (_, y) -> x < y) studio_grosses


(* given list of movies,
 * return list of pairs (studio_name, total gross revenue for that studio)  *)
let by_studio (movies:movie list) : studio_gross list =
  (* Insert movie into studio_gross list, update total if the studio
   * is already in the list *)
  let rec insert_movie (m:movie)(l:studio_gross list) : studio_gross list =
    let (_,studio,gross,_) = m in
    match l with
    | [] -> []@[studio,gross]
    | hd :: tl -> let (curr,total) = hd in
      if curr = studio then (studio, gross +. total) :: tl
      else hd :: insert_movie m tl
  in
  let rec aux (movies:movie list) (sl:studio_gross list): studio_gross list =
    match movies with
    | [] -> sl
    | hd :: tl -> aux tl (insert_movie hd sl)
  in
  aux movies []





(***********)
(* Testing *)
(***********)

(* Augment the testing infrastructure below as you see fit *)

(* Test Data *)

let data1 : movie list = [
  ("The Lord of the Rings: The Return of the King","NL",377.85,2003)
]

let data2 : movie list = [
  ("The Lord of the Rings: The Return of the King","NL",377.85,2003);
  ("The Hunger Games","LGF",374.32,2012)
]

let data3 : movie list = [
  ("Harry Potter and the Sorcerer's Stone","WB",317.57555,2001);
  ("Star Wars: Episode II - Attack of the Clones","Fox",310.67674,2002);
  ("Return of the Jedi", "Fox", 309.306177, 1983)
]

let data4 : movie list = [
  ("The Lord of the Rings: The Return of the King","NL",377.85,2003);
  ("The Hunger Games","LGF",374.32,2012);
  ("The Dark Knight","WB",533.34,2008);
  ("Harry Potter and the Deathly Hallows Part 2","WB",381.01,2011)
]

(* Assertion Testing *)

(* Uncomment the following when you are ready to test your take routine *)

let _ = assert(take 0 data4 = [])
let _ = assert(take 1 data1 = data1)
let _ = assert(take 2 data4 = data2)
let _ = assert(take 5 data2 = data2)
let _ = assert(take 2 data2 = data2)


(* Additional Testing Infrastructure *)

let stests : (unit -> movie list) list = [
  (fun () -> sort_by_gross data1);
  (fun () -> sort_by_gross data2);
  (fun () -> sort_by_gross data3);
  (fun () -> sort_by_gross data4)
]

let check (i:int) (tests:(unit -> 'a) list) : 'a =
  if i < List.length tests && i >= 0 then
    List.nth tests i ()
  else
    failwith ("bad test" ^ string_of_int i)
