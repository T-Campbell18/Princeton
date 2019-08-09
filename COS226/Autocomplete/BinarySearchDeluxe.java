/******************************************************************************
 * Name: Tyler Campbell
 * NetID: tylercc
 * Precept: P04
 *
 * Partner Name:    N/A
 * Partner NetID:   N/A
 * Partner Precept: N/A
 * 
 * Description: binary searching a sorted array that returns either the first
 * or last index of a key if the key is in the array multiple times 
 * 
 ******************************************************************************/
import java.util.Comparator;
import java.util.Arrays;

public class BinarySearchDeluxe 
{
  // Returns the index of the first key in a[] that equals
  // the search key, or -1 if no such key.
  public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator)
  {
    if (a == null || key == null || comparator == null)
      throw new NullPointerException();
    if (a.length == 0)
      return -1;
    int lo = 0;
    int hi = a.length -1;
    int mid = 0;  
    while (lo <= hi)
    {
      mid = lo + (hi - lo) / 2;
      int compare = comparator.compare(key, a[mid]);
      if (compare < 0)
        hi = mid - 1;
      else if (compare > 0)
        lo = mid + 1;
      else if (lo == mid)
        return mid;
      else
        hi = mid;
    }
    return -1;  
  }

  // Returns the index of the last key in a[] that equals the 
  // search key, or -1 if no such key.
  public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator)
  {
    if (a == null || key == null || comparator == null)
      throw new NullPointerException();
    if (a.length == 0)
      return -1;
    int lo = 0;
    int hi = a.length -1;
    while (lo <= hi)
    {
      int mid = lo + (hi + 1 - lo) / 2;
      int compare = comparator.compare(key, a[mid]);
      if (compare < 0)
        hi = mid - 1;
      else if (compare > 0)
        lo = mid + 1;
      else if (hi == mid)
        return mid;
      else
        lo = mid;
    }
    return -1;
  }
  

  // unit testing (required)
  public static void main(String[] args)
  {
    // create array
    String[] a = new String[26];
    int in = 0;
    // fill array with lowercase letters more "c" and "t" no Uppercase leters
    for (int z = 97; z <= 122; z++)
    {
      if (in >= 7 && in <= 12)
      {
        a[in] = "c";
        in++;
        continue;
      }
      if (in >= 18 && in <= 21)
      {
        a[in] = "t";
        in++;
        continue;
      }
      a[in] = Character.toString((char) z);
      in++;
    }
    // sort array
    Arrays.sort(a);
    // test search methods
    int x = BinarySearchDeluxe.firstIndexOf(a, "c", String::compareTo);
    int y = BinarySearchDeluxe.lastIndexOf(a, "c", String::compareTo);
    System.out.println("Original array: " + Arrays.toString(a));
    System.out.println("First index of c: " + x);
    System.out.println("Last index of c: " + y);
    x = BinarySearchDeluxe.firstIndexOf(a, "t", String::compareTo);
    y = BinarySearchDeluxe.lastIndexOf(a, "t", String::compareTo);
    System.out.println("First index of t: " + x);
    System.out.println("Last index of t: " + y);
    x = BinarySearchDeluxe.firstIndexOf(a, "T", String::compareTo);
    y = BinarySearchDeluxe.lastIndexOf(a, "C", String::compareTo);
    System.out.println("T is not in array: " + x);
    System.out.println("C is not in array: " + y);
  }  
}