(*

Name: Tyler Campbell
Email: tylercc@princeton.edu
Minutes Spent on Problem 2:

(You aren't in any way graded on the number of minutes spent;
 we are just trying to calibrate for future versions of the class)

Comments/Problems/Thoughts on this part of the assignment:

*)

open Ast
open ExpressionLibrary

(* TIPS FOR PROBLEM 2:
 * 1. Read the writeup.
 * 2. Use the type definitions in the ast.ml as a reference. But don't worry
 *    about expressionLibrary.ml
 * 3. Test!  (Use "assert" where appropriate.)
 *)


(*>* Problem 2.1 *>*)

(* evaluate : evaluates an expression for a particular value of x.
 *  Example : evaluate (parse "x*x + 3") 2.0 = 7.0 *)
let rec evaluate (e:expression) (x:float) : float =
  match e with
  | Num n -> n
  | Var -> x
  | Binop (op, a, b) ->
      match op with
      | Add -> (evaluate a x) +. (evaluate b x)
      | Sub -> (evaluate a x) -. (evaluate b x)
      | Mul -> (evaluate a x) *. (evaluate b x)

let _ = assert (evaluate (parse "x*x + 3") 2.0 = 7.0)

(*>* Problem 2.2 *>*)

(* See writeup for instructions.  *)
let rec derivative (e:expression) : expression =
  match e with
  | Num _ -> Num 0.
  | Var -> Num 1.
  | Binop (op, a, b) ->
    match op with
    | Add -> Binop(Add, derivative a,derivative b)
    | Sub -> Binop(Sub, derivative a,derivative b)
    | Mul -> Binop(Add, Binop(Mul, a, derivative b),Binop(Mul, derivative a, b))



(* A helpful function for testing. See the writeup. *)
let checkexp strs xval=
  print_string ("Checking expression: " ^ strs^"\n");
  let parsed = parse strs in (
	print_string "Result of evaluation: ";
	print_float  (evaluate parsed xval);
	print_endline " ";
	print_string "Result of derivative: ";
	print_endline " ";
	print_string (to_string (derivative parsed));
	print_endline " ");;

(* checkexp "2*x*x" 3. *)

(*>* Problem 2.3 *>*)

(* See writeup for instructions. *)
let rec find_zero (e:expression) (g:float) (epsilon:float) (lim:int)
    : float option =
  if abs_float (evaluate e g) < epsilon then Some g
  else
    match (g, lim) with
    | (_, 0) -> None
    | (_, _) -> find_zero e (g -. evaluate e g /. evaluate (derivative e) g)
                                                              epsilon (lim - 1)



(*>* Problem 2.4 *>*)

(* for representing an expression as a tuple list
 * adds expression to polynomial list rec to make a polynomial *)
let rec polynomial
    (e : expression) (p: (float * float) list) : (float * float) list =
  match e with
  | Num a -> List.map (fun (coeff, pow) -> (coeff *. a, pow)) p
  | Var -> List.map (fun (coeff, pow) -> (coeff, pow +. 1.)) p
  | Binop (op, a, b) ->
    match op with
    | Add -> (polynomial a p) @ (polynomial b p)
    | Sub ->  (polynomial a p) @
        (polynomial b (List.map (fun (coeff, pow) -> (-1. *. coeff, pow)) p))
    | Mul -> (polynomial b (polynomial a p))

(* removes polynomials with coefficients that are 0 *)
let zerocoeffs  (p : (float * float) list) : (float * float) list =
  List.filter (fun (c,_) -> if c = 0. then false else true) p

(* simplifys the polynomial*)
let rec simplify (pls : (float * float) list) (new_element : float * float)
  : (float * float) list =
  let (coeff, pow) = new_element in
  match pls with
  | [] -> [new_element]
  | (c, p) :: tl ->
    if (p = pow)
    then (c +. coeff, p) :: tl
    else (c, p) :: simplify tl new_element

(* solves a one degree list represntation of a polynomial *)
let solve (input : (float * float) list) : expression option =
  match input with
  | [(b, 0.); (a, 1.)] -> Some (Num (-1. *. b /. a))
  | [(a, 1.); (b, 0.)] -> Some (Num (-1. *. b /. a))
  | [(a, 1.)] -> Some (Num 0.)
  | _ -> None

(* See writeup for instructions. *)

(* create polynomial, simplify the polynomial, then solve it *)
let find_zero_exact (e:expression) : expression option =
  match e with
  | Num x -> Some (Num x)
  | Var -> Some (Num 0.)
  | Binop (_, _, _) ->
    let poly = polynomial e [(1., 0.)] in
    let simplified = zerocoeffs (List.fold_left simplify [] poly) in
    solve simplified

let _ = assert(find_zero_exact
                 (parse "5*x - 3 + 2*(x - 8)") = Some (Num (19. /. 7.)))
let _ = assert(find_zero_exact (parse "2*x*x+5*x - 3 + 2*(x - 8)") = None)
