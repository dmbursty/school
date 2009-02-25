import java.util.Iterator;


public class PreorderIterator2 implements Iterator
{
   private StackInterface stack = DataFactory.makeStack();
	
   public PreorderIterator2(BinaryTreeInterface tree)
   {  if (!tree.isEmpty())
      {  stack.push(tree);
      }
   }
	
   public boolean hasNext()
   {  return !this.stack.isEmpty();
   }

   public Object next()
   {  BinaryTreeInterface next = (BinaryTreeInterface) this.stack.pop();
      if (!next.getRightSubtree().isEmpty())
      {  this.stack.push(next.getRightSubtree());
      }
      if (!next.getLeftSubtree().isEmpty())
      {  this.stack.push(next.getLeftSubtree());
      }
      return next.getRootItem();
   }

   public void remove()
   {  throw new UnsupportedOperationException("Remove is not supported");
   }

}
