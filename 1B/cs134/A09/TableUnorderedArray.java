public class TableUnorderedArray implements TableInterface {
 
  public static final int INITIAL_SIZE = 10;
 
  private KeyedItem[] items;
  private int numItems;
 
 
  public TableUnorderedArray() {
    this.numItems = 0;
    this.items = new KeyedItem[INITIAL_SIZE];
  }
 
  // Precondition for all operations: 
  // No two items of the table have the same search key, and
  // all keys in the table and passed as parameters to methods (sometimes 
  // within a KeyedItem) have the same type and are non-null.
 
  // post: this.items.length < this.numItems
  private void ensureCapacity() {  
    if (this.items.length == this.numItems) {  
      int newLength = this.items.length * 2 + 1;
      KeyedItem newItems[] = new KeyedItem[newLength];
     
      // copy values into the new array
      for (int i = 0; i < this.numItems; i++) {  
        newItems[i] = this.items[i]; 
      }
     
      this.items = newItems; 
    }
  }
 
  // post: returns true if table is empty; otherwise returns false.
  public boolean isEmpty() {
    return this.length()==0;
  }
 
  // post: returns number of items in the table.
  public int length() { 
    return this.numItems; 
  }
 
  // pre:  newItem is not null and its search key differs from all search 
  //       keys presently in this.
  // post: newItem is inserted into its proper place in this.
  public void insert(KeyedItem newItem) { 
    this.ensureCapacity();
    this.items[numItems] =  newItem;
    this.numItems++;
  }
 
  // pre:  searchKey is not null.
  // post: if an item whose search key equals searchKey existed in this, 
  //       it is deleted and method returns true; otherwise, returns false.
  public boolean delete(Comparable searchKey) {
    // scan until searchKey found or all items compared to searchKey
    int i=0;
    while (i < this.numItems && this.items[i].getKey().compareTo(searchKey) != 0) {
      i++;
    }
    if (i == this.numItems) { // matching search key not found
      return false;
    } else {                  // matching search key found at index i
                              // so swap last key to index i
      this.items[i] = this.items[this.numItems-1];
      this.items[this.numItems-1] = null;
      this.numItems--;
      return true;
    }
  }
 
  // pre:  searchKey is not null.
  // post: returns null if searchKey not found, otherwise returns the 
  //       matching item in the table
  public KeyedItem retrieve(Comparable searchKey) { 
    // scan for items and break if found
    for(int i=0; i < this.numItems; i++) {
      if (this.items[i].getKey().compareTo(searchKey) == 0) {
        return this.items[i];
      }
    }
    return null; // item not found
  }
 
  // post: returns an iterator that returns each KeyedItem in the table
  //       exactly once in an unspecified order. 
  public java.util.Iterator getIterator() {
    return new TableIterator(items,numItems);
  }
 
}
