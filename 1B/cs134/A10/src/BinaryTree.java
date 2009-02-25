import java.util.Iterator;


public class BinaryTree implements BinaryTreeInterface
{

   private Object root; // Reference to the root item
   private BinaryTreeInterface leftChild; // Reference to the left subtree
   private BinaryTreeInterface rightChild; // Reference to the left subtree

   
   // Create an empty tree
   public BinaryTree()
   {  this.root = null;
      this.leftChild = null;
      this.rightChild = null;
   }

   // Create a one node tree
   public BinaryTree(Object root)
   {  super();
      this.root = root;
      this.leftChild = new BinaryTree();
      this.rightChild = new BinaryTree();
   }

   // Create a tree with the given root node and left and
   // right subtrees
   public BinaryTree(Object root, BinaryTreeInterface left,
         BinaryTreeInterface right)
   {  super();
      this.root = root;
      this.leftChild = left;
      this.rightChild = right;
   }

   public boolean isEmpty()
   {  // post: returns true iff this is empty.
      return (this.leftChild == null) && (this.rightChild == null);
   }

   public void makeEmpty()
   {  // post: this is empty.
      this.root = null;
      this.leftChild = null;
      this.rightChild = null;
   }

   public Object getRootItem()
   {  // pre:  this is not empty.
      // post: returns value associated with the root.
      assert !this.isEmpty();
		
      return this.root;
   }

   public void setRootItem(Object newItem)
   {  // post: if this is not empty set the value associated with the root to be 
      // newItem; otherwise set this tree to consist of a root node only, 
      // with root item set to newItem.
   	
      // Make new empty left/right subtrees if this is empty
      if (this.isEmpty())
      {  this.leftChild = new BinaryTree();
         this.rightChild = new BinaryTree();
      }

      // Overwrite the root
      this.root = newItem;
   }

   public void attachLeft(Object newItem)
   {  // pre:  this is not empty, left subtree is empty
      // post: sets the left subtree to be a leaf node with 
      // associated value set to newItem.
      assert !this.isEmpty();
      assert this.leftChild.isEmpty();
		
      this.leftChild.setRootItem(newItem);
   }

   public void attachRight(Object newItem)
   {  // pre:  this is not empty, right subtree is empty
      // post: sets the right subtree to be a leaf node with 
      // associated value set to newItem.
      assert !this.isEmpty();
      assert this.rightChild.isEmpty();

      this.rightChild.setRootItem(newItem);
   }

   public BinaryTreeInterface getLeftSubtree()
   {  // pre:  this is not empty.
      // post: returns the left subtree of this
      assert !this.isEmpty();
		
      return this.leftChild;
   }

   public void attachLeftSubtree(BinaryTreeInterface leftTree)
   {  // pre:  this is not empty, the left subtree of this is empty, 
      // and leftTree is not null.
      // post: attaches leftTree as the left subtree of this 
      assert !this.isEmpty();
      assert this.leftChild.isEmpty();
      assert leftTree != null;

      this.leftChild = leftTree;
   }

   public BinaryTreeInterface detachLeftSubtree()
   {  // pre:  this is not empty.
      // post: returns the left subtree of this 
      // and sets the left subtree to be the empty tree.
      assert !this.isEmpty();
		
      BinaryTreeInterface tree = this.leftChild;
      this.leftChild = new BinaryTree();
      return tree;
   }

   public BinaryTreeInterface getRightSubtree()
   {  // pre:  this is not empty.
      // post: returns the right subtree of this
      assert !this.isEmpty();
		
      return this.rightChild;
   }

   public void attachRightSubtree(BinaryTreeInterface rightTree)
   {  // pre:  this is not empty, the right subtree of this is empty, 
      // and rightTree is not null.
      // post: attaches rightTree as the right subtree of this
      assert !this.isEmpty();
      assert this.rightChild.isEmpty();
      assert rightTree != null;
		
      this.rightChild = rightTree;
   }

   public BinaryTreeInterface detachRightSubtree()
   {  // pre:  this is not empty.
      // post: returns the right subtree of this 
      // and sets the right subtree to be the empty tree.
      assert !this.isEmpty();
		
      BinaryTreeInterface tree = this.rightChild;
      this.rightChild = new BinaryTree();
      return tree;
   }
	
   public Iterator getPreorderIterator()
   {  // post: returns an iterator that returns each item in the tree once
      // such that roots are returned before items in their subtrees
      return new PreorderIterator2(this);
   }

   public Iterator getPostorderIterator()
   {  // post: returns an iterator that returns each item in the tree once
      // such that roots are returned after all the items in their subtrees
      return new PostorderIterator(this);
   }

   public Iterator getInorderIterator()
   {  // post: returns an iterator that returns each item in the tree once
      // such that roots are returned after the items in the left subtree
      // and before items in the right subtree
      return new InorderIterator(this);
   }

   public Iterator getLevelOrderIterator()
   {  // post: returns an iterator that returns each item in the tree once
      // such that all of the items at level n are returned before the items
      // at level n+1.
      return new LevelOrderIterator(this);
   }
}
