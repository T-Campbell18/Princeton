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

Final Programming Project: Atomic Nature of Matter

Hours to complete assignment (optional):


/**************************************************************************
 *  The input size n for BeadTracker is the product of the number of      *
 *  pixels per frame and the number of frames. What is the estimated      *
 *  running time (in seconds) of BeadTracker as a function of n?          *
 *  Justify your answer with empirical data and explain how you used it.  *
 *  Your answer should be of the form a*n^b where b is an integer.        *
 **************************************************************************/

a = 1.45 e -7
b = 1

time = 1.45e-7 * n

307200 pixels per frame

framea    n                  time   
25        7680000            1.376
50        15360000           2.451
100       30720000           4.509
200       61440000           8.722
400       122880000          17.629

the data was used in a power regression to find a and b(b was forced
to an integer)


/**************************************************************************
 *  Did you receive help from classmates, past COS 126 students, or       *
 *  anyone else?  Please list their names.  ("A Sunday lab TA" or         *
 *  "Office hours on Thursday" is ok if you don't know their name.)       *
 **************************************************************************/
n/a


/**************************************************************************
 *  Describe any serious problems you encountered.                        *
 **************************************************************************/
n/a


/**************************************************************************
 *  List any other comments here.                                         *
 **************************************************************************/
