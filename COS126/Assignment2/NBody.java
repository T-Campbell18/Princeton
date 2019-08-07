/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: Simulates the motion of n particles in the 
 * plane, mutually affected by gravitational forces, and animate the results
 * methods such as this id used to study complex physical systems
 *
 ******************************************************************************/

public class NBody
{
  public static void main(String[] args) 
  {
    // Gravitational constant
    final double G = 6.67e-11;
    double time = 0.0;
    double tao = Double.parseDouble(args[0]);
    double dt = Double.parseDouble(args[1]);
    int n = StdIn.readInt();
    StdIn.readLine();
    double radius = StdIn.readDouble();
    StdIn.readLine();
    // parallel arrays for the postion velocity and mass as well as image
    double[] xPostion = new double[n];
    double[] yPostion = new double[n];
    double[] xVelocity = new double[n];
    double[] yVelocity = new double[n];
    double[] mass = new double[n];
    String[] images = new String[n];
    // read in data using StdIn and assign to corresponding array
    for (int x = 0; x < n; x++)
    {
      xPostion[x] = StdIn.readDouble();
      yPostion[x] = StdIn.readDouble();
      xVelocity[x] = StdIn.readDouble();
      yVelocity[x] = StdIn.readDouble();
      mass[x] = StdIn.readDouble();
      images[x] = StdIn.readString();
      StdIn.readLine();
    }
    StdDraw.setScale(-radius, radius);
    StdAudio.loop("space.mid");
    StdDraw.enableDoubleBuffering();
    // draws the planets and updates their values until time reaches tao
    while (time < tao)
    {
      // draw backgroud
      StdDraw.picture(0, 0, "starfield.jpg");
      double[] xForces = new double[n];
      double[] yForces = new double[n];
      for (int x = 0; x < n; x++)
      {
        double netForceX = 0.0;
        double netForceY = 0.0;
        for (int a = 0; a < n; a++)
        {
          if (x == a)
            continue;
          // calculate force
          double deltax = xPostion[a] - xPostion[x];
          double deltay = yPostion[a] - yPostion[x];
          double r = Math.sqrt(deltax * deltax + deltay * deltay);
          double f = (G * mass[x] * mass[a]) / (r*r);
          double fx = f * (deltax / r);
          double fy = f * (deltay / r);
          // calculate net force by adding all forces
          netForceX += fx;
          netForceY += fy;
          xForces[x] = netForceX;
          yForces[x] = netForceY;
        }
      }
      // draw each planet
      for (int x = 0; x < n; x++)
      {
        StdDraw.picture(xPostion[x], yPostion[x], images[x]);
      }
      double[] tempXPostions = new double[n];
      double[] tempYPostions = new double[n];
      // find new postion based on forces
      for (int x = 0; x < n; x++)
      {
        double ax = xForces[x] / mass[x];
        double ay = yForces[x] / mass[x];
        double vx = xVelocity[x] + dt * ax;
        double vy = yVelocity[x] + dt * ay; 
        double rx = xPostion[x] + dt * vx;
        double ry = yPostion[x] + dt * vy;
        xVelocity[x] = vx;
        yVelocity[x] = vy;
        tempXPostions[x] = rx;
        tempYPostions[x] = ry;
      }
      xPostion = tempXPostions;
      yPostion = tempYPostions;
      StdDraw.show();
      StdDraw.pause(20);
      time += dt;
    } 
    // print final info of planets 
    StdOut.println(n + "\n" + radius);
    for (int x = 0; x < n; x++)
    {
      StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
      xPostion[x], yPostion[x], xVelocity[x], yVelocity[x], mass[x], images[x]);
    }
  }
}