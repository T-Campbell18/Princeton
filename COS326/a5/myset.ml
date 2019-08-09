(* Definitions for sets. *)

exception TODO

(* An interface for set modules *)
module type SET =
sig
  type elt  (* type of elements in the set *)
  type set  (* abstract type for the set *)

  val empty : set

  val is_empty : set -> bool

  val insert : elt -> set -> set

  (* same as insert x empty *)
  val singleton : elt -> set

  val union : set -> set -> set
  val intersect : set -> set -> set

  (* remove an element from the set -- if the
   * element isn't present, does nothing. *)
  val remove : elt -> set -> set

  (* returns true iff the element is in the set *)
  val member : set -> elt -> bool

  (* chooses some member from the set, removes it
   * and returns that element plus the new set.
   * If the set is empty, returns None. *)
  val choose : set -> (elt * set) option

  (* fold a function across the elements of the set
   * in some unspecified order. *)
  val fold : (elt -> 'a -> 'a) -> 'a -> set -> 'a

  (* functions to convert our types to a string. useful for debugging. *)
  val string_of_set : set -> string
  val string_of_elt : elt -> string

  (* runs our tests. See TESTING EXPLANATION *)
  val run_tests : unit -> unit
end



(* parameter to Set modules -- we must pass in some
 * type for the elements of a set, a comparison
 * function, and a way to stringify it.
*)
module type COMPARABLE =
sig
  type t
  val compare : t -> t -> Order.order
  val string_of_t : t -> string

  (* The functions below are used for testing. See TESTING
   * EXPLANATION *)

  (* Generate a value of type t. The same t is always returned *)
  val gen : unit -> t

  (* Generate a random value of type t. *)
  val gen_random : unit -> t

  (* Generate a t greater than the argument. *)
  val gen_gt : t -> unit -> t

  (* Generate a t less than the argument. *)
  val gen_lt : t -> unit -> t

  (* Generate a t between the two arguments. Return None if no such
   * t exists. *)
  val gen_between : t -> t -> unit -> t option
end



(* An example implementation of our COMPARABLE signature. Use this
 * struct for testing. *)
module IntComparable : COMPARABLE =
struct
  open Order
  type t = int
  let compare x y = if x < y then Less else if x > y then Greater else Eq
  let string_of_t = string_of_int
  let gen () = 0
  let gen_random =
    let _ = Random.self_init () in
    (fun () -> Random.int 10000)
  let gen_gt x () = x + 1
  let gen_lt x () = x - 1
  let gen_between x y () =
    let (lower, higher) = (min x y, max x y) in
    if higher - lower < 2 then None else Some (higher - 1)
end



(* A simple, list-based implementation of sets. *)
module ListSet(C: COMPARABLE) : (SET with type elt = C.t) =
struct
  open Order
  type elt = C.t
  type set = elt list

  (* INVARIANT: sorted, no duplicates *)
  let empty = []
  let is_empty xs =
    match xs with
    | [] -> true
    | _ -> false
  let singleton x = [x]
  let rec insert x xs =
    match xs with
    | [] -> [x]
    | y::ys -> (match C.compare x y with
        | Greater -> y::(insert x ys)
        | Eq -> xs
        | Less -> x::xs)

  let union xs ys = List.fold_right insert xs ys
  let rec remove y xs =
    match xs with
    | [] -> []
    | x::xs1 -> (match C.compare y x with
        | Eq -> xs1
        | Less -> xs
        | Greater -> x::(remove y xs1))

  let rec intersect xs ys =
    match xs, ys with
    | [], _ -> []
    | _, [] -> []
    | xh::xt, yh::yt -> (match C.compare xh yh with
        | Eq -> xh::(intersect xt yt)
        | Less -> intersect xt ys
        | Greater -> intersect xs yt)

  let rec member xs x =
    match xs with
    | [] -> false
    | y::ys -> (match C.compare x y with
        | Eq -> true
        | Greater -> member ys x
        | Less -> false)

  let choose xs =
    match xs with
    | [] -> None
    | x::rest -> Some (x,rest)
  let fold f e = List.fold_left (fun a x -> f x a) e

  let string_of_elt = C.string_of_t
  let string_of_set (s: set) : string =
    let f = (fun y e -> y ^ "; " ^ C.string_of_t e) in
    "set([" ^ (List.fold_left f "" s) ^ "])"


  (****************************************************************)
  (* Tests for our ListSet functor                                *)
  (* These are just examples of tests, your tests should be a lot *)
  (* more thorough than these.                                    *)
  (****************************************************************)

  (* adds a list of (key,value) pairs in left-to-right order *)
  let insert_list (d: set) (lst: elt list) : set =
    List.fold_left (fun r k -> insert k r) d lst

  let rec generate_random_list (size: int) : elt list =
    if size <= 0 then []
    else (C.gen_random()) :: (generate_random_list (size - 1))

  let test_insert () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    List.iter (fun k -> assert(member s1 k)) elts ;
    ()

  let test_remove () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    let s2 = List.fold_right (fun k r -> remove k r) elts s1 in
    List.iter (fun k -> assert(not (member s2 k))) elts ;
    ()

  let test_union () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    let elts2 = generate_random_list 100 in
    let s2 = insert_list empty elts2 in
    let s3 = union s1 s2 in
    List.iter (fun k -> assert(member s3 k)) elts ;
    List.iter (fun k -> assert(member s3 k)) elts2 ;
    ()

  let rec size (s:set) (i:int) : int =
    match s with
    | [] -> i
    | hd::tl -> size tl (i + 1)

  let test_intersect () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    let s2 = insert_list empty elts in
    let s3 = intersect s1 s2 in
    let len = fold (fun v r -> r + 1) 0 s3 in
    assert(len = size s3 0);
    assert (intersect empty s1 = empty);
    assert (intersect s1 empty = empty);
    assert (intersect empty empty = empty);
    ()

  let test_member () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    List.iter (fun k -> assert(member s1 k)) elts ;
    let v = List.hd s1 in
    let s2  = remove v s1 in
    assert (not (member s2 v)) ;
    ()

  let test_choose () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    match choose s1 with
    | None -> assert(is_empty(s1))
    | Some (e, s2) ->
      assert (member s1 e);
      assert (not(member s2 e));
      ()

  let test_fold () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    let s2 = empty in
    let len2 = fold (fun _ x -> x + 1) 0 s2 in
    let len = fold (fun _ x -> x + 1) 0 s1 in
    assert(len2 = size s2 0);
    assert(len = size s1 0);
    ()

  let test_is_empty () =
    let s1 = empty in
    assert(is_empty(s1)) ;
    let elts = generate_random_list 100 in
    let s2 = insert_list s1 elts in
    assert(not(is_empty(s2))) ;
    ()

  let test_singleton () =
    let v = C.gen_random() in
    let s1 = singleton v in
    assert(member s1 v) ;
    assert(List.length s1 = 1) ;
    ()

  let run_tests () =
    test_insert () ;
    test_remove () ;
    test_union () ;
    test_intersect () ;
    test_member () ;
    test_choose () ;
    test_fold () ;
    test_is_empty () ;
    test_singleton () ;
    ()

end


(******************************************************************)
(* DictSet: a functor that creates a SET by calling our           *)
(* Dict.Make functor                                              *)
(******************************************************************)

module DictSet(C : COMPARABLE) : (SET with type elt = C.t) =
struct
  module D = Dict.Make(struct
      type key = C.t
      type value = unit
      let compare = C.compare
      let string_of_key = C.string_of_t
      let string_of_value () = ""
      let gen_key = C.gen
      let gen_key_random  = C.gen_random
      let gen_key_gt x = C.gen_gt x
      let gen_key_lt x = C.gen_lt x
      let gen_key_between x y = C.gen_between x y
      let gen_value () = ()
      let gen_pair () = (gen_key (), gen_value())
    end)

  open Order
  type elt = D.key
  type set = D.dict
  let empty = D.empty
  let is_empty set =
    match D.choose set with
    | None -> true
    | Some _ -> false
  let insert x ds = D.insert ds x ()
  let singleton x = insert x empty
  let remove x ds = D.remove ds x
  let fold f x ds = D.fold (fun k v x -> f k x) x ds
  let union ds1 ds2 = fold insert ds1 ds2
  let member ds x = D.member ds x
  let choose ds =
    match D.choose ds with
    | None -> None
    | Some (x,_,d) -> Some (x,d)
  let rec intersect ds1 ds2 =
    match D.choose ds1, D.choose ds2 with
    | None, _ -> empty
    | _, None -> empty
    | Some(ds1hd, _, ds1tl), Some(ds2hd, _, ds2tl) ->
      match C.compare ds1hd ds2hd with
      | Eq -> D.insert (intersect ds1tl ds2tl) ds1hd ()
      | Less -> intersect ds1tl ds2
      | Greater -> intersect ds1 ds2tl



  (* implement the rest of the functions in the signature! *)

  let string_of_elt = D.string_of_key
  let string_of_set s = D.string_of_dict s

  (****************************************************************)
  (* Tests for our DictSet functor                                *)
  (* Use the tests from the ListSet functor to see how you should *)
  (* write tests. However, you must write a lot more              *)
  (* comprehensive tests to test ALL your functions.              *)
  (****************************************************************)

(* add your test functions to run_tests *)

  let insert_list (d: set) (lst: elt list) : set =
    List.fold_left (fun r k -> insert k r) d lst

  let rec generate_random_list (size: int) : elt list =
    if size <= 0 then []
    else (C.gen_random()) :: (generate_random_list (size - 1))

  let test_insert () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    List.iter (fun k -> assert(member s1 k)) elts ;
    ()

  let test_remove () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    let s2 = List.fold_right (fun k r -> remove k r) elts s1 in
    List.iter (fun k -> assert(not (member s2 k))) elts ;
    ()

  let test_union () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    let elts2 = generate_random_list 100 in
    let s2 = insert_list empty elts2 in
    let s3 = union s1 s2 in
    List.iter (fun k -> assert(member s3 k)) elts ;
    List.iter (fun k -> assert(member s3 k)) elts2 ;
    ()

  let rec size (s:set) (i:int) : int =
    match choose s with
    | None -> i
    | Some (_, s) -> size s (i + 1)

  let test_intersect () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    let s2 = insert_list empty elts in
    let s3 = intersect s1 s2 in
    let len = fold (fun v r -> r + 1) 0 s3 in
    assert(len = size s3 0);
    assert (intersect empty s1 = empty);
    assert (intersect s1 empty = empty);
    assert (intersect empty empty = empty);
    ()

  let test_member () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    List.iter (fun k -> assert(member s1 k)) elts ;
    ()

  let test_choose () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    match choose s1 with
    | None -> assert(is_empty(s1))
    | Some (e, s2) ->
      assert (member s1 e);
      assert (not(member s2 e));
      ()

  let test_fold () =
    let elts = generate_random_list 100 in
    let s1 = insert_list empty elts in
    let s2 = empty in
    let len2 = fold (fun _ x -> x + 1) 0 s2 in
    let len = fold (fun _ x -> x + 1) 0 s1 in
    assert(len2 = size s2 0);
    assert(len = size s1 0);
    ()

  let test_is_empty () =
    let s1 = empty in
    assert(is_empty(s1)) ;
    let elts = generate_random_list 100 in
    let s2 = insert_list s1 elts in
    assert(not(is_empty(s2))) ;
    ()

  let test_singleton () =
    let k = C.gen_random() in
    let s1 = singleton k in
    assert(member s1 k) ;
    let s0 = remove k s1 in
    assert(is_empty(s0)) ;
    ()

  let run_tests () =
    test_insert () ;
    test_remove () ;
    test_union () ;
    test_intersect () ;
    test_member () ;
    test_choose () ;
    test_fold () ;
    test_is_empty () ;
    test_singleton () ;
    ()
end






(******************************************************************)
(* Run our tests.                                                 *)
(******************************************************************)

(* Create a set of ints using our ListSet functor. *)
module IntListSet = ListSet(IntComparable) ;;
IntListSet.run_tests();;

(* Create a set of ints using our DictSet functor
 *
 * Uncomment out the lines below when you are ready to test your
 * 2-3 dict set implementation *)

module IntDictSet = DictSet(IntComparable) ;;
IntDictSet.run_tests();;



(******************************************************************)
(* Make: a functor that creates a SET by calling our              *)
(* ListSet or DictSet functors                                    *)
(******************************************************************)
module Make(C : COMPARABLE) : (SET with type elt = C.t) =
  (* Change this line to use our dictionary implementation when your are
   * finished. *)
  (*ListSet (C) *)
 DictSet (C)
