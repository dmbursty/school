

/** An implementation of ADT List using a linked list. */
public class ListLinked implements ListInterface
{
   private Node head;
   private int numItems;
	
   /** Construct an empty list. */
   public ListLinked()
   {  this.head = null;
      this.numItems = 0;
   }

   private Node find(int index)
   {  // pre:	1 <= index <= numItems
      // post:	returns a reference to the node at position index
      assert 1 <= index && index <= this.numItems;
      
      Node curr = this.head;
      for (int skip = 1; skip < index; skip++)
      {  curr = curr.getNext();
      }
      return curr;
   }
   
   public void add(int index, Object item)
   {  // pre: 1 <= index <= size()+1
      // post: Items from index to size have their index increased by one,
      // item is inserted at index.
      assert 1 <= index && index <= this.size() + 1;
		
      Node n = new Node(item);
      if (index == 1)
      {  n.setNext(this.head);
         this.head = n;
      } else
      {  Node prev = this.find(index - 1);
         n.setNext(prev.getNext());
         prev.setNext(n);
      }
      this.numItems++;
   }

   public Object get(int index)
   {  // pre: 1 <= index <= size()
      // post: returns item at position index in the list
      assert 1 <= index && index <= this.size();
   	
      return this.find(index).getItem();
   }

   public boolean isEmpty()
   {  // post: returns true iff this is empty
      return this.head == null;
   }

   public Object remove(int index)
   {  // pre: 1 <= index <= size()
      // post: removes and returns the item at position index in the list
      // and items from index+1 to the end have their position 
      // decreased by one.
      assert 1 <= index && index <= this.size();
   	
      Node toRemove;
      if (index == 1)
      {  toRemove = this.head;
         this.head = this.head.getNext();
      } else
      {  Node prev = this.find(index - 1);
         toRemove = prev.getNext();
         prev.setNext(toRemove.getNext());
      }
      this.numItems--;
      return toRemove.getItem();
   }

   public void removeAll()
   {  // post: this is empty
      this.head = null;
      this.numItems = 0;
   }

   public int size()
   {  // post: returns the number of items currently in this
      return this.numItems;
   }
	
   public String toString()
   {  String result = "";
      if (this.head != null)
      {  result = this.head.getItem().toString();
         Node curr = this.head.getNext();
         while (curr != null)
         {  result = result + " " + curr.getItem().toString();
            curr = curr.getNext();
         }
      }
      return result;
   }
}
