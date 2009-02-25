import junit.framework.TestCase;

public class SubListRecursiveTest extends TestCase
{
   public void testDataFactory0()
   {
      SubListInterface sl = DataFactory.makeSubList();
      assertNotNull(sl);
      assertEquals(true, sl.isEmpty());
   }

   public void testDataFactory1()
   {
      Object o = new Object();
      SubListInterface sl = DataFactory.makeSubList(o);
      assertNotNull(sl);
      assertEquals(false, sl.isEmpty());
      assertEquals(o, sl.getRootItem());
   }

   public void testDataFactory2()
   {
      Object o0 = new Object();
      Object o1 = new Object();
      SubListInterface rem = DataFactory.makeSubList(o0);
      SubListInterface sl = DataFactory.makeSubList(o1, rem);
      assertNotNull(sl);
      assertEquals(false, sl.isEmpty());
      assertEquals(o1, sl.getRootItem());
      assertEquals(rem, sl.getRemainder());
      assertEquals(o0, sl.getRemainder().getRootItem());
   }
   
   private SubListInterface build1()
   {
      return DataFactory.makeSubList("A");
   }
   
   private SubListInterface build2()
   {
      return DataFactory.makeSubList("B", this.build1());
   }
   
   private SubListInterface build3()
   {
      return DataFactory.makeSubList("C", this.build2());
   }
   
   public void testSetRootItem()
   {
      SubListInterface sl = this.build1();
      assertEquals("A", sl.getRootItem());
      sl.setRootItem("B");
      assertEquals("B", sl.getRootItem());
   }
   
   public void testAttachRemainder()
   {
      SubListInterface sl = this.build1();
      SubListInterface rem = this.build2();
      sl.attachRemainder(rem);
      assertEquals(rem, sl.getRemainder());
   }
   
   public void testDetachRemainder()
   {
      SubListInterface rem = this.build2();
      SubListInterface sl = DataFactory.makeSubList("C", rem);
      SubListInterface dRem = sl.detachRemainder();
      assertEquals(rem, dRem);
      assertEquals("C", sl.getRootItem());
      assertEquals(true, sl.getRemainder().isEmpty());
   }
}
