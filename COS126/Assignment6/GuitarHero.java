/******************************************************************************
 *  Name: Tyler Campbell     
 *  NetID: tylercc 
 *  Precept:  P02
 * 
 *  Partner Name: n/a  
 *  Partner NetID: n/a   
 *  Partner Precept: n/a
 *
 *  Description: simulates a keyboard that supports a total of 37 notes 
 *  on the chromatic scale from 110 Hz to 880 Hz. Plays note when user          
 *  presses keyboard
 *
 ******************************************************************************/
public class GuitarHero 
{
  public static void main(String[] args) 
  {
    final double CONCERT_A = 440.0;
    String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    GuitarString[] guitar = new GuitarString[keyboard.length()];
    for (int x = 0; x < guitar.length; x++)
    {
      GuitarString note = new GuitarString(CONCERT_A * Math.pow(2, (x-24)/12.0));
      guitar[x] = note;
    }
    
    
    // the main input loop
    while (true) 
    {

      // check if the user has typed a key, and, if so, process it
      if (StdDraw.hasNextKeyTyped()) 
      {
         
        // the user types this character
        char key = StdDraw.nextKeyTyped();
        if (keyboard.indexOf(key) != -1)
        {
           int a = keyboard.indexOf(key);
           guitar[a].pluck();
        }  

      }        

    // compute the superposition of the samples
    double sample = 0;
    for (int x = 0; x < guitar.length; x++)
    {
      sample += guitar[x].sample();
    }

    // send the result to standard audio
    StdAudio.play(sample);

    // advance the simulation of each guitar string by one step
    for (int x = 0; x < guitar.length; x++)
    {
      guitar[x].tic();
    }
    }    
  }
}