public interface TableInterface 
{
   // Precondition for all operations: 
   // No two items of the table have the same search key.
	  
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
  
}  
