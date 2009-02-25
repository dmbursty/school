import java.util.Iterator;

public class TableIterator implements Iterator {
  
  public KeyedItem[] items;
  public int numItems;
  public int current;
 
  public TableIterator(KeyedItem[] items, int numItems)
  { 
    this.items = items;
    this.numItems = numItems;
    this.current = -1;
  }

  //post: returns true if the iteration has more elements
  public boolean hasNext()
  {
    return current != numItems;
  }

  //pre:  this.hasNext() == true
  //post: returns the next element in the iteration 
  public Object next()
  {
    current++;
    return items[current];
  }
 
  public void remove()
  {  
    // You are not required to implement this method.
  }
  
}
