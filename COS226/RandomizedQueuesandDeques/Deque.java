/******************************************************************************
 * Name:    Tyler Campbell
 * NetID:   tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: Creates a generic data type Deque, that is a generalization of 
 * a stack and a queue that supports adding and removing items from either the 
 * front or the back of the data structure
 *
 *
 ******************************************************************************/
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> 
{
  private Node<Item> first; // reference to first node of deque
  private Node<Item> last; // // reference to last node of deque
  private int size; // number of elements in deque
  
  // construct an empty deque
  public Deque()
  {
    first = null;
    last = null;
  }
  
  // is the deque empty?                           
  public boolean isEmpty()
  {
    return size == 0;  
  }
  
  // return the number of items on the deque
  public int size()
  {
    return size;
  }
  
  // add the item to the front                
  public void addFirst(Item item)
  {
    if (item == null)
      throw new NullPointerException();
    Node<Item> temp = first;
    first = new Node<>(item);
    if (isEmpty()) 
      last = first;
    else 
    {
      first.next = temp;
      temp.previous = first;
    }
    size++;
  }
  
  // add the item to the end        
  public void addLast(Item item)
  {
    if (item == null) 
      throw new java.lang.NullPointerException();
    Node<Item> temp = last;
    last = new Node<>(item);
    if (isEmpty()) 
      first = last;
    else 
    {
      temp.next = last;
      last.previous = temp;
    }
    size++;
  }
  
  // remove and return the item from the front        
  public Item removeFirst() 
  {
    if (isEmpty()) 
      throw new java.util.NoSuchElementException();
    Item temp = first.item;
    if (size() == 1)
    {
      first = null;
      last = null;
    }
    else 
    {
      first = first.next;
      first.previous = null;
    }
    size--;
    return temp;
  }
  // remove and return the item from the end               
  public Item removeLast()
  {
    if (isEmpty()) 
      throw new java.util.NoSuchElementException();
    Item temp = last.item;
    if (size() == 1)
    {
      first = null;
      last = null;
    }
    else 
    {
      last = last.previous;
      last.next = null;
    }
    size--;
    return temp;
  } 
  
  // return an iterator over items in order from front to end
  public Iterator<Item> iterator()
  {
    return new DequeIterator();
  }
  
  // class for Iterator
  private class DequeIterator implements Iterator<Item> 
  {
    private Node<Item> current = first; // current node of Iterator, starts a first
    // returns true if the current node has a next node
    public boolean hasNext() 
    {
      return current != null;
    }
    // throws Unsupported Operation Exception
    public void remove() 
    {
      throw new java.lang.UnsupportedOperationException();
    }
    // returns the current item
    public Item next() 
    {
      if (!hasNext()) 
        throw new java.util.NoSuchElementException();
      Item temp = current.item;
      current = current.next;
      return temp;
    }
  }
  // nested Node class
  private class Node<Item> 
  {
    private Item item; // Item of node
    private Node<Item> next; // the next node in linked list 
    private Node<Item> previous; // the previous node in linked list
    
    private Node(Item item) 
    {
      this.item = item;
      next = null;
      previous = null;
    }
  }
  // unit testing (required)       
  public static void main(String[] args)
  {
    Deque<String> d = new Deque<String>();
    d.addFirst("C");
    d.addFirst("Tyler");
    d.addLast("Campbell");
    d.addFirst("The");
    d.addLast("!!!");
    for (String a : d)
      System.out.println(a);
    System.out.println();
    d.removeFirst();
    d.removeLast();
    for (String a : d)
      System.out.println(a);
  }   
}