import junit.framework.*;
public class SizeTests extends TestCase {
    
  public void testSizeZero2() {
  // Input:  Empty binary tree 2
  // Test:   size zero
  // Result: returns 0
    BinaryTree2 tree = new BinaryTree2();
    assertEquals(0,tree.size());
  }
  
  public void testSizeOne2() {
  // Input:  Single node binary tree 2
  // Test:   size 1
  // Result: returns 1
    BinaryTree2 tree = new BinaryTree2("A");
    assertEquals(1,tree.size());
  }
  
  public void testSizeLargeTree2() {
  // Input:  Larger binary tree 2
  // Test:   Normal case
  // Result: returns 6
    BinaryTree2 tree = new BinaryTree2();
    tree.setRootItem("a");
    tree.attachLeft(DataFactory.makeBinaryTree2());
    tree.getLeftSubtree().setRootItem("b");
    tree.getLeftSubtree().attachLeft(DataFactory.makeBinaryTree2());
    tree.getLeftSubtree().attachRight(DataFactory.makeBinaryTree2());
    tree.getLeftSubtree().getLeftSubtree().setRootItem("c");
    tree.getLeftSubtree().getRightSubtree().setRootItem("d");
    tree.attachRight(DataFactory.makeBinaryTree2());
    tree.getRightSubtree().setRootItem("e");
    tree.getRightSubtree().attachRight(DataFactory.makeBinaryTree2());
    tree.getRightSubtree().getRightSubtree().setRootItem("f");
    assertEquals(6,tree.size());
  }
  
  public void testSizeZero() {
  // Input:  Empty binary tree
  // Test:   size zero
  // Result: returns 0
    BinaryTree tree = new BinaryTree();
    assertEquals(0,tree.size());
  }
  
  public void testSizeOne() {
  // Input:  Single node binary tree
  // Test:   size one
  // Result: returns 1
    BinaryTree tree = new BinaryTree("A");
    assertEquals(1,tree.size());
  }
  
  public void testSizeLargeTree() {
  // Input:  Larger binary tree
  // Test:   Normal case
  // Result: returns 6
    BinaryTree tree = new BinaryTree();
    tree.setRootItem("a");
    tree.attachLeft(DataFactory.makeBinaryTree());
    tree.getLeftSubtree().setRootItem("b");
    tree.getLeftSubtree().attachLeft(DataFactory.makeBinaryTree());
    tree.getLeftSubtree().attachRight(DataFactory.makeBinaryTree());
    tree.getLeftSubtree().getLeftSubtree().setRootItem("c");
    tree.getLeftSubtree().getRightSubtree().setRootItem("d");
    tree.attachRight(DataFactory.makeBinaryTree());
    tree.getRightSubtree().setRootItem("e");
    tree.getRightSubtree().attachRight(DataFactory.makeBinaryTree());
    tree.getRightSubtree().getRightSubtree().setRootItem("f");
    assertEquals(6,tree.size());
  }
}
    