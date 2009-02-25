import java.util.Iterator;

public class TableIterator implements Iterator {

  private int index = 0;
     
  private KeyedItem[] items;
  private int numItems;
     
  public TableIterator(KeyedItem[] items, int numItems)
  { 
    this.items = items;
    this.numItems = numItems;
  }
 
  public boolean hasNext()
  {
    return index < this.numItems;
  }
 
  public Object next()
  { 
    return this.items[this.index++];
  }
 
  public void remove()
  {  
    // You are not required to implement this method.
  }
  
}
