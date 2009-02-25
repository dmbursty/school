public class QueueLinked implements QueueInterface 
{
   private Node lastNode = null;
  
   public boolean isEmpty() 
   {  // post: returns true iff this is empty
      return lastNode == null;
   }  
  
   public void enqueue(Object newItem) 
   {  // post: newItem is in this
      Node newNode = new Node(newItem);
    
      // insert the new node
      if (isEmpty()) 
      {  // insertion into empty queue
         newNode.setNext(newNode);
      } else 
      {  // insertion into nonempty queue
         newNode.setNext(lastNode.getNext());
         lastNode.setNext(newNode);
      }  
    
      lastNode = newNode; // new node is at back
   } 
  
   public Object dequeue()  
   {  // pre:  this is not empty
      // post: removes and returns the earliest added item 
      // that has not yet been dequeued
      // queue is not empty; remove front
      assert !this.isEmpty();
   	
      Node firstNode = lastNode.getNext();
    
      // special case?
      if (firstNode == lastNode)
      {  // yes, one node in queue
         lastNode = null;           
      } else
      {  lastNode.setNext(firstNode.getNext());
      }  
    
      return firstNode.getItem();
   }  
   
   public void dequeueAll() 
   {  // post: this is empty 
      lastNode = null;
   }
  
   public Object peek() 
   {  // pre:  this is not empty
      // post: returns the earliest added item that 
      // has not yet been dequeued
      assert !this.isEmpty();
   	
      Node firstNode = lastNode.getNext();
      return firstNode.getItem();
   }  
  
   public String toString()
   {	// post: return a representation of this as a string
   	String result = "";
   	if (this.lastNode == null)
   		return result;
   	
   	Node n = this.lastNode.getNext();
   	while (n != this.lastNode)
   	{
   		result = result + " " + n.getItem().toString();
   		n = n.getNext();
   	}
   	result = result + " " + n.getItem().toString();
   	return result.trim();
   }

} 
