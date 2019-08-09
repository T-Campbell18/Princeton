import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import java.util.LinkedList;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;


public class TimerForKdTrees {
    public static void main(String args[]) {
        PointST<Integer> pointHolder = new PointST<Integer>();
        while (!StdIn.isEmpty()) {
        double xValue = StdIn.readDouble();
        double yValue = StdIn.readDouble();
        Point2D point = new Point2D(xValue, yValue);
        pointHolder.put(point, 3);
        }
         Stopwatch stopWatch = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
        double x = StdRandom.uniform();
        double y = StdRandom.uniform();
        pointHolder.nearest(new Point2D(x, y));
        }
        System.out.println(stopWatch.elapsedTime());
        System.out.println(1000 / stopWatch.elapsedTime());
    }
}