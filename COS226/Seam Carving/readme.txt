/******************************************************************************
 *  Name: Tyler Campbell       
 *  NetID: tylercc
 *  Precept: P04 
 *
 *  Partner Name: N/A      
 *  Partner NetID: N/A      
 *  Partner Precept: N/A   
 *
 *  Hours to complete assignment (optional):
 *
 ******************************************************************************/


/******************************************************************************
 *  Describe concisely your algorithm to compute the horizontal and
 *  vertical seam. 
 *****************************************************************************/
To compute a vertical seam I first calculate the energy if the picture pixel
by pixel. Then loop through 2D array of energy again and add the cumlative energy.
To find the cumlative energy you add the minimum energy from the possible previous
pixels (which are the ones directly above and if possible up/left and up/right.
After this find the minimum value in the bottom row, then back track from that
pixel to the top always selecting the minimum cumlative energy for the possible 
previous pixels. While doing this add the column postion to the seam array.

To compute the horizontal seam, transpose the picture then use same method as
vertical seam, transpose picture back.


/******************************************************************************
 *  Describe what makes an image ideal for this seamCarving algorithm and what
 *  kind of image would not work well.
 *****************************************************************************/
the most ideal image would be a image that has a width of one pixel, but all
other picture would be the seame since every pixels is visted the same amount
of times regardless of the type of content in the picture.



/******************************************************************************
 *  Give a formula (using tilde notation) for the running time 
 *  (in seconds) required to reduce image size by one row and a formula
 *  for the running time required to reduce image size by one column. 
 *  Both should be functions of W and H. Removal should involve exactly
 *  one call to the appropriate find method and one call to the 
 *  appropriate remove method. The randomPicture() method in SCUtility 
 *  may be useful.
 * 
 *  Justify your answer with sufficient data using large enough 
 *  W and H values. To dampen system effects, you may wish to perform
 *  many trials for a given value of W and H and average the results.
 *  
 *  Be sure to give the leading coefficient and the value of the exponents 
 *  as a fraction with 2 decimal places. And show your calculations.
 *****************************************************************************/

(keep W constant)
W = 8000

 H           Row removal time (seconds)     Column removal time (seconds)
--------------------------------------------------------------------------
1000                2.151                               1.570
2000                4.998                               3.633
4000               10.489                               6.857
8000               25.366                              15.746
16000              60.281                              26.523

(keep H constant)
H = 8000

 W           Row removal time (seconds)     Column removal time (seconds)
--------------------------------------------------------------------------
1000                2.372                               1.145
2000                5.009                               2.198
4000                9.117                               4.307
8000               19.137                               9.284
16000              42.844                              24.059

Running time to remove one row as a function of
                                   both W and H:  ~  3.00e-8 * W^1.07 * H^1.21

Ratio for H: 2.32, 2.1, 2.42, 2.38 ==> 2.31
lg 2.31 = 1.21
Ratio for W: 2.11, 1.82, 2.10, 2.24 ==> 2.08
lg 2.08 = 1.07
c = t / (W^1.07 * H^1.21)  (get t, W, H from table then average values)
c = 3.00e-8
Running time to remove one column as a function of
                                   both W and H: ~ 6.45e-8 c* W^1.08 * H^1.04
Ratio for H: 2.31, 1.89, 2.30, 1.69 ==> 2.05
lg 2.05 = 1.04
Ratio for W: 1.92, 1.96, 2.16, 2.45 ==> 2.12
lg 2.12 = 1.08
c = t / (W^1.08 * H^1.04)  (get t, W, H from table then average values)
c = 6.45e-8

/******************************************************************************
 *  Known bugs / limitations.
 *****************************************************************************/
none

/******************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *****************************************************************************/
n/a

/******************************************************************************
 *  Describe any serious problems you encountered.                    
 *****************************************************************************/
n/a

/******************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 *****************************************************************************/
n/a



/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 *****************************************************************************/
very fun and useful assignment 