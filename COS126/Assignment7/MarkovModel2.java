/******************************************************************************
 *  Name: Tyler Campbell   
 *  NetID: tylercc
 *  Precept: P02 
 *
 *  Partner Name: n/a  
 *  Partner NetID: n/a 
 *  Partner Precept: n/a
 * 
 * Description: creates a data type to model a vibrating guitar string
 *  
 *
 ******************************************************************************/

public class MarkovModel2 
{
  private ST<String, ST<Character, Integer>> st; // !!
  private int order; // !!
  
  // creates a Markov model of order k for the specified text
  public MarkovModel2(String input, int k)
  {
    order = k;
    String inputText = input;
    String cirularText = inputText + inputText.substring(0, order);
    st = new ST<String, ST<Character, Integer>>();
    for (int x = 0; x < inputText.length(); x++)
    {
      String text = cirularText.substring(x, x + order);
      char nextChar = cirularText.charAt(x + order);
      
      if (!st.contains(text))
      {
        ST<Character, Integer> temp = new ST<Character, Integer>();
        for (int a = 0; a < 128; a++)
        {
          if (a == (int) nextChar)
          {
            temp.put(nextChar, 1);
          }
          else 
          {
            temp.put((char) a, 0);
          }
          st.put(text, temp);
        }
      }
      else 
      {
        st.get(text).put(nextChar, st.get(text).get(nextChar)+1);
      }
    }
    
  }

  // returns the order k of this Markov model
  public int order()
  {
    return order;
  }

  // returns the number of times the specified kgram appears in the text
  public int freq(String kgram)
  {
    int f = 0;
    if (st.contains(kgram))
    {
      ST<Character, Integer> temp = st.get(kgram);
      for (char c : temp.keys())
      {
        f += temp.get(c);
      }
    }
    return f;
  }

  // returns the number of times the character c follows the specified
  // kgram in the text
  public int freq(String kgram, char c)
  {
    if (!st.contains(kgram)) 
      return 0;
      
    return st.get(kgram).get(c);
  }

  // returns a random character that follows the specified kgram in the text,
  // chosen with weight proportional to the number of times that character
  // follows the specified kgram in the text
  public char random(String kgram)
  {
    ST<Character, Integer> temp = st.get(kgram);
    double[] probability = new double[128];
    int f = freq(kgram);
    double sum = 0;
    for (char c : temp.keys()) 
    {
      double prob = 1.0 * freq(kgram, c) / f;
      probability[c] = prob;
      sum += prob;
    }
    char randomCharacter = (char) StdRandom.discrete(probability);
    
    return randomCharacter;
  }
  
  // returns a string representation of the Markov model
  public String toString()
  {
    return "";
  }
  
  // unit tests all methods in this class
  public static void main(String[] args) 
  {
      String text1 = "banana";
      MarkovModel model1 = new MarkovModel(text1, 2);
      StdOut.println("freq(\"an\", 'a')    = " + model1.freq("an", 'a'));
      StdOut.println("freq(\"na\", 'b')    = " + model1.freq("na", 'b'));
      StdOut.println("freq(\"na\", 'a')    = " + model1.freq("na", 'a'));
      StdOut.println("freq(\"na\")         = " + model1.freq("na"));
      StdOut.println();

      String text2 = "one fish two fish red fish blue fish"; 
      MarkovModel model3 = new MarkovModel(text2, 4);
      StdOut.println("freq(\"ish \", 'r') = " + model3.freq("ish ", 'r'));
      StdOut.println("freq(\"ish \", 'x') = " + model3.freq("ish ", 'x'));
      StdOut.println("freq(\"ish \")      = " + model3.freq("ish "));
      StdOut.println("freq(\"tuna\")      = " + model3.freq("tuna"));
//      int t = 0;
//      int z = 0;
//      for (int a = 0; a < 100; a++)
//      {
//        String b = model1.random("na") + "";
//        if (b.equals("b"))
//          t++;
//        String c = model3.random("fish") + "";
//        if (c.equals("o"))
//          z++;
//      }
//      StdOut.println(t + " " + z);
  }
}