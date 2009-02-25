public interface QueueInterface
{
   public void enqueue(Object newItem);
   // post: newItem is in this
  
   public Object dequeue();
   // pre:  this is not empty
   // post: removes and returns the earliest added item 
   // that has not yet been dequeued
   
   public boolean isEmpty();
   // post: returns true iff this is empty
  
   public void dequeueAll();
   // post: this is empty 
  
   public Object peek();
   // pre:  this is not empty
   // post: returns the earliest added item that 
   // has not yet been dequeued
  
}
