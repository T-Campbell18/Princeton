(*************************************************)
(* An environment-based evaluator for Dynamic ML *)
(*************************************************)

open Syntax
open Printing
open EvalUtil

(* Defines the subset of expressions considered values
   Notice that closures are values but the rec form is not -- this is
   slightly different from the way values are defined in the
   substitution-based interpreter.  Rhetorical question:  Why is that?
   Notice also that Cons(v1,v2) is a value (if v1 and v2 are both values).
*)
let rec is_value (e:exp) : bool =
  match e with
      Constant _ -> true
    | Pair (e1, e2) -> is_value e1 && is_value e2
    | EmptyList -> true
    | Cons (e1, e2) -> is_value e1 && is_value e2
    | Closure _ -> true
    | _ -> false

(* removes free variables from list *)
let rec freevars (env:env) (ls: variable list) : env =
  match env with
  | [] -> []
  | (var, exp) :: tl ->
    if List.mem var ls
    then (var, exp) :: freevars tl ls
    else freevars tl ls

(* evaluation; use eval_loop to recursively evaluate subexpressions *)
let eval_body (env:env) (eval_loop:env -> exp -> exp) (e:exp) : exp =
  match e with
  | Var x -> (
      match lookup_env env x with
      | None -> raise (UnboundVariable x)
      | Some v -> v)
  | Constant x -> e
  | EmptyList -> e
  | Closure _ as e -> e
  | Op (exp1, op, exp2) -> (
    let e1 = eval_loop env exp1 in
    let e2 = eval_loop env exp2 in
    apply_op e1 op e2)
  | If (exp1, exp2, exp3) -> (
    match eval_loop env exp1 with
    | Constant (Bool true) -> eval_loop env exp2
    | Constant (Bool false) -> eval_loop env exp3
    | v -> raise (BadIf v))
  | Let (v, exp1, exp2) ->
    eval_loop (update_env env v (eval_loop env exp1)) exp2
  | Pair (exp1, exp2) -> Pair((eval_loop env exp1), (eval_loop env exp2))
  | Fst (exp) -> (
      match eval_loop env exp with
      | Pair (e, _) -> eval_loop env e
      | _ -> Fst (eval_loop env exp))
  | Snd (exp) -> (
      match eval_loop env exp with
      | Pair (_, e) -> eval_loop env e
      | _ -> Snd (eval_loop env exp))
  | Cons (exp1, exp2) -> Cons ((eval_loop env exp1), (eval_loop env exp2))
  | Match (e1, e2, hd, tl, e3) -> (
      match eval_loop env e1 with
      | EmptyList -> eval_loop env e2
      | Cons (head, tail) -> (
          let env1 = update_env env hd (head) in
          let env2 =  update_env env1 tl (tail) in
          eval_loop env2 e3)
      | _ -> raise (BadMatch e1))
  | Rec (f, x, body) ->
      let rec aux (exp:exp) : variable list =
        match exp with
        | Var (e) -> []
    	  | Constant (e) -> []
        | Op (e1, op, e2) -> aux e1 @ aux e2
	      | If (e1, e2, e3) -> aux e1 @ aux e2 @ aux e3
    	  | Let (var, e1, e2) -> var :: aux e1 @ aux e2
    	  | Pair (e1, e2) -> aux e1 @ aux e2
    	  | Fst (e) -> aux e
    	  | Snd (e) -> aux e
        | EmptyList -> []
    	  | Cons(e1, e2) -> aux e1 @ aux e2
    	  | Match (e1, e2, hd, tl, e3) -> hd :: tl :: aux e1 @ aux e2 @ aux e3
    	  | Closure (env, f, x, b) -> f :: x :: aux b
        | Rec (f, x, b) -> f :: x :: aux b
        | App (e1, e2) -> aux e1 @ aux e2
      in
      Closure (freevars env (aux body), f, x, body)
(* when calling freevars, clo raises expection *)
(* when freevars does not change varible list all test work *)
    | App (exp1, exp2) ->
        match eval_loop env exp1 with
        | (Closure (e, f, x, b)) as c ->
            let env1 = update_env e x (eval_loop env exp2) in
            let env2 = update_env env1 f c in
            eval_loop env2 b
        | _ -> raise (BadApplication exp1)


(* evaluate closed, top-level expression e *)

let eval e =
  let rec loop env e = eval_body env loop e in
  loop empty_env e


(* print out subexpression after each step of evaluation *)
let debug_eval e =
  let rec loop env e =
    if is_value e then e  (* don't print values *)
    else
      begin
	Printf.printf "Evaluating %s\n" (string_of_exp e);
	let v = eval_body env loop e in
	Printf.printf
	  "%s evaluated to %s\n" (string_of_exp e) (string_of_exp v);
	v
      end
  in
  loop empty_env e
