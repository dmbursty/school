
public interface StackInterface
{
   public boolean isEmpty();
   // post: returns true iff this is empty

   public void push(Object item);
   // post: newItem is added to the top of this

   public Object pop();
   // pre: this is not empty
   // post: removes and returns the item in this that was the 
   // most recently added

   public void popAll();
   // post: this is empty

   public Object peek();
   // pre: this is not empty
   // post: returns the item in this that was most recently added

   // Note that size() is not a defined method in this interface.
}
