

/** An implementation of ADT List using an array. */
public class ListArray implements ListInterface
{
   private Object[] items = new Object[0];
   private int numItems = 0;
 
   public void add(int index, Object item)
   {  // pre: 1 <= index <= size()+1
      // post: Items from index to size have their index increased by one,
      // item is inserted at index.
      this.ensureCapacity();
      for (int i = this.numItems; i >= index; i--) 
      {  this.items[this.translate(i + 1)] = this.items[this.translate(i)];
      }

      this.items[translate(index)] = item;
      this.numItems++; 
   }
   
   public Object get(int index)
   {  // pre: 1 <= index <= size()
      // post: returns item at position index in the list
      return this.items[this.translate(index)];
   }
   
   public boolean isEmpty()
   {  // post: returns true iff this is empty
      return this.numItems == 0;
   }
   
   public Object remove(int index)
   {  // pre: 1 <= index <= size()
      // post: removes and returns the item at position index in the list
      // and items from index+1 to the end have their position 
      // decreased by one.
      Object toRemove = this.items[this.translate(index)];

      for (int pos = index; pos < this.size(); pos++)
      {  this.items[this.translate(pos)] = this.items[this.translate(pos + 1)];
      }

      this.items[this.translate(this.numItems)] = null;
      this.numItems--;

      return toRemove;
   }
   
   public void removeAll()
   {  // post: this is empty
      for (int i = 0; i < this.numItems; i++)
      {  this.items[i] = null;
      }  // encourage garbage collection
      this.numItems = 0;
   }
   
   public int size()
   {  // post: returns the number of items currently in this
      return this.numItems;
   }

   private int translate(int i)
   {  return i - 1;
   }
    
   private void ensureCapacity()
   {  if (this.items.length == this.numItems) 
      {  int newLength = this.items.length * 2 + 1;
         Object newItems[] = new Object[newLength];

         // copy values into the new array
         for (int i = 0; i < this.numItems; i++) 
         {  newItems[i] = this.items[i]; 
         }

         this.items = newItems; 

         // N.B. the old array is garbage
      }
   }

   public String toString()
   {  String result = "";
      if (this.numItems != 0)
      {  result = this.items[0].toString();
         for (int i = 1; i < this.numItems; i++)
         {  result = result + " " + this.items[i].toString();
         }
      }
      return result;
   }
}
