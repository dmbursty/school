
public class StackLinked implements StackInterface
{
   private Node top = null;

   public StackLinked()
   {}
	
   public boolean isEmpty()
   {  // post: returns true iff this is empty
      return this.top == null;
   }

   public void push(Object item)
   {  // post: newItem is added to the top of this
      this.top = new Node(item, this.top);
   }

   public Object pop()
   {  // pre: this is not empty
      // post: removes and returns the item in this that was the 
      // most recently added
      assert !this.isEmpty();
		
      Node oldTop = this.top;
      this.top = oldTop.getNext();
      oldTop.setNext(null); // enable garbage collection
      return oldTop.getItem();
   }

   public void popAll()
   {  // post: this is empty
      this.top = null;
   }

   public Object peek()
   {  // pre: this is not empty
      // post: returns the item in this that was most recently added
      assert !this.isEmpty();
      return this.top.getItem();
   }

   public String toString()
   {	// post: return a representation of this as a string
   	String result = "";
   	Node n = this.top;
   	while (n != null)
   	{
   		result = result + " " + n.getItem().toString();
   		n = n.getNext();
   	}
   	return result.trim();
   }
}
