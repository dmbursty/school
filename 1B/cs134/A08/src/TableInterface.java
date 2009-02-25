import java.util.Iterator;

public interface TableInterface 
{
   // Precondition for all operations: 
   // No two items of the table have the same search key, and
   // all keys in the table and passed as parameters to methods (sometimes 
   // within a KeyedItem) have the same type and are non-null.
   
   public boolean isEmpty();
   // post: returns true if table is empty; otherwise returns false.
   
   public int length();
   // post: returns number of items in the table.
   
   public void insert(KeyedItem newItem);
   // pre:  newItem is not null and its search key differs from all search 
   // keys presently in this.
   // post: newItem is inserted into its proper place in this.
  
   public boolean delete(Comparable searchKey);
   // pre:  searchKey is not null.
   // post: if an item whose search key equals searchKey existed in this, 
   // it is deleted and method returns true; otherwise, returns false.
  
   public KeyedItem retrieve(Comparable searchKey);
   // pre:  searchKey is not null.
   // post: returns null if searchKey not found, otherwise returns the 
   // matching item in the table
 
   public Iterator getIterator();
   // post: returns an iterator that returns each KeyedItem in the table
   // exactly once in an unspecified order. 
}  
