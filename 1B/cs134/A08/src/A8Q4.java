
import java.util.Iterator;

public class A8Q4 {

  //pre:  t1 and t2 are not null
  //post: returns the number of keys k such that
  //      t1 contains an item with key k1 where k1.compareTo(k) == 0 and 
  //      t2 contains an item with key k2 where k2.compareTo(k) == 0
  public static int intersection(TableInterface t1, TableInterface t2) {
    int count = 0;
    Iterator i1 = t1.getIterator();
    while (i1.hasNext())
    {
      Comparable current = ((KeyedItem)i1.next()).getKey();
      
      Iterator i2 = t2.getIterator();
      
      while (i2.hasNext())
      {
        if (current.compareTo(((KeyedItem)i2.next()).getKey()) == 0) count++;
      }
    }
    
    return count;
    
  }

}
