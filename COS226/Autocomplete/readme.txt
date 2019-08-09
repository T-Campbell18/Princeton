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
 *  Describe how your firstIndexOf() method in BinarySearchDeluxe.java
 *  finds the first index of a key that equals the search key.
 *****************************************************************************/
the firstIndexOf() works similar to the normal binary search if the mid
value is not equal to the key. However, the method is different if the key is
equal to the object at current index. Instead of returning that index it makes the 
high what the middle was. It continues to run normal until the low index equals
the middle index which returns the lowest index that is the key. This works
because once the key is found equal the object at index all keys to right
can be ignored since the index will be greater, but must include that
current index.

/******************************************************************************
 *  What is the order of growth of the number of compares (in the
 *  worst case) that each of the operations in the Autocomplete
 *  data type make, as a function of the number of terms N and the
 *  number of matching terms M?
 *
 *  Recall that with order-of-growth notation, you should discard
 *  leading coefficients and lower order terms, e.g., M^2 + M log N.
 *****************************************************************************/

constructor: N log N

allMatches(): M log M + log N

numberOfMatches(): log N  




/******************************************************************************
 *  Known bugs / limitations.
 *****************************************************************************/
none

/******************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *
 *  Also include any resources (including the web) that you may
 *  may have used in creating your design.
 *****************************************************************************/
none

/******************************************************************************
 *  Describe any serious problems you encountered.                    
 *****************************************************************************/
none

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
n/a
  
