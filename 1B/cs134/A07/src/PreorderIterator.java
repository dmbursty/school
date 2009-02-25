public class PreorderIterator implements java.util.Iterator
{
   private QueueInterface q;
  
   PreorderIterator(BinaryTreeInterface bt)
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
   {  throw new UnsupportedOperationException("Remove is not supported");
   }
  
   private void fillQueue(BinaryTreeInterface t)
   {  if (!t.isEmpty())
      {  q.enqueue(t.getRootItem());
         fillQueue(t.getLeftSubtree());
         fillQueue(t.getRightSubtree());
      }
   }
}
