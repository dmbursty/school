
public class SubListRecursive implements SubListInterface
{
   private Object root = null;
   private SubListInterface remainder = null;
   
   public SubListRecursive()
   {  // post:  returns an empty sublist
   }
   
   public SubListRecursive(Object root)
   {  // post:  returns a sublist with root as its only item
      this.root = root;
      this.remainder = new SubListRecursive();
   }
   
   public SubListRecursive(Object root, SubListInterface remainder)
   {  // post: returns a sublist with root at the beginning followed by remainder
      this.root = root;
      this.remainder = remainder;
   }
   
   public boolean isEmpty()
   {  // post: returns true iff this is empty.
      return this.root == null && this.remainder == null;
   }

   public void makeEmpty()
   {  // post: this is empty.
      this.remainder = null;
      this.root = null;
   }

   /* Methods dealing with the root. */
   public Object getRootItem()
   {  // pre: this is not empty.
      // post: returns value associated with the root.
      return this.root;
   }
   
   public void setRootItem(Object newItem)
   {  // post: Sets the value associated with the root to be newItem
      // if this is not empty; otherwise sets this to consist of a
      // root node only, with root item set to newItem.
      if (this.isEmpty())
      {
         this.remainder = new SubListRecursive();
      }
      this.root = newItem;
   }
   
   /* Methods dealing with remainders. */
   public SubListInterface getRemainder()
   {  // pre: this is not empty
      // post: returns the remainder of this
      assert !this.isEmpty();
      return this.remainder;
   }

   public SubListInterface detachRemainder()
   {  // pre: this is not empty.
      // post: returns the remainder of this and sets the remainder to
      // be the empty sublist.
      assert !this.isEmpty();
      SubListInterface t = this.remainder;
      this.remainder = new SubListRecursive();
      return t;
   }
   
   public void attachRemainder(SubListInterface rem)
   {  // pre: this is not empty, the remainder of this is empty, and
      // rem is not null.
      // post: attaches value of rem as the remainder of this
      assert !this.isEmpty();
      assert this.remainder.isEmpty();
      assert rem != null;
      this.remainder = rem;
   }
   
   public String toString()
   {
      if (this.isEmpty())
         return "";
      else if (this.remainder.isEmpty())
         return this.root.toString();
      else
         return this.root.toString() + " " + this.remainder.toString();
   }
}
