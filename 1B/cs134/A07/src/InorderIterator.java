public class InorderIterator implements java.util.Iterator
{
   private QueueInterface q;
  
   InorderIterator(BinaryTreeInterface bt)
   {  q = DataFactory.makeQueue();
    
      // Preload the traversal of the tree as it stands now
      fillQueue(bt);
   }
  
   public boolean hasNext()
   {  return !q.isEmpty();
   }
  
   public Object next() 
   {  return q.dequeue();
   }
  
   public void remove()
   {  System.out.println("Remove is not supported");
   }
  
   private void fillQueue(BinaryTreeInterface t)
   {  if (!t.isEmpty())
      {  fillQueue(t.getLeftSubtree());
         q.enqueue(t.getRootItem());
         fillQueue(t.getRightSubtree());
      }
   }
}
