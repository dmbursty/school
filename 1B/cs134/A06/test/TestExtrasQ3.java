import junit.framework.TestCase;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 * 
 * Provides initial (but incomplete) testing of the methods required for Assignment 6,
 * Question 3.
 */
public class TestExtrasQ3 extends TestCase {
  /**
   * A test method.
   * (Replace "X" with a name describing the test.  You may write as
   * many "testSomething" methods in this class as you wish, and each
   * one will be called when running JUnit over this class.)
   */
   
  public void testGCDWithZero()
  // Input:  a=3,b=0
  // Test:   one of the numbers is zero
  // Result: returns the non zero value
  {
    assertEquals(3,A06Q3.gcd(3,0));
  }
  
  public void testMakeAllPalindromesLarger()
  // Input:  <"1" <"2" <"3" <"4" <>>>>>
  // Test:   a SubList containing many elements
  // Result: <"11" <"22" <"33" <"44" <>>>>>
  {
    SubListInterface s = createLargerSubList();
    A06Q3.makeAllPalindromes(s);
    assertEquals("11", s.getRootItem());
    assertEquals("22", s.getRemainder().getRootItem()); 
    assertEquals("33", s.getRemainder().getRemainder().getRootItem()); 
    assertEquals("44", s.getRemainder().getRemainder().getRemainder().getRootItem());
  }
  
  public void testReplaceItemsLargerNone()
  // Input:  s=<"1"<"2"<"3"<"4"<>>>>>, target = "5", changeTo = "XXX"
  // Test:   SubList contains multiple values, and item is none of those values
  // Result: <"1"<"2"<"3"<"4"<>>>>>
  {
    SubListInterface s = createLargerSubList();
    A06Q3.replaceItems(s,"5","XXX");
    assertEquals("1", s.getRootItem());
    assertEquals("2", s.getRemainder().getRootItem()); 
    assertEquals("3", s.getRemainder().getRemainder().getRootItem()); 
    assertEquals("4", s.getRemainder().getRemainder().getRemainder().getRootItem());
  }
  
  public void testReplaceItemsAll()
  // Input:  <"1"<"1"<"1"<"1"<>>>>>
  // Test:   Sublist containing multiple values, all the target value
  // Result: <"XXX"<"XXX"<"XXX"<"XXX"<>>>>>
  {
    SubListInterface s = DataFactory.makeSubList();
    s.setRootItem("1");
    s.getRemainder().setRootItem("1");
    s.getRemainder().getRemainder().setRootItem("1");
    s.getRemainder().getRemainder().getRemainder().setRootItem("1");
    A06Q3.replaceItems(s,"1","XXX");
    assertEquals("XXX", s.getRootItem());
    assertEquals("XXX", s.getRemainder().getRootItem()); 
    assertEquals("XXX", s.getRemainder().getRemainder().getRootItem()); 
    assertEquals("XXX", s.getRemainder().getRemainder().getRemainder().getRootItem());
  }
  
   public void testExtremesLargerMiddleValue()
  // Input:  <75 <40 <80 < 63<>>>>>
  // Test:   Sublist containing multiple items, with the min and max in the middle of the Sublist
  // Result: {40,80}
  {
    SubListInterface s = DataFactory.makeSubList();
    s.setRootItem(75);
    s.getRemainder().setRootItem(40);
    s.getRemainder().getRemainder().setRootItem(80);
    s.getRemainder().getRemainder().getRemainder().setRootItem(63);
    int[] result = A06Q3.extremes(s);
    assertEquals(40, result[0]);
    assertEquals(80, result[1]);
  }
    
  private SubListInterface createLargerSubList()
  // Helper method to create and return the SubList <"1"<"2"<"3"<"4"<>>>>> 
  { SubListInterface s = DataFactory.makeSubList();
    s.setRootItem("1");
    s.getRemainder().setRootItem("2");
    s.getRemainder().getRemainder().setRootItem("3");
    s.getRemainder().getRemainder().getRemainder().setRootItem("4");
    return s;
  }
}
