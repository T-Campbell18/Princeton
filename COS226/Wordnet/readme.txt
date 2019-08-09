/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name: Lorraine Cliff 
 * Partner NetID: lcliff
 * Partner Precept: P04B   
 *
 *  Hours to complete assignment (optional):
 *
 ******************************************************************************/


/******************************************************************************
 *  Describe concisely the data structure(s) you used to store the 
 *  information in synsets.txt. Why did you make this choice?
 *****************************************************************************/
We used a hashMap that maps Integers to Strings. Since for each specific syntex 
there is id number associated with that syntex along with a string that 
represents the syntext itself. Therefore it makes sense to use an data structure 
that can easily map Integers to Strings such as a hashMap. Also since we wanted 
to be able to use the methods put, get, and size to help represent and use the 
information from synsets.txt, a hashMap seemed like a natural choice.

/******************************************************************************
 *  Describe concisely the data structure(s) you used to store the 
 *  information in hypernyms.txt. Why did you make this choice?
 *****************************************************************************/
We used a hashMap that maps Strings to an arrayList to represent the information 
in hypernyms.txt. We wanted to be able to map individual synsets with their given 
id numbers, to all the hypernyms associated with it. Therefore while the synset 
with the given id was the String aka the noun in our hashMap, the arrayList 
represented the id numbers of all the hypernyms that the text file specified 
were associated with this particular index.

/******************************************************************************
 *  Describe concisely the algorithm you use in the constructor of
 *  ShortestCommonAncestor to check if the digraph is a rooted DAG.
 *  What is the order of growth of the worst-case running times of
 *  your algorithms as a function of the number of vertices V and the
 *  number of edges E in the digraph?
 *****************************************************************************/

Description: We basically created a DirectedCylce object based on the graph first. 
That way we knew that if the graph contained any directed cycles the digraph was 
not a rooted DAG. Then we advanced through each of the vertixes in the graph 
and for everyone we checked if there was another vertix adjacent to that vertex 
and if there was we incremented the root. That way if at the end the root did 
not equal one we knew that the digraph was not a rooted DAG.
   
Order of growth of running time: E + 2V


/******************************************************************************
 *  Describe concisely your algorithm to compute the shortest common
 *  ancestor in ShortestCommonAncestor. What is the order of growth of
 *  the running time of your methods as a function of the number of
 *  vertices V and the number of edges E in the digraph? What is the
 *  order of growth of the best-case running time?
 *
 *  If you use hashing, you should assume the uniform hashing assumption
 *  so that put() and get() take constant time.
 *
 *  Be careful! If you use a BreadthFirstDirectedPaths object, don't
 *  forget to count the time needed to initialize the marked[],
 *  edgeTo[], and distTo[] arrays.
 *****************************************************************************/

Description: The shortest common ancestor is found by calculating the breadth
first search of the two vertices and then based on the breadth first searches 
finding the vertex in the directed graph whose distance to the first vertex 
plus its distance to the second vertex is smallest. That particular vertex that
minimizes the accumilative distance to both the two vertices in question is the
shortest common ancestor of the two vertices.

                               running time
method                               best case            worst case
------------------------------------------------------------------------
length(int v, int w)              E + V                  E + V

ancestor(int v, int w)            E + V                  E + V

length(Iterable<Integer> v,       (a * b) * (E + V)      (a * b) * (E + V)  
     Iterable<Integer> w)        

ancestor(Iterable<Integer> v,     (a * b) * (E + V)      (a * b) * (E + V)  
      Iterable<Integer> w)




/******************************************************************************
 *  Known bugs / limitations.
 *****************************************************************************/
no DeluxeBFS class created

/******************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *****************************************************************************/


/******************************************************************************
 *  Describe any serious problems you encountered.                    
 *****************************************************************************/
None!

/******************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 *****************************************************************************/
We did follow the protocal described on the assignment page. As a partnership 
we coded together, debugged together, and submitted together.


/**********************************************************************
 *  Have you completed the mid-semester survey? If you haven't yet,
 *  please complete the brief mid-course survey at https://goo.gl/gB3Gzw
 * 
 ***********************************************************************/
Yes.

/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 *****************************************************************************/