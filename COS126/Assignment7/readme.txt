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

Programming Assignment 7: Markov Model

Hours to complete assignment (optional): 

/**********************************************************************
 * Describe the type parameters of your symbol table (i.e., what is   *
 * the key type and what is the value type). How did you use the      *
 * symbol table to implement the random() method.                     *
 **********************************************************************/
key is String which is the kgram
value is an int[] that tracks the frqencey of character for each kgram
The random method get the array (value) from the kgram (key) using 
the get method. Then calls Std.random.discrete with the array as the
parameter. The index returned is then typecasted to a char and that
char is returned. 

/**********************************************************************
 * The main() method we provide in the checklist does not test your   *
 * random() method. Describe how you tested it.                       *
 **********************************************************************/
I created a loop the iterated 100 times, called the random() method
inside of the loop, and every time the method return a certian character
(for example model1 returns 'b') I increased count by 1. After running
the program I checked to see if amount of times out of 100 random() 
generated a certian character was close to the expected probality.


/**********************************************************************
 * Paste two of your favorite, not too long, output fragments here.   *
 * In addition to the pseudo-random text, indicate the order of your  *
 * model and what text file you used.                                 *
 **********************************************************************/
ponyrhae; k = 0; bible.txt
tlittt; k = 0; bible.txt

/**********************************************************************
 * Did you receive help from classmates, past COS 126 students, or    *
 * anyone else?  Please list their names.  ("A Sunday lab TA" or      *
 * "Office hours on Thursday" is ok if you don't know their name.)    *
 **********************************************************************/
no

/**********************************************************************
 * Describe any serious problems you encountered.                     *
 **********************************************************************/
n/a


/**********************************************************************
 *  List any other comments here.                                     *
 **********************************************************************/

