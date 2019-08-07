/******************************************************************************
 *  Name: Tyler Campbell   
 *  NetID: tylercc 
 *  Precept: P02
 *
 *  Partner Name: n/a
 *  Partner NetID: n/a   
 *  Partner Precept: n/a 
 * 
 ******************************************************************************/

Which partner is submitting the program files? me

Programming Assignment 8: Traveling Salesperson Problem

Hours to complete assignment (optional): 


/**********************************************************************
 *  Explain how you implemented the nearest insertion heuristic.      *
 **********************************************************************/
if empty list create node and set its next node to itself
traverse list using do while, if distance to next point is less than 
previous distance (starts at infinity) reset closest distance, and
store that node in the close varible.
After traversing list create and insert node after the close node


/**********************************************************************
 *  Explain how you implemented the smallest insertion heuristic.     *
 *  It's sufficient to list only the differences between this         *
 *  heuristic and the nearest insertion heuristic.                    *
 **********************************************************************/
calculate the new distance by finding distance from current point to 
point p + the distance from point p to the next point. calulate the 
orginal distance from current point to next point. calculate the 
change in distance by substracting the current distance from the new 
distance. if the change is less than the previous change in distance
reset smallesr increase distance, and store that node in the small
varible.


/**********************************************************************
 *  Explain why it's better to use a circular linked list instead of  *
 *  an array.                                                         *
 **********************************************************************/
it is more effiecient to insert a node in linked list than an array 


/**********************************************************************
 *  Fill in the lengths computed by your heuristics.                  *
 **********************************************************************/

data file      nearest neighbor     smallest increase
-----------------------------------------------------
tsp10.txt         1566.1363             1655.7462
tsp100.txt        7389.9297             4887.2190
tsp1000.txt       27868.7106            17265.6282
usa13509.txt      77449.9794            45074.7769

/**********************************************************************
 *  Do two timing analyses. Estimate the running time (in seconds)    *
 *  of each heuristic as a function of n, using expressions of the    *
 *  form: a * n^b, where b is an integer.                             *
 *                                                                    *
 *  Explain how you determined each of your answers.                  *
 *                                                                    *
 *  To get your data points, run the two heuristics for n = 1,000,    *
 *  and repeatedly double n until the execution time exceeds 60       *
 *  seconds.                                                          *
 *                                                                    *
 *  You may use TSPTimer to help do this, as per the checklist.       *
 *  If you do so, execute it with the -Xint option. This turns off    *
 *  various compiler optimizations, which helps normalize and         *
 *  stabilize the timing data that you collect.                       *
 *                                                                    *
 *  (If n = 1,000 takes over 60 seconds, your code is too slow.       *
 *  See the checklist for a suggestion on how to fix it.)             *
 **********************************************************************/

n               nearest time           smallest time
------------------------------------------------------------
1000            0.05                   0.135
2000            0.182                  0.535
4000            0.71                   2.18
8000            2.879                  9.036
16000           12.254                 36.377
32000           50.858                 140.452


to solve for a and b, by looking at data points guessed that the
function was quadratic therefore b = 2. then solved for a 
(time / n^2 = a)for each point and averaged them to get a.


time = a * n ^ b

nearest time : a = 4.485 e -8; b = 2
smallest time: a = 1.238 e -8; b = 2

/**********************************************************************
 *  Did you receive help from classmates, past COS 126 students, or
 *  anyone else?  Please list their names.  ("A Sunday lab TA" or 
 *  "Office hours on Thursday" is ok if you don't know their name.)
 **********************************************************************/
no

/**********************************************************************
 *  Describe any serious problems you encountered.                    
 **********************************************************************/
none


/**********************************************************************
 *  List any other comments here.                                     
 **********************************************************************/

