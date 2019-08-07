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

public class GuitarString 
{
    private static final int SAMPLERATE = 44100; // constant for sample rate
    private RingBuffer guitar; // Ring Buffer for the Guitar String class
    
    // creates a guitar string of the specified requency,
    // using sampling rate of 44,100
    public GuitarString(double frequency) 
    {
        int n = (int) (Math.ceil(SAMPLERATE / frequency));
        guitar = new RingBuffer(n);
        for (int x = 0; x < n; x++)
        {
            guitar.enqueue(0);
        }
    }

    // creates a guitar string whose size and initial values are given by
    // the specified array
    public GuitarString(double[] init) 
    {
        guitar = new RingBuffer(init.length);
        for (int x = 0; x < init.length; x++)
        {
            guitar.enqueue(init[x]);
        }
    }
    
    //  returns the number of samples in the ring buffer
    public int length()
    {
        return guitar.size();
    }
    
    // plucks the guitar string (by replacing the buffer with white noise)
    public void pluck() 
    {
        for (int x = 0; x < guitar.size(); x++)
        {
            guitar.dequeue();
            guitar.enqueue(StdRandom.uniform(-0.5, 0.5));
        }
    }

    // advances the Karplus-String simulation one time step
    public void tic() 
    {
        double a = guitar.dequeue();
        guitar.enqueue(0.996 * ((guitar.peek() + a) / 2));
    }

    // returns the current sample
    public double sample() 
    {
        return guitar.peek();
    }


    // unit tests this class
    public static void main(String[] args) 
    {
        double[] samples = { 0.2, 0.4, 0.5, 0.3, -0.2, 0.4, 0.3, 0.0, -0.1, -0.3 };  
        GuitarString testString = new GuitarString(samples);
        int m = 25; // 25 tics
        for (int i = 0; i < m; i++) 
        {
            double sample = testString.sample();
            System.out.printf("%6d %8.4f\n", i, sample);
            testString.tic();
        }
        testString.pluck();
        for (int i = 0; i < m; i++) 
        {
            double sample = testString.sample();
            System.out.printf("%6d %8.4f\n", i, sample);
            testString.tic();
        }
        System.out.println(testString.length());
        
    }

}
