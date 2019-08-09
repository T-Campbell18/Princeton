/******************************************************************************
 *  Name:  Tyler Campbell    
 *  NetID:  tylercc
 *  Precept:  P04 
 *
 *  Partner Name: n/a      
 *  Partner NetID: n/a     
 *  Partner Precept: n/    
 *
 *  Hours to complete assignment (optional):
 *
 ******************************************************************************/


/******************************************************************************
 *  Explain briefly how you implemented the board data type.
 *****************************************************************************/
I implented the board data type using a 1d array to represent the board
as well as varibles for the size, and the postion of the blank space.
neighbor() returns an ArrayList of all the valid boards with the blank
space swapped up, down, left, and right. Both hamming and mahanttan loop through 
the array and add up the proper integer value thens return that value. isgoal
return if the mahatten is 0




/******************************************************************************
 *  Explain briefly how you represented a search node
 *  (board + number of moves + previous search node).
 *****************************************************************************/
the search node has 4 instance varibles the board, number of moves, parent node
(previous node), and the priority which is just the manhattan of the board + 
the number of moves. The compareTo compares the priority of the search nodes



/******************************************************************************
 *  Explain briefly how you detected unsolvable puzzles. What is the
 *  order of growth of the runtime of your isSolvable() method?
 *****************************************************************************/
my issolvable calls numinversion to check for the number of inversions which 
calculates the number of inverions in the board, then if the board is even 
check if the num of inversions plus the x0(row of blank space) is even or odd
for odd boards it is the same without the addtion of x0 

order of growth = N^4 



/******************************************************************************
 *  For each of the following instances, give the minimal number of 
 *  moves to reach the goal state. Also give the amount of time
 *  it takes the A* heuristic with the Hamming and Manhattan
 *  priority functions to find the solution. If it can't find the
 *  solution in a reasonable amount of time (say, 5 minutes) or memory,
 *  indicate that instead. Note that you may be able to solve
 *  puzzlexx.txt even if you can't solve puzzleyy.txt even if xx > yy.
 *****************************************************************************/


            number of          seconds
    instance       moves      Hamming     Manhattan
  ------------  ----------   ----------   ----------
  puzzle28.txt     28           .37            .03
  puzzle30.txt     30          1.75            .04
  puzzle32.txt     32           no             .25
  puzzle34.txt     34           no             .28
  puzzle36.txt     36           no            1.60
  puzzle38.txt     38           no            1.70
  puzzle40.txt     40           no             .21
  puzzle42.txt     42           no            3.91

no = no solution in reasonable time/not enough memeory

/******************************************************************************
 *  If you wanted to solve random 4-by-4 or 5-by-5 puzzles, which
 *  would you prefer:  a faster computer (say, 2x as fast), more memory
 *  (say 2x as much), a better priority queue (say, 2x as fast),
 *  or a better priority function (say, one on the order of improvement
 *  from Hamming to Manhattan)? Why?
 *****************************************************************************/
I would prefer a better priority function because both the hamming and the 
manhattan both have a N^2 order of growth. so for the larger puzzles the 2x
faster would not relactively change the time it takes that much as it would 
still take a long time but improving the priority function say to n log n would 
increase performance greatly 





/******************************************************************************
 *  Known bugs / limitations.
 *****************************************************************************/



/******************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *****************************************************************************/





/******************************************************************************
 *  Describe any serious problems you encountered.                    
 *****************************************************************************/



/******************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 *****************************************************************************/







/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 *****************************************************************************/
very fun assignment