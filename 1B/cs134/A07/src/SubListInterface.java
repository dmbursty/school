public interface SubListInterface
{ /* Methods dealing with the entire structure. */

   public boolean isEmpty();
   // post: returns true iff this is empty.

   public void makeEmpty();
   // post: this is empty.

   /* Methods dealing with the root. */
   public Object getRootItem();
   // pre: this is not empty.
   // post: returns value associated with the root.

   public void setRootItem(Object newItem);
   // post: Sets the value associated with the root to be newItem
   // if this is not empty; otherwise sets this to consist of a
   // root node only, with root item set to newItem.

   /* Methods dealing with remainders. */
   public SubListInterface getRemainder();
   // pre: this is not empty
   // post: returns the remainder of this

   public SubListInterface detachRemainder();
   // pre: this is not empty.
   // post: returns the remainder of this and sets the remainder to
   // be the empty sublist.

   public void attachRemainder(SubListInterface rem);
   // pre: this is not empty, the remainder of this is empty, and
   // rem is not null.
   // post: attaches value of rem as the remainder of this
}
