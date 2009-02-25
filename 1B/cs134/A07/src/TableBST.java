public class TableBST implements TableInterface 
{
   
   // Precondition for all operations: 
   // No two items of the table have the same search key.
  
   private BinaryTreeInterface table;
   private int size;
  
   public TableBST()
   {  this.table = DataFactory.makeBinaryTree();
      this.size = 0;
   }
	  
   public boolean isEmpty()
   {	// post: returns true if table is empty; otherwise returns false.
   	return this.table.isEmpty();
   }
   
   public int length()
   {	// post: returns number of items in the table.
   	return this.size;
   }
   
   public void insert(KeyedItem newItem)
   {// pre:  newItem is not null and its search key differs from all search 
   // keys presently in this.
   // post: newItem is inserted into its proper place in this.

   	// Find the subtree where the item should be placed
      BinaryTreeInterface subtree = locate(newItem.getKey(), table);
    
      if (!subtree.isEmpty())
      {  System.out.println(
               "key: " + newItem.getKey() + " already exists in table.");
      } else
      {  subtree.setRootItem(newItem);
         size++;
      }
   }
  
   public boolean delete(Comparable searchKey)
   {// pre:  searchKey is not null.
   // post: if an item whose search key equals searchKey existed in this, 
   // it is deleted and method returns true; otherwise, returns false.

   	BinaryTreeInterface target = locate(searchKey, table);
      
      // Case 0: key not found
      if (target.isEmpty())
      {  return false;
      } // Case 1: key found in leaf
      else if (target.getLeftSubtree().isEmpty() && target.getRightSubtree().isEmpty())
      {  target.makeEmpty();
         size--;
      } // Case 2.1: target has an empty left subtree
      else if (target.getLeftSubtree().isEmpty())
      {  // Detach the right subtree
         BinaryTreeInterface oldRight = target.detachRightSubtree();
      
         // Make the target a clone of the old right subtree
         target.setRootItem(oldRight.getRootItem());
         target.attachLeftSubtree(oldRight.getLeftSubtree());
         target.attachRightSubtree(oldRight.getRightSubtree());
         size--;
      } // Case 2.2: target has an empty right subtree
      else if (target.getRightSubtree().isEmpty())
      {  // Detach the left subtree
         BinaryTreeInterface oldLeft = target.detachLeftSubtree();
      
         // Make the target a clone of the old left subtree
         target.setRootItem(oldLeft.getRootItem());
         target.attachLeftSubtree(oldLeft.getLeftSubtree());
         target.attachRightSubtree(oldLeft.getRightSubtree());
         size--;
      } // Case 3: target is a full node
      else
      {  // Get a copy of the inorder successor
         KeyedItem successor = getMinimum(target.getRightSubtree());
      
         // Delete the inorder successor
         this.delete(successor.getKey());
      
         // Set our root item to the inorder successor
         target.setRootItem(successor);
      }
    
      return true;
   }
  
   public KeyedItem retrieve(Comparable searchKey)
   {// pre:  searchKey is not null.
   // post: returns null if searchKey not found, otherwise returns the 
   // matching item in the table
   	
   	// Find the subtree where the item should be located
      BinaryTreeInterface subtree = locate(searchKey, table);
    
      if (!subtree.isEmpty())
      {  return (KeyedItem) subtree.getRootItem();
      } else
      {  return null;
      }
   }
  
   // Recursive helper function to find the appropriate subtree in the table
   private BinaryTreeInterface locate(Comparable key, BinaryTreeInterface bt)
   // pre:  key != null,  bt is a binary search tree
   // post: returns the subtree containing the key as its root,
   // or the subtree that would have contained the key
   {  // Base case:  Return the empty tree we eventually found
      if (bt.isEmpty())
      {  return bt;
      }
    
      // Get the root item's key value
      Comparable k = ((KeyedItem) bt.getRootItem()).getKey(); 
    
      // Base case:  Return the tree which has the correct key as its root
      if (key.compareTo(k) == 0)
      {  return bt;
      } // Recursive case:  Look in the left subtree if root is too big
      else if (key.compareTo(k) < 0)
      {  return locate(key, bt.getLeftSubtree());
      } // Recursive case:  Look in the right subtree if root is too small
      else
      {  return locate(key, bt.getRightSubtree());
      }
   }
  
   // Recursive helper function to find the smallest key in the tree
   private KeyedItem getMinimum(BinaryTreeInterface bt)
   // pre:  bt is a non-empty binary search tree
   // post: returns the KeyedItem with the smallest key
   {  // Base: left subtree is empty
      if (bt.getLeftSubtree().isEmpty())
      {  return (KeyedItem) bt.getRootItem();
      } // Recursive: stuff exists in left subtree
      else
      {  return getMinimum(bt.getLeftSubtree());
      }
   }
   
   // Represent this table as a string listing each element in order
   public String toString()
   {
   	return this.toString(this.table).toString();
   }
   
   private StringBuffer toString(BinaryTreeInterface bst)
   {	// pre:	bst is not null
   	// post:	return a string representing an in-order traversal of bst
   	if (bst.isEmpty())
   	{
   		return new StringBuffer();
   	} else
   	{
   		StringBuffer result = this.toString(bst.getLeftSubtree());
   		result.append(bst.getRootItem().toString());
   		result.append(this.toString(bst.getRightSubtree()));
   		return result;
   	}
   }
}
