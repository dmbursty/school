import junit.framework.TestCase;
import java.util.Iterator;
import java.util.ArrayList;

public class TableIteratorTest extends TestCase {

  public void testGetIterator() { 
  // Input:  iterator for a table containing 25 items with Integer keys
  // Test:   use of hasNext() and next() to iterate through all items
  // Result: each item visited exactly once 

    TableInterface t = new TableUnorderedArray();
    ArrayList<KeyedItem> keyedItems = new ArrayList<KeyedItem>();

    for(int i=0; i<25; i++) {  
      KeyedItem k = new KeyedItem(new Integer(i));
      t.insert(k);
      keyedItems.add(k);
    }

    Iterator it = t.getIterator();
    while (it.hasNext()) {  
      KeyedItem k = (KeyedItem)it.next();
      keyedItems.remove(k);
    }

    assertEquals(true, keyedItems.isEmpty());
   
  }
 
}
