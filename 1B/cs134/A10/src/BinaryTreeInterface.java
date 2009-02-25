// The Interface for a Binary Tree
import java.util.Iterator;


public interface BinaryTreeInterface
{

   /* methods dealing with empty trees */
	
   public boolean isEmpty();
   // post: returns true iff this is empty.
  
   public void makeEmpty();
   // post: this is empty.
   
   /* methods dealing with items */
  
   public Object getRootItem();
   // pre:  this is not empty.
   // post: returns value associated with the root.
  
   public void setRootItem(Object newItem);
   // post: if this is not empty set the value associated with the root to be 
   // newItem; otherwise set this tree to consist of a root node only, 
   // with root item set to newItem.
  
   public void attachLeft(Object newItem);
   // pre:  this is not empty, left subtree is empty
   // post: sets the left subtree to be a leaf node with 
   // associated value set to newItem.
  
   public void attachRight(Object newItem);
   // pre:  this is not empty, right subtree is empty
   // post: sets the right subtree to be a leaf node with 
   // associated value set to newItem.
   
   /* methods dealing with subtrees */
   
   public BinaryTreeInterface getLeftSubtree();
   // pre:  this is not empty.
   // post: returns the left subtree of this
  
   public void attachLeftSubtree(BinaryTreeInterface leftTree);
   // pre:  this is not empty, the left subtree of this is empty, 
   // and leftTree is not null.
   // post: attaches leftTree as the left subtree of this 

   public BinaryTreeInterface detachLeftSubtree();
   // pre:  this is not empty.
   // post: returns the left subtree of this 
   // and sets the left subtree to be the empty tree.
   
   public BinaryTreeInterface getRightSubtree();
   // pre:  this is not empty.
   // post: returns the right subtree of this
   
   public void attachRightSubtree(BinaryTreeInterface rightTree);
   // pre:  this is not empty, the right subtree of this is empty, 
   // and rightTree is not null.
   // post: attaches rightTree as the right subtree of this
  
   public BinaryTreeInterface detachRightSubtree();
   // pre:  this is not empty.
   // post: returns the right subtree of this 
   // and sets the right subtree to be the empty tree.
  
   public Iterator getPreorderIterator();
   // post: returns an iterator that returns each item in the tree once
   // such that roots are returned before items in their subtrees
   
   public Iterator getPostorderIterator();
   // post: returns an iterator that returns each item in the tree once
   // such that roots are returned after all the items in their subtrees

   public Iterator getInorderIterator();
   // post: returns an iterator that returns each item in the tree once
   // such that roots are returned after the items in the left subtree
   // and before items in the right subtree

   public Iterator getLevelOrderIterator();
   // post: returns an iterator that returns each item in the tree once
   // such that all of the items at level n are returned before the items
   // at level n+1.
}  
