import java.util.Iterator;


public class BinaryTree2 implements BinaryTreeInterface2
{

   private Object root; // Reference to the root item
   private BinaryTreeInterface2 leftChild; // Reference to the left subtree
   private BinaryTreeInterface2 rightChild; // Reference to the left subtree

   // Create an empty tree
   public BinaryTree2()
   {  this.root = null;
      this.leftChild = null;
      this.rightChild = null;
   }

   // Create a one node tree
   public BinaryTree2(Object root)
   {  super();
      this.root = root;
      this.leftChild = new BinaryTree2();
      this.rightChild = new BinaryTree2();
   }

   // Create a tree with the given root node and left and
   // right subtrees
   public BinaryTree2(Object root, BinaryTreeInterface2 left,
         BinaryTreeInterface2 right)
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
      {  this.leftChild = new BinaryTree2();
         this.rightChild = new BinaryTree2();
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

   public BinaryTreeInterface2 getLeftSubtree()
   {  // pre:  this is not empty.
      // post: returns the left subtree of this
      assert !this.isEmpty();
		
      return this.leftChild;
   }

   public void attachLeftSubtree(BinaryTreeInterface2 leftTree)
   {  // pre:  this is not empty, the left subtree of this is empty, 
      // and leftTree is not null.
      // post: attaches leftTree as the left subtree of this 
      assert !this.isEmpty();
      assert this.leftChild.isEmpty();
      assert leftTree != null;

      this.leftChild = leftTree;
   }

   public BinaryTreeInterface2 detachLeftSubtree()
   {  // pre:  this is not empty.
      // post: returns the left subtree of this 
      // and sets the left subtree to be the empty tree.
      assert !this.isEmpty();
		
      BinaryTreeInterface2 tree = this.leftChild;
      this.leftChild = new BinaryTree2();
      return tree;
   }

   public BinaryTreeInterface2 getRightSubtree()
   {  // pre:  this is not empty.
      // post: returns the right subtree of this
      assert !this.isEmpty();
		
      return this.rightChild;
   }

   public void attachRightSubtree(BinaryTreeInterface2 rightTree)
   {  // pre:  this is not empty, the right subtree of this is empty, 
      // and rightTree is not null.
      // post: attaches rightTree as the right subtree of this
      assert !this.isEmpty();
      assert this.rightChild.isEmpty();
      assert rightTree != null;
		
      this.rightChild = rightTree;
   }

   public BinaryTreeInterface2 detachRightSubtree()
   {  // pre:  this is not empty.
      // post: returns the right subtree of this 
      // and sets the right subtree to be the empty tree.
      assert !this.isEmpty();
		
      BinaryTreeInterface2 tree = this.rightChild;
      this.rightChild = new BinaryTree2();
      return tree;
   }
   
   public int size()
   { // post: returns the number of nodes in this
     
     // If this is an empty tree, return zero
     if (this.isEmpty())
     {
       return 0;
     }
     // Otherwise return 1 plus the number of nodes in left plus the number of nodes in right
     else
     {
       return 1 + this.getLeftSubtree().size() + this.getRightSubtree().size();
     }
   }
}
