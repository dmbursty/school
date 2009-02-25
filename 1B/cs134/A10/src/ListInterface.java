public interface ListInterface
{  /* finite (possibly empty) collection of objects,
    * indexed from 1 to size()
    */
  
   public void add(int index, Object item);
   // pre: 1 <= index <= size()+1
   // post: Items from index to size have their index increased by one,
   // item is inserted at index.
  
   public Object get(int index);
   // pre: 1 <= index <= size()
   // post: returns item at position index in the list
  
   public boolean isEmpty();
   // post: returns true iff this is empty
  
   public Object remove(int index);
   // pre: 1 <= index <= size()
   // post: removes and returns the item at position index in the list
   // and items from index+1 to the end have their position 
   // decreased by one.
  
   public void removeAll();
   // post: this is empty
  
   public int size();
   // post: returns the number of items currently in this
}
