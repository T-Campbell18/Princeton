/******************************************************************************
*  Name: Tyler Campbell
*  NetID: tylercc
*  Precept: P02 
*
*  Partner Name: n/a  
*  Partner NetID: n/a 
*  Partner Precept: n/a
* 
* Description: craetes a data type to represent a Markov model of 
* order k from a given text string.
*  
*
******************************************************************************/

  public class MarkovModel
  {
    // constant to represent number of ASCII values
    private static final int ASCII = 128;
    // Symbol table with key String (kgram), value is array of int that 
    // tracks the frqencey of character for each kgram
    private ST<String, int[]> st;
    private int order; // the korder of the kgram
    
    // creates a Markov model of order k for the specified text
    public MarkovModel(String input, int k)
    {
      order = k;
      String inputText = input;
      String cirularText = inputText + inputText.substring(0, order);
      st = new ST<String, int[]>();
      for (int x = 0; x < inputText.length(); x++)
      {
        String text = cirularText.substring(x, x + order);
        char nextChar = cirularText.charAt(x + order);
        
        if (!st.contains(text))
        {
          int[] temp = new int[ASCII];
          for (int a = 0; a < ASCII; a++)
          {
            if (a == (int) nextChar)
              temp[a] = 1;
            else 
              temp[a] = 0;
            st.put(text, temp);
          }
        }
        else 
          st.get(text)[nextChar]++;
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
      if (kgram.length() != order)
        throw new IllegalArgumentException("kgram is not of length k");

      int f = 0;
      if (st.contains(kgram))
      {
        int[] temp = st.get(kgram);
        for (int x = 0; x < ASCII; x++)
        {
          f += temp[x];
        }
      }
      return f;
    }

    // returns the number of times the character c follows the specified
    // kgram in the text
    public int freq(String kgram, char c)
    {
      if (kgram.length() != order)
        throw new IllegalArgumentException("kgram is not of length k");

      if (!st.contains(kgram)) 
        return 0;
        
      return st.get(kgram)[c];
    }

    // returns a random character that follows the specified kgram in the text,
    // chosen with weight proportional to the number of times that character
    // follows the specified kgram in the text
    public char random(String kgram)
    {
      if (kgram.length() != order)
        throw new IllegalArgumentException("kgram is not of length k");
      if (!st.contains(kgram))
        throw new IllegalArgumentException("kgram does not appear in the text"); 
      int[] temp = st.get(kgram);
      return (char) StdRandom.discrete(temp);
    }
    
    // returns a string representation of the Markov model
    public String toString()
    {
      String rep = "";
      for (String key : st)
      {
        int[] temp = st.get(key);
        rep += key + ": ";
        for (int x = 0; x < ASCII; x++)
        {
          if (temp[x] != 0)
          {
            char c = (char) x;
            int f = temp[x];
            rep += c + " "+ f + " ";
          }
        }
        rep += "\n";
      }
      
      return rep;
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
        int t = 0;
        int z = 0;
        for (int a = 0; a < 100; a++)
        {
          String b = model1.random("na") + "";
          if (b.equals("b"))
            t++;
          String c = model3.random("fish") + "";
          if (c.equals("o"))
            z++;
        }
        StdOut.println(t + " " + z);
    }
  }