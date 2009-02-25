import junit.framework.TestCase;

public class BinaryTreeTest extends TestCase
{

   public void testDataFactory0()
   {
      BinaryTreeInterface bt = DataFactory.makeBinaryTree();
      assertNotNull(bt);
      assertEquals(true, bt.isEmpty());
   }

   public void testDataFactory1()
   {
      BinaryTreeInterface bt = DataFactory.makeBinaryTree("Root");
      assertNotNull(bt);
      assertEquals(false, bt.isEmpty());
      assertEquals("Root", bt.getRootItem());
      assertEquals(true, bt.getLeftSubtree().isEmpty());
      assertEquals(true, bt.getRightSubtree().isEmpty());
   }

   public void testDataFactory2()
   {
      BinaryTreeInterface left = DataFactory.makeBinaryTree("Left");
      BinaryTreeInterface right = DataFactory.makeBinaryTree("Right");
      BinaryTreeInterface bt = DataFactory.makeBinaryTree("Root", left, right);
      assertNotNull(bt);
      assertEquals(false, bt.isEmpty());
      assertEquals("Root", bt.getRootItem());
      assertEquals(left, bt.getLeftSubtree());
      assertEquals(right, bt.getRightSubtree());
   }
}
