/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: Creates a generic data type RandomizedQueue, that is similar to 
 * a stack or queue, except that the item removed is chosen uniformly at 
 * random from items in the data structure.  
 *
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> 
{
  private Item[] queue; // array of Item in queue
  private int size; // number of elements in queue
  
  // construct an empty randomized queue
  public RandomizedQueue()
  {
    queue = (Item[]) new Object[1];
  } 
  // is the queue empty?              
  public boolean isEmpty()
  {
    return size == 0;
  }
  // return the number of items on the queue                
  public int size()
  {
    return size;
  }
  // add the item                        
  public void enqueue(Item item)
  {
    if (item == null)
      throw new java.lang.NullPointerException();
    queue[size] = item;
    size++;
    if (size == queue.length) 
      resize(queue.length * 2);
  }
  // remove and return a random item         
  public Item dequeue()
  {
    if (isEmpty())
      throw new java.util.NoSuchElementException();
    int ran = StdRandom.uniform(size);
    Item temp = queue[ran];
    size--;
    queue[ran] = queue[size];
    queue[size] = null;
    if (size == queue.length / 4) 
      resize(queue.length / 2);
    return temp;
  }
  // return a random item (but do not remove it)                   
  public Item sample()
  {
    if (isEmpty()) 
      throw new java.util.NoSuchElementException();
    return queue[StdRandom.uniform(size)];
  }
  // changes the size of array to the given new size and fills in the items
  private void resize(int newSize) 
  {
    Item[] temp = (Item[]) new Object[newSize];
    for (int x = 0; x < size; x++)
      temp[x] = queue[x];
    queue = temp;
  }
  // return an independent iterator over items in random order                    
  public Iterator<Item> iterator()
  {
    return new RandomIterator<Item>(queue);
  }
  
  // class for Iterator 
  private class RandomIterator<Item> implements Iterator<Item>
  {
    private RandomizedQueue<Item> iter = new RandomizedQueue<Item>();
    // constructs RandomizedQueue by filling it with giving queue
    public RandomIterator(Item[] queue)
    {
      for (Item item : queue)
      {
        if (item == null)
          break;
        iter.enqueue(item);  
      }
    }
    // Returns true if the iteration has more elements
    public boolean hasNext() 
    {
      return !iter.isEmpty();
    }
    // Returns the next element in the iteration
    public Item next() 
    {
      if (iter.isEmpty()) 
        throw new java.util.NoSuchElementException();
      return iter.dequeue();
    }
    // throws Unsupported Operation Exception
    public void remove() 
    {
      throw new UnsupportedOperationException();
    }
  }
  
  // unit testing (required) 
  public static void main(String[] args)
  {
    RandomizedQueue<String> rq = new RandomizedQueue<String>();
    rq.enqueue("My");
    rq.enqueue("name");
    rq.enqueue("is");
    rq.enqueue("Tyler");
    rq.enqueue("C");
    rq.enqueue("Campbell");
    for (String a : rq)
      System.out.println(a);
  }   
}