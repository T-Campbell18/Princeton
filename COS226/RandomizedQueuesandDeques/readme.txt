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
 *  Explain briefly how you implemented the randomized queue and deque.
 *  Which data structure did you choose (array, linked list, etc.)
 *  and why?
 *****************************************************************************/
I implented the randomized queue with the use of an array of Item. To enqueue
an element the Item is just added to the the size index of the array. If the 
size is equal to the array length it is resized to 2x as big. To dequeue a
random index is chosen from 0 to the size -1 index to ensure that you are
choosing from a valid index with an item. If the size is a 1/4 of the array
length then array is resized to half the size. Array was use becaused it is
easy to access a random index for dequeue method. When iterating returns
elements is random elements.

I implented the deque with a linked list. The add/remove front/last methods
just delete or add to the front or last node. A linked list was used because 
it very effiencent to remove add/remove to front or back (O(1)). 


/******************************************************************************
 *  How much memory (in bytes) do your data types use to store N items
 *  in the worst case? Use the 64-bit memory cost model from Section
 *  1.4 of the textbook and use tilde notation to simplify your answer.
 *  Briefly justify your answers and show your work.
 *
 *  Do not include the memory for the items themselves (as this
 *  memory is allocated by the client and depends on the item type)
 *  or for any iterators, but do include the memory for the references
 *  to the items (in the underlying array or linked list).
 *****************************************************************************/

Randomized Queue:   ~ 16N  bytes
16 object overhead
4 for size int
worst case array can be 2x N
(2)8N + 24 for array
8 for reference
4 for padding
Deque:              ~ 48N bytes
16 object overhead
8 for first node reference
8 for last node reference
4 for size int
48N for list of nodes
node 48
{
    16 obeject overhead
    8 extra overhead
    8 item reference
    8 prvious reference
    8 next reference
}
4 padding


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
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 *****************************************************************************/
n/a


/******************************************************************************
 *  Describe any serious problems you encountered.                    
 *****************************************************************************/
n/a



/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 *****************************************************************************/
This assignment was very striaghtfoward 