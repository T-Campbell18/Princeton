//
// Particle
// Created by Tyler Campbell on 9/27/16.

public class Particle 
{
  public final double G = 6.67e-11;
  private double mass;
  private double x;
  private double y;
  private double vx;
  private double vy;
  private String image;
  
  public Particle(String[] part)
  {
    x = Double.parseDouble(part[0]);
    y = Double.parseDouble(part[1]);
    vx = Double.parseDouble(part[2]);
    vy = Double.parseDouble(part[3]);
    mass = Double.parseDouble(part[4]);
    image = part[5];    
  }
  
  public Particle(double a, double b, double c, double d, double e, String name)
  {
    x = a;
    y = b;
    vx = c;
    vy = d;
    mass = e;
    image = name;    
  }
  
  
  public double calcDistance(Particle a)
  {
    double deltax = a.x - x;
    double deltay = a.y - y;
    return Math.sqrt(deltax*deltax + deltay * deltay);
  }
  
  public void update(double t, double fx, double fy)
  {
    double ax = fx / mass;
    double ay = fy / mass;
    vx = vx + t * ax;
    vy = vy + t * ay; 
    x = x + t * vx;
    y = y + t * vy;
  }
  public double calcForce(Particle a)
  {
    double r = calcDistance(a);
    return (G * a.mass * mass) / (r*r);
  }
  
  public double forceX(Particle a)
  {
    double f = calcForce(a);
    double r = calcDistance(a);
    double deltax = a.x - x;
    if (r == 0)
      return 0.0;
    return f * (deltax / r);
  }
  
  public double forceY(Particle a)
  {
    double f = calcForce(a);
    double r = calcDistance(a);
    double deltay = a.y - y;
    if (r == 0)
      return 0.0;
    return f * (deltay / r);
  }
  public void draw()
  {
    StdDraw.picture(x, y, image);
  }
  
  public void drawWithAngle(double angle)
  {
   
    StdDraw.picture(x, y, image, angle);
  }
  
  public double calcAngle(double dx, double dy)
  {
    double direction = Math.atan(dy/dx);
    if (dx < 0 && dy > 0)
      direction += Math.PI;
    if (dx < 0 && dy < 0)
      direction += Math.PI;
       
    return  Math.toDegrees(direction);
  }
  
  
  public Double calcNetForceX(Particle[] part)
  {
    double force = 0.0;
    for (int x = 0; x < part.length; x++)
    {
      force += forceX(part[x]);
    }
    return force;
  }
  public Double calcNetForceY(Particle[] part)
  {
    double force = 0.0;
    for (int x = 0; x < part.length; x++)
    {
      force += forceY(part[x]);
    }
    return force;
  }
  
  public double move(Particle target)
  {
    double dx = target.x - x;
    double dy = target.y - y;
    double speed = 1500000000.0;
    if (Math.abs(dx) < 100000000 && Math.abs(dy) < 100000000)
      speed = 10000000.0;
    double direction = Math.atan(dy/dx);
    
    if (dx < 0 && dy > 0)
      direction += Math.PI;
    if (dx < 0 && dy < 0)
      direction += Math.PI; 
    x = x + (speed * Math.cos(direction));
    y = y + (speed * Math.sin(direction));
    return Math.toDegrees(direction);
  }
  
  public boolean rocketLanded(Particle planet)
  {
    if (Math.abs(x - planet.x) < 1000000000  && Math.abs(y - planet.y) < 1000000000)
      return true;
    return false;  
  }
  
  public double getMass()
  {
    return mass;
  }
  
  public double getX()
  {
    return x;
  }
  public double getY()
  {
    return y;
  }
  public double getVX()
  {
    return vx;
  }
  public double getVY()
  {
    return vy;
  }
  public String getImage()
  {
    return image;
  }
  public void printInfo()
  {
    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
    x, y, vx, vy, mass, image);
  }

}