
public class QueueArray implements QueueInterface
{
   private Object items[];
   private int front = 0;
   private int count = 0;
	
   public QueueArray(int initialSize)
   {  // pre: initialSize >= 0
      // post: this can hold up to initialSize elements and
      // all operations will operate in O(1) time.  If
      // additional elements are added, some operations
      // may take O(n) time.
      assert initialSize >= 0;
		
      this.items = new Object[initialSize];
   }
  
   public void enqueue(Object newItem)
   {  // post: newItem is in this
      if (this.count == this.items.length)
      {  // enlarge the array
         Object[] temp = new Object[this.items.length * 2 + 1];
         for (int i = 0; i < this.count; i++)
         {  temp[i] = this.items[(this.front + i) % this.items.length];
         }
   		
         this.items = temp;
         this.front = 0;
      }
      this.items[(this.front + this.count) % this.items.length] = newItem;
      this.count++;
   }
	
   public Object dequeue()
   {  // pre:  this is not empty
      // post: removes and returns the earliest added item 
      // that has not yet been dequeued
      assert !this.isEmpty();
   	
      Object value = this.items[this.front];
      this.items[this.front] = null; // for garbage collector
      this.front = (this.front + 1) % this.items.length;
      this.count--;
      return value;
   }
  
   public void dequeueAll()
   {  // post: this is empty 
      for (int i = 0; i < this.items.length; i++)
      {  this.items[i] = null; // for garbage collector
      }
      this.front = 0;
      this.count = 0;
   }
  
   public boolean isEmpty()
   {  // post: returns true iff this is empty
      return this.count == 0;
   }
  
   public Object peek()
   {  // pre:  this is not empty
      // post: returns the earliest added item that 
      // has not yet been dequeued
      assert !this.isEmpty();
   	
      return this.items[this.front];
   }

   public String toString()
   {  String result = "";
      if (this.count > 0)
      {  result = this.items[this.front].toString();
         for (int i = 1; i < this.count; i++)
         {  result += " "
                  + this.items[(this.front + i) % this.items.length].toString();
         }
      }
      return result;
   }
}
