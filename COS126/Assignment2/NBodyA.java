/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: Simulates Nbody
 *
 ******************************************************************************/

public class NBodyA
{
  public static void main(String[] args) 
  {
    final int VALUES = 6;
    final double G = 6.67e-11;
    double time = 0.0;
    double tao = Double.parseDouble(args[0]);
    double dt = Double.parseDouble(args[1]);
    int n = StdIn.readInt();
    StdIn.readLine();
    double radius = StdIn.readDouble();
    StdIn.readLine();
    double[][] particles = new double[n][VALUES];
    String[] images = new String[n];
    for (int x = 0; x < n; x++)
    {
      for (int y = 0; y < VALUES; y++)
      {
        if (y == 5)
          images[x] = StdIn.readString();
        else
          particles[x][y] = StdIn.readDouble();
      }
      StdIn.readLine();
    }
    StdDraw.setScale(-radius, radius);
    StdAudio.loop("space.mid");
    StdDraw.enableDoubleBuffering();
    
    while (time < tao)
    {
      StdDraw.picture(0.5, 0.5, "starfield.jpg");
      double[] xForces = new double[n];
      double[] yForces = new double[n];
      for (int x = 0; x < n; x++)
      {
        double netForceX = 0.0;
        double netForceY = 0.0;
        for (int a = 0; a < n; a++)
        {
          double[] planet = particles[x];
          double deltax = particles[a][0] - planet[0];
          double deltay = particles[a][1] - planet[1];
          double r = Math.sqrt(deltax * deltax + deltay * deltay);
          double f = (G * planet[4] * particles[a][4]) / (r * r);
          double fx = f * (deltax / r);
          double fy = f * (deltay / r);
          if (r == 0)
          {
            fx = 0;
            fy = 0;
          }
          netForceX += fx;
          netForceY += fy;
          xForces[x] = netForceX;
          yForces[x] = netForceY;
        }
        
      }
      
      for (int x = 0; x < n; x++)
      {
        double[] planets = particles[x];
        StdDraw.picture(planets[0], planets[1], images[x]);
      }
      double[][] temp = particles;
      for (int x = 0; x < n; x++)
      {
        double[] planets = particles[x];
        double ax = xForces[x] / planets[4];
        double ay = yForces[x] / planets[4];
        planets[2] = planets[2] + dt * ax;
        planets[3] = planets[3] + dt * ay; 
        planets[0] = planets[0] + dt * planets[2];
        planets[1] = planets[1] + dt * planets[3];
        temp[x] = planets;
      }
      particles = temp;
      StdDraw.show();
      StdDraw.pause(20);
      time += dt;
      System.out.println(xForces[0] + " " + xForces[1]);
    } 
     
    StdOut.println(n + "\n" + radius);
    for (int x = 0; x < n; x++)
    {
      double[] planet = particles[x];
      StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
      planet[0], planet[1], planet[2], planet[3], planet[4], images[x]);
    }
  }
}