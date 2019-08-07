/******************************************************************************
 *  Name: Tyler Campbell  
 *  NetID: tylercc  
 *  Precept: P02
 *
 *  Partner Name: n/a    
 *  Partner NetID: n/a   
 *  Partner Precept: n/a 
 * 
 *  Description: creates a data type to model the ring buffer
 *
 *
 ******************************************************************************/
import java.util.Arrays;

public class RingBuffer 
{
    public double[] buffer; // array that keeps items of Ring Buffer
    private int front; // index for front of buffer, first item
    private int back; // index for back of buffer, last item 
    private int num; // number of items in buffer

    // creates an empty ring buffer with the specified capacity
    public RingBuffer(int capacity) 
    {
        buffer = new double[capacity];
        num = 0;
        front = 0;
        back = -1;
    }

    // return the capacity of this ring buffer
    public int capacity() 
    {
        return buffer.length;
    }

    // return number of items currently in this ring buffer
    public int size() 
    {
        return num;
    }

    // is this ring buffer empty (size equals zero)?
    public boolean isEmpty() 
    {
        return num == 0;
    }

    // is this ring buffer full (size equals capacity)?
    public boolean isFull() 
    {
       return num == buffer.length;
    }

    // adds item x to the end of this ring buffer
    public void enqueue(double x) 
    {
        if (num == buffer.length)
            throw new RuntimeException("Ring buffer is full");
        
        back = (back + 1) % buffer.length;
        buffer[back] = x;
        num++;
    }

    // deletes and returns the item at the front of this ring buffer
    public double dequeue() 
    {
        if (isEmpty())
            throw new RuntimeException("Ring buffer is empty");
        double x = buffer[front];
        num--;
        front = (front + 1) % buffer.length;
        return x;
    }

    // returns the item at the front of this ring buffer
    public double peek() 
    {
        if (isEmpty())
            throw new RuntimeException("Ring buffer is empty");
        return buffer[front];
    }

    // unit tests this class
    public static void main(String[] args) 
    {
        int n = Integer.parseInt(args[0]);
        RingBuffer buffer = new RingBuffer(n);
        System.out.println(buffer.isEmpty());  
        for (int i = 1; i <= n; i++) 
        {
            buffer.enqueue(i);
        }
        System.out.println(buffer.isFull());
        double t = buffer.dequeue();
        buffer.enqueue(t);
        System.out.println("Size after wrap-around is " + buffer.size());
        System.out.println(Arrays.toString(buffer.buffer) + buffer.size());
        while (buffer.size() >= 2) 
        {
            double x = buffer.dequeue();
            double y = buffer.dequeue();
            buffer.enqueue(x + y);
            System.out.println(Arrays.toString(buffer.buffer) + buffer.size());
        }
        System.out.println(buffer.peek());
        System.out.println(buffer.capacity());
        
        
    }
}
