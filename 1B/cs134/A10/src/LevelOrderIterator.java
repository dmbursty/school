import java.util.Iterator;


public class LevelOrderIterator implements Iterator
{
   private QueueInterface q = DataFactory.makeQueue();

   public LevelOrderIterator(BinaryTreeInterface t)
   {  if (!t.isEmpty())
      {  q.enqueue(t);
      }
   }
	
   public boolean hasNext()
   {  return !this.q.isEmpty();
   }
	
   public Object next()
   {  assert !this.q.isEmpty();
		
      BinaryTreeInterface t = (BinaryTreeInterface) q.dequeue();
      BinaryTreeInterface left = t.getLeftSubtree();
      if (!left.isEmpty())
      {  this.q.enqueue(left);
      }
      BinaryTreeInterface right = t.getRightSubtree();
      if (!right.isEmpty())
      {  this.q.enqueue(right);
      }
		
      return t;
   }
		  
   public void remove()
   {  throw new UnsupportedOperationException("Remove is not supported");
   }

}
