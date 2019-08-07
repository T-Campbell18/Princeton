/******************************************************************************
*  Name: Tyler Campbell
*  NetID: tylercc
*  Precept: P02 
*
*  Partner Name: n/a  
*  Partner NetID: n/a 
*  Partner Precept: n/a
* 
* Description: takes two integer command-line arguments k and T; reads the 
* input text from standard input; builds a Markov model of order k from the 
* input text; then, starting with the k-gram consisting of the first k 
* characters of the input text, prints T characters generated by simulating 
* a trajectory through the corresponding Markov chain.
*   
*
******************************************************************************/

public class TextGenerator
{
  public static void main(String[] args) 
  {
    // read in k and t agruments and assign text from input
    int k = Integer.parseInt(args[0]);
    int t = Integer.parseInt(args[1]);
    String text = StdIn.readAll();
    // create MarkobModel using text and k
    MarkovModel model = new MarkovModel(text, k);
    String kgram = text.substring(0, k);
    StringBuilder build = new StringBuilder(kgram);
    // print intial kgram
    StdOut.print(kgram);
    // repeadlty prints random character and updates k gram
    // to hold the last k characters printed
    for (int x = 0; x < t - k; x++)
    {
      char randChar = model.random(kgram);
      StdOut.print(randChar);
      build.append(randChar);
      kgram = build.substring(build.length()-k);
    }
    StdOut.println();
  }
}