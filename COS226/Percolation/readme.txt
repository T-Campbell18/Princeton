/******************************************************************************
 *  Name: Tyler Campbell 
 *  NetID: tylercc 
 *  Precept: P04
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 * 
 *  Operating system: MAC OS
 *  Compiler: javac 1.8.0_91
 *  Text editor / IDE: Coderunner
 *
 *  Have you taken (part of) this course before: no 
 *  Have you taken (part of) the Coursera course Algorithm, Part I: no
 *
 *  Hours to complete assignment (optional):
 *
 ******************************************************************************/


/******************************************************************************
 *  Describe how you implemented Percolation.java. How did you check
 *  whether the system percolates?
 *****************************************************************************/
I used a system where each site is represented by a byte. A closed site is 0,
opened site is 1, connected to top is 2(10), and connected to the bottom is 
4(100) at the start. A system percolates if at the site or the root of that 
site is connected to both the top and the bottom (represented by 7 (111)). 
The bitwise and is used to check if it open or connected to top/bottom. The 
bitwise or is used to mark a site as open or connected to top/bottom.

I started by having 2 WeightedQuickUnionUF objects to fix the backwash 
probelmbut was over the (bonus) memory, I then used a system that incleded 
int, but also was over memory, I then used that as a basis to create the
system with bytes

/******************************************************************************
 *  Using Percolation with QuickFindUF.java,  fill in the table below such that 
 *  the N values are multiples of each other.

 *  Give a formula (using tilde notation) for the running time (in seconds) of 
 *  PercolationStats.java as a function of both N and T. Be sure to give both 
 *  the coefficient and exponent of the leading term. Your coefficients should 
 *  be based on empirical data and rounded to two significant digits, such as 
 *  5.3*10^-8 * N^5.0 T^1.5.
 *****************************************************************************/

(keep T constant)

 N          time (seconds)
------------------------------
25          .033
50          .117
100        1.428
200       24.546
400      379.075


(keep N constant)

 T          time (seconds)
------------------------------
25          .361
50          .667
100        1.265
200        2.614
400        5.16
800       10.433


running time as a function of N and T:  ~ 1.5*10^-9 * N^3.5 T^1.0


/******************************************************************************
 *  Repeat the previous question, but use WeightedQuickUnionUF.java.
 *****************************************************************************/

(keep T constant)

 N          time (seconds)
------------------------------
25          .024
50          .045
100         .129
200         .367
400        1.211
800        8.666


(keep N constant)

 T          time (seconds)
------------------------------
25          .064
50          .123
100         .214
200         .29
400         .377
800         .651


running time as a function of N and T:  ~ 2.0*10^-6 * N^1.8 T^.65

/**********************************************************************
 *  How much memory (in bytes) does a Percolation object (which uses
 *  WeightedQuickUnionUF.java) use to store an N-by-N grid? Use the
 *  64-bit memory cost model from Section 1.4 of the textbook and use
 *  tilde notation to simplify your answer. Briefly justify your
 *  answers.
 *
 *  Include the memory for all referenced objects (deep memory).
 **********************************************************************/

~ 9 n^2

Since using tilde notation only the highest order of growth term is 
used.From the WeightedQuickUnionUF object there is 8n^2 of bytes used
because there are 2 arrays of int. One array uses 4n, so 2 is 8n. In 
Percolationthe object is instantiated with n * n elements so 8n^2 is 
the total cost. In percolation there is a array of byte (byte array 
uses n) that is instantiated with n * n elements, so its total use is 
1n^2. 8 + 1 = 9 therefore total is 9n^2.

/******************************************************************************
 *  After reading the course collaboration policy, answer the
 *  following short quiz. This counts for a portion of your grade.
 *  Write down the answers in the space below.
 *****************************************************************************/
1. B
2. C
3. D

1. How much help can you give a fellow student taking COS 226?
(a) None. Only the preceptors and lab TAs can help.
*(b) You can discuss ideas and concepts but students can get help
  debugging their code only from a preceptor, lab TA, or
  student who has already passed COS 226.
(c) You can help a student by discussing ideas, selecting data
  structures, and debugging their code.
(d) You can help a student by emailing him/her your code.

2. What are the rules when partnering?
 (a) You and your partner must both be present while writing code.
   But after that only one person needs to do the analysis.
 (b) You and your partner must both be present while writing code
   and during the analysis, but, after that, only one person
   needs to be present while submitting the code and the
   readme.
 *(c) You and your partner must both be present while writing code,
   during the analysis, and while submitting the code and the
   readme. Failure to do so is a violation of the course
   collaboration policy.

3. For any programming assignment, I am permitted to use code that
   I found on a website other than the COS 226 or algs4 website
   (e.g., on GitHub or StackOverflow):
 (a) Only when the online code was written by a person who does not
   and did not go to Princeton.
 (b) Only when the online code implemented different assignment
   specifications than the ones I'm currently working on.
 (c) Always, because online code is available to everyone.
 *(d) Never, because such websites are an impermissible "outside 
   source".
 
/******************************************************************************
 *  Known bugs / limitations.
 *****************************************************************************/
the use of byte[] makes code very complex, and not as easy to understand as
using a boolean[], checkstyle error comes from checker, but told was okay
from this assignment

/******************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *****************************************************************************/


/******************************************************************************
 *  Describe any serious problems you encountered.                    
 *****************************************************************************/
none



/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 *****************************************************************************/
I enjoyed this assignment very much, figuring out how to fix the backwash 
probelm while maintaining the memory was difficult, but fun to solve