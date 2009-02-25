import junit.framework.TestCase;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 * 
 * Provides initial (but incomplete) testing of the methods required for Assignment 6,
 * Question 3.
 */
public class TestA06Q3Methods extends TestCase {
  /**
   * A test method.
   * (Replace "X" with a name describing the test.  You may write as
   * many "testSomething" methods in this class as you wish, and each
   * one will be called when running JUnit over this class.)
   */
  
  public void testMakePalindromeEmpty() {
  // Input:  ""
  // Test:   empty string (boundary, special case)
  // Result: returns empty string
    assertEquals("",A06Q3.makePalindrome(""));
  }
  
  public void testMakePalindromeLonger() {
  // Input:  "abcd"
  // Test:   a longer input string (typical case)
  // Result: "abcddcba"
    assertEquals("abcddcba", A06Q3.makePalindrome("abcd"));
  }
  
  public void testGCDEqual() {
  // Input:  a=6,b=6
  // Test:   numbers are equal (special case)
  // Result: returns 6 (common value of a and b)
    assertEquals(6,A06Q3.gcd(6,6)); 
  }
  
  public void testGCDRelativelyPrime() {
  // Input: a=7, b=13  
  // Test:  a and b are relatively prime 
  // Result: returns 1
    assertEquals(1,A06Q3.gcd(7,13)); 
  }
  
  private SubListInterface createSingletonSubList()
  // Helper method to create and return the SubList <A<>>
  { SubListInterface s = DataFactory.makeSubList();
    s.setRootItem("A");
    return s;
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
  
  public void testMakeAllPalindromesEmpty()
  // Input:  <> 
  // Test:   SubList is empty (boundary case)
  // Result: SubList remains empty
  { SubListInterface s = DataFactory.makeSubList();
    A06Q3.makeAllPalindromes(s);
    assertEquals(true, s.isEmpty()); 
  }
  
  public void testMakeAllPalindromesSingleton()
  // Input:  <"A" <>>
  // Test:   a SubList containing one value only (special case)
  // Result: <"AA" <>>
  { SubListInterface s = createSingletonSubList();
    A06Q3.makeAllPalindromes(s);
    assertEquals("AA", s.getRootItem());
    assertEquals(true, s.getRemainder().isEmpty());
  }
  
  public void testReplaceItemsSingletonThere()
  // Input:  s=<"A"<>>, target = "A", changeTo="XXX"
  // Test:   SubList contains one item only, try to replace that value
  // Result: s=<"AA" <>>
  { SubListInterface s = createSingletonSubList();
    A06Q3.replaceItems(s, "A", "XXX");
    assertEquals("XXX", s.getRootItem());
    assertEquals(true, s.getRemainder().isEmpty());
  }
  
  public void testReplaceItemsLargerOnce()
  // Input:  s=<"1"<"2"<"3"<"4"<>>>>>, target = "1", changeTo = "XXX"
  // Test:   SubList contains multiple value (typical), and item is one of those values
  // Result: s=<"XXX"<"2"<"3"<"4"<>>>>> (first entry changed)
  { SubListInterface s = createLargerSubList();
    A06Q3.replaceItems(s,"1","XXX");
    assertEquals("XXX", s.getRootItem());
    assertEquals("2", s.getRemainder().getRootItem()); 
    assertEquals("3", s.getRemainder().getRemainder().getRootItem()); 
    assertEquals("4", s.getRemainder().getRemainder().getRemainder().getRootItem());
  }
  
  private SubListInterface createSingletonIntegerSubList()
  // Helper method which creates and returns the SubList <100 <>>
  { SubListInterface s = DataFactory.makeSubList();
    s.setRootItem(100);
    return s;
  }
  
  private SubListInterface createLargerIntegerSubListSame()
  // Helper method which creates and returns the SubList <100 <100 <100 <>>>>
  { SubListInterface s = DataFactory.makeSubList();
    s.setRootItem(100);
    s.getRemainder().setRootItem(100);
    s.getRemainder().getRemainder().setRootItem(100);
    s.getRemainder().getRemainder().getRemainder().setRootItem(100);
    return s;
  }
  
  public void testExtremesSingleton()
  // Input:  <100 <>> 
  // Test:   SubList of size one (boundary case)
  // Result: array {100,100} (single value is both minimum and maximum)
  { SubListInterface s = createSingletonIntegerSubList();
    int[] result = A06Q3.extremes(s);
    assertEquals(100, result[0]);
    assertEquals(100, result[1]);
  }
  
  public void testExtremesLargerZeroDiff()
  // Input:  <100 <100 <100 <>>>>
  // Test:   SubList contains multiple items (typical case), all values equal (special case)
  // Result: {100,100} (common value is both minimum and maximum)
  { SubListInterface s = createLargerIntegerSubListSame();
    int[] result = A06Q3.extremes(s);
    assertEquals(100, result[0]);
    assertEquals(100, result[1]);
  }
}
