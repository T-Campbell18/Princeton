/******************************************************************************
 *  Name: Tyler Campbell   
 *  NetID: tylercc
 *  Precept: P02 
 *
 *  Partner Name: n/a  
 *  Partner NetID: n/a 
 *  Partner Precept: n/a
 * 
 * Description: takes an integer min, a double value tau, a double value delta, 
 * and a sequence of image filenames as command-line arguments; identifies the
 * beads (using the specified values for min and tau) in each image 
 * (using BeadFinder); and prints the distance that each bead moves from one 
 * frame to the next (assuming that distance is no longer than delta).
 *
 *
 ******************************************************************************/

public class BeadTracker 
{
  public static void main(String[] args) 
  {
    // read command line args
    int min = Integer.parseInt(args[0]);
    double tau = Double.parseDouble(args[1]);
    double delta = Double.parseDouble(args[2]);
    // create BindFiner objects
    BeadFinder find1 = null;
    BeadFinder find2 = null;
    // loops through all pictures except last one starting at first picture
    for (int t = 3; t < args.length - 1; t++)
    {
      // read in current picture and next picture
      find1 = new BeadFinder(new Picture(args[t]), tau);
      find2 = new BeadFinder(new Picture(args[t+1]), tau);
      Blob[] beads1 = find1.getBeads(min);
      Blob[] beads2 = find2.getBeads(min);
      // calculates the closest bead
      // prints if closest bead is less than or equal to delta
      for (int x = 0; x < beads2.length; x++) 
      {
        double low = Double.POSITIVE_INFINITY;
        for (int y = 0; y < beads1.length; y++) 
        {
          double distance = beads2[x].distanceTo(beads1[y]);
          if (distance <= low) 
            low = distance;
        }
        if (low <= delta) 
          StdOut.printf(" %.4f\n", low);
      }
    }
  }
}