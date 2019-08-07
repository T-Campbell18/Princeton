/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P02
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: 
 *
 ******************************************************************************/

public class NBody2 
{
  public static void main(String[] args) 
  {
    final int VALUES = 6;
    int targetPlanet = 1;
    double time = 0.0;
    double tao = Double.parseDouble(args[0]);
    double dt = Double.parseDouble(args[1]);
    int n = StdIn.readInt();
    StdIn.readLine();
    double radius = StdIn.readDouble();
    StdIn.readLine();
    String[][] particles = new String[n][VALUES];
    for (int x = 0; x < n; x++)
    {
      for (int y = 0; y < VALUES; y++)
      {
        particles[x][y] = StdIn.readString();
      }
      StdIn.readLine();
    }
    Particle[] part = new Particle[n];
    for (int x = 0; x < n; x++)
    {
      part[x] = new Particle(particles[x]);
    }
    StdDraw.setScale(-radius, radius);
    StdAudio.loop("space.mid");
    Particle rocket = new Particle(part[0].getX(), part[0].getY(), 0, 0, 10000, "rocket.png");
    
    while(time < tao)
    {
      
      StdDraw.picture(.5, .5, "starfield.jpg");
      double[] xForces = new double[n];
      double[] yForces = new double[n];
      for (int x = 0; x < n; x++)
      {
        xForces[x] = part[x].calcNetForceX(part);
        yForces[x] = part[x].calcNetForceY(part);
      }
      for (int x = 0; x < n; x++)
      {
        part[x].draw();
      }
      for(int x = 0; x < n; x++)
      {
        part[x].update(dt, xForces[x], yForces[x]);
      }
      
      if(rocket.rocketLanded(part[targetPlanet]))
      {
        targetPlanet++;
        if (targetPlanet == 3)
          targetPlanet++;
        if(targetPlanet == 5)
          targetPlanet = 0;
      }
         
      double angle = rocket.move(part[targetPlanet]) + -90;
      rocket.drawWithAngle(angle);
      StdDraw.show(20);
      time += dt;
    }
    StdOut.println(n + "\n" + radius);
    for(Particle planet : part)
    {
      planet.printInfo();
    }
  }
}
