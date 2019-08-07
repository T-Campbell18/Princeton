/******************************************************************************
 *  Name: Tyler Campbell   
 *  NetID: tylercc
 *  Precept: P02 
 *
 *  Partner Name: n/a  
 *  Partner NetID: n/a 
 *  Partner Precept: n/a
 * 
 * Description: reads the radial displacements (r1, r2, r3, ...) from 
 * standard input and estimates Boltzmann's constant and Avogadro's number 
 * using the given formulas
 *  
 *
 ******************************************************************************/

public class Avogadro
{
  public static void main(String[] args) 
  {
    // constants and conversions
    int ABSOLUTE_TEMP = 297;
    double VISCOSITY_WATER = 9.135e-4;
    double METERS_PER_PIXEL = 0.175e-6;
    double RADIUS_BEAD = 0.5e-6;
    double GAS_CONSTANT = 8.31446;
    // nummber of beads and total displacement 
    int x = 0;
    double distance = 0.0;
    // reads all the radial displacements
    // keeps total displacement and number of beads
    while (!StdIn.isEmpty())
    {
      x++;
      distance += Math.pow(StdIn.readDouble() * METERS_PER_PIXEL, 2);
    }
    // calculates Boltzmann and Avogadro
    double d =  distance / (2 * x); 
    double k = d * 6 * Math.PI * VISCOSITY_WATER * RADIUS_BEAD / ABSOLUTE_TEMP;
    double na = GAS_CONSTANT / k;
    // prints Boltzmann and Avogadro
    StdOut.printf("Boltzmann = %.4e\n", k);
    StdOut.printf("Avogadro  = %.4e\n", na);
  }
}