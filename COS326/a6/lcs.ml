open Memoizer
open Timing

type base = Base.base
type dna = Base.dna

(* slow lcs *)
let rec slow_lcs ((s1,s2) : dna * dna) : dna =
  match (s1,s2) with
      ([], _) -> []
    | (_, []) -> []
    | (x :: xs, y :: ys) ->
      if Base.eq x y then
	x :: slow_lcs (xs, ys)
      else
	Base.longer_dna_of (slow_lcs (s1, ys)) (slow_lcs (xs, s2))


(* A potentially useful module *)
module DnaPairOrder : Map.OrderedType with type t = dna * dna =
struct
    type t = dna * dna

    let rec compare_dna x' y' : int =
        match x',y' with
          [],[] -> 0
        | [], xs -> -1
        | xs, [] -> 1
        | x::xs, y::ys ->
	  (match Base.compare x y with
	      0 -> compare_dna xs ys
            | other -> other)


    (* implements a lexicographic ordering:
     * compare the second components only if first components are equal *)
    let compare (a, b) (c, d) =
      match compare_dna a c with
	  0 -> compare_dna b d
        | other -> other

end

(* Task 4.4 *)

(* implement fast_lcs using your automatic memoizer functor!
 * doing so will of course require proper creation of modules and
 * use of functors *)
module AutoMemoizer = Memoizer(Map.Make(DnaPairOrder))

let fast_lcs (ds : dna * dna) : dna =
  let fast (f:dna * dna -> dna) ((s1,s2): dna * dna) : dna =
    match (s1,s2) with
    | ([],_) -> []
    | (_,[]) -> []
    | x::xs, y::ys ->
      if Base.eq x y then x::f(xs,ys)
      else Base.longer_dna_of (f (s1,ys)) (f (xs,s2))
  in AutoMemoizer.memo fast ds

(* Task 4.5 *)

(* Implement some experiment that shows performance difference
 * between slow_lcs and fast_lcs. (Print your results.)
 * Explain in a brief comment what your experiment shows.

   Generated random DNA sequences to run experiments on,
   show the slow_lcs time vs the fast_lcs time to prove
   that fast_lcs is faster (testing is similar to the test in fib.ml) *)

let print_header () =
  print_string "----- --------  ------------\n";
  print_string "  N     Slow        Fast    \n";
  print_string "----- --------  ------------\n";;

let print_row n slow fast =
  let space () = print_string "   " in
  let print f = Printf.printf "%6.4f" f in
  let print_float slow =
    match slow with
    | None -> print_string "   -  "
    | Some f -> print f
  in
  if n < 10 then print_string " ";
  if n < 100 then print_string " ";

  print_int n; space ();
  print_float slow; space ();
  print fast; space ();
  print_newline ();
;;

let experiment ((s1,s2):string*string) : unit =
  let dna1 = Base.dna_from_string s1 in
  let dna2 = Base.dna_from_string s2 in
  let slow ds = Some (time_fun slow_lcs ds) in
  let fast ds = time_fun fast_lcs ds in
  print_row (List.length dna1) (slow (dna1,dna2)) (fast (dna1,dna2))


let main () =
  let dnalist =
  [("T", "C");
   ("TC","CT");
   ("GGGT","TGTG");
   ("CGGACGGC","ATATTTGG");
   ("TGCCTCCGGC","TCAGTATCGA");
   ("CCTAACGGTAAG","AGAGTCACATAA");
   ("ATGGCAGACACCCAGT","GTCGAACAACACCTGA");
   ("TTCCAGGGCCTAATCTGA","TGTGGGCAATACGAGGTA");] in
    (* ("ATGGACTGAGATGAATCTTTAC","CGAAGCGGAAACGGGTGCGTGG")
       22   123.3808   0.0034 (very long test)*)
print_header ();
List.iter experiment dnalist




(* uncomment this block to run your experiment,
 * but please do not submit with it uncommented
 *)
(*let _ = main () *)
