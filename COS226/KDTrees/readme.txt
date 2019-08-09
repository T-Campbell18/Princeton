/******************************************************************************
 *  Name:    Tyler Campbell  
 *  NetID:   tylercc  
 *  Precept: P04 
 *
 *  Partner Name:  Lorraine Cliff    
 *  Partner NetID: lcliff    
 *  Partner Precept: P04B   
 *
 *  Hours to complete assignment (optional): 
 *
 ******************************************************************************/



/******************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 *****************************************************************************/
The node was implemented using a private class. Each node contains a left and 
right node, which refer to its left and right subtrees. Also a RectHV object is 
stored in the node. Finally, the point that the node refers to along with the 
value at that given point is also stored within the private node class for any 
given node.

/******************************************************************************
 *  Describe your method for range search in a kd-tree.
 *****************************************************************************/
The range search method recursively works to find all the points in the given 
rectangle. It starts at the root and then it searches in both of the subtrees. 
The base case for the recursion is that the node is null or the rectangle doesn't
intersect with the rectangle associated with the node (point) provided (you 
shouldn't check here at all, if the node rectangles don't intersect). Our code 
first recursively checks the left subtree and then the right. A point is only 
added to our ArrayList (called list) if it is contained in the rectangle. If the
rectangles don’t intersects that nodes children do not get checked. 

/******************************************************************************
 *  Describe your method for nearest neighbor search in a kd-tree.
 *****************************************************************************/
We found the closest point to a given point using a recursive nearest method. 
This method has a base case that the node is null and if so it just returns the 
champion as found so far. Once again the pruning rule is implemented as we make 
sure to only search the node if the closest point discovered so far is father 
than the distance between the query point and the rectangle corresponding to a 
node. In that way our method only searches a node if it might contain a possible
champion (champ) that is closer than the one we saw so far. Also its important 
to note that as we recursively call we change the level aka whether its a 
vertical comparison or horizontal comparison each time. Additionally if the tree 
is totally empty we just return null because there is no champion point in this 
case.
                                        
/******************************************************************************
 *  Using the 64-bit memory cost model from the textbook and lecture,
 *  give the total memory usage in bytes of your 2d-tree data structure
 *  as a function of the number of points N. Use tilde notation to
 *  simplify your answer (i.e., keep the leading coefficient and discard
 *  lower-order terms).
 *
 *  Include the memory for all referenced objects (including
 *  Node, Point2D, and RectHV objects) except for Value objects
 *  (because the type is unknown). Also, include the memory for
 *  all referenced objects.
 *
 *  Justify your answer below.
 *
 *****************************************************************************/

bytes per Point2D: 32 bytes
It actually uses a few more because of the Comparator instance variables.
    (this information was found on the FAQ of the assignment page)
    
bytes per RectHV: 48 bytes
Overhead = 16 bytes
4 double = 8*4 bytes

bytes per KdTree of N points:   ~ 64N
overhead = 16 bytes
1 node = 64 bytes
    private node class = 64N
    overhead = 16
    extra overhead = 8
    point reference = 8 bytes
    value reference = 8 bytes
    rectangle reference = 8 bytes
    left subtree reference = 8 bytes
    right subtree reference = 8 bytes
reference to node = 8 bytes
i integer = 4 byte
4 byte for padding

    total memory: 64N + 32
/******************************************************************************
 *  How many nearest neighbor calculations can your brute-force
 *  implementation perform per second for input100K.txt (100,000 points)
 *  and input1M.txt (1 million points), where the query points are
 *  random points in the unit square? Show the math how you used to determine
 *  the operations per second. (Do not count the time to read in the points
 *  or to build the 2d-tree.)
 *
 *  Repeat the question but with the 2d-tree implementation.
 *****************************************************************************/

                       calls to nearest() per second
                     brute force               2d-tree
                     ---------------------------------
input100K.txt        328.84                   470,337

input1M.txt          15.859                   268,384


In order to test this we wrote a separate class that read in from standard input
and put all the points in the given input text file into a PointST or a KdTree 
respectively. Then we called nearest 1000 times for brute and a million times for 
kdtree and through this we used a stopwatch to discover the elapsed time over 
these given number of calls to nearest. Then we divided the total amount of time 
spent by the number of nearest searches to find the number of searches per second
for each of the data structures (the brute force method (PointST) and the 2d-tree 
structure).
    
For input100K.txt, for the brute force way, 1000 nearest calls took roughly 
3.041 seconds. Therefore there were 328.83 calls per second.
    
For input1M.txt, for the brute force way, 1000 nearest calls took roughly 63.054
seconds. Therefore there were 15.859 calls per second.
    
For input100K.txt, for the 2d-tree structure, 1,000,000 nearest calls took 
roughly 2.126 seconds. Therefore there were 470,337 calls per second.
 
For input1M.txt, for the 2d-tree structure, 1,000,000 nearest calls took roughly
3.726; seconds. Therefore there were 268,384 calls per second.

/******************************************************************************
 *  Known bugs / limitations.
 *****************************************************************************/
None that we know of.

/******************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *****************************************************************************/
None.

/******************************************************************************
 *  Describe any serious problems you encountered.                    
 *****************************************************************************/
Issues with get() and put() repeated calls for generalized points, but it was 
an easy fix!

/******************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 *****************************************************************************/
My partner and I coded, debugged, and submitted our code together.


/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on  how helpful the class meeting was and on how much you learned 
 * from doing the assignment, and whether you enjoyed doing it.
 *****************************************************************************/
 This assignment was particularly difficult, and not quite as enjoyable as some 
 of the others. Not necessarily because it was hard, but it was also kind of 
 boring.