
/** An implementation of ADT Stack that provides constant time
 * operations, provided the user's initial guess for the size is
 * not exceeded.  If the initial size is exceeded, the stack will 
 * grow as necessary, but not with constant time operations.
 */
public class StackArray implements StackInterface
{
   private static final int DEFAULT_SIZE = 30;
   private final int ORIGINAL_SIZE;
   private Object[] items;
   private int top = -1;
   
   public StackArray(int initialSize)
   {  // post: create a new stack with room for initialSize items
      this.items = new Object[initialSize];
      this.ORIGINAL_SIZE = initialSize;
   }
   
   public StackArray()
   {  // post: create a new stack with a default initial size
      this(DEFAULT_SIZE);
   }
   public boolean isEmpty()
   {  // post: returns true iff this is empty
      return this.top == -1;
   }

   public void push(Object item)
   {  // post: newItem is added to the top of this
      if (this.top == this.items.length-1)
      {
         Object[] tmp = new Object[this.items.length * 2 + 1];
         for(int i=0; i<=this.top; i++)
         {
            tmp[i] = this.items[i];
         }
         this.items = tmp;
      }
      
      this.top++;      
      this.items[this.top] = item;
   }

   public Object pop()
   {  // pre: this is not empty
      // post: removes and returns the item in this that was the 
      // most recently added
      assert this.top >= 0;
      Object topItem = this.items[this.top];
      this.items[this.top] = null;
      this.top--;
      return topItem;
   }

   public void popAll()
   {  // post: this is empty
      this.items = new Object[this.ORIGINAL_SIZE];
      this.top = -1;
   }

   public Object peek()
   {  // pre: this is not empty
      // post: returns the item in this that was most recently added
      assert this.top >= 0;
      return this.items[this.top];
   }
   

   public String toString()
   {  // post: return a representation of this as a string
      String result = "";
      for(int i=this.top; i>=0; i--)
      {
         result = result + " " + this.items[i].toString();
      }
      return result.trim();
   }
}
