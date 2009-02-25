import junit.framework.TestCase;

/**
 * A JUnit test case class which includes methods to test the method replace.
 * Note: ListArray can hold any type of objects. The examples below are, for simplicity, working 
 * with String values. You may test with other types as well.
 */
public class TestReplace extends TestCase {

  /* Helper method: Used to fill an existing list with a specific set of values used for testing
   * post: l contains 8 values: "ABC", "DEF", "DEF", "GHI", "DEF", "IJK", "DEF", "ABC"
   */
  private ListArray makeLargerList() {
    ListArray l = new ListArray();
    l.add(1,"ABC");
    l.add(2,"DEF");
    l.add(3,"DEF");
    l.add(4,"GHI");
    l.add(5,"DEF");
    l.add(6,"IJK");
    l.add(7,"DEF");
    l.add(8,"ABC");
    return l;
  }
  
  /* testEmptyList
   * Input: an empty list, and a value (obviously!) not in the list 
   * Test: replace an entry in an empty list
   * Result: list is still empty, 0 returned
   */
  public void testEmptyList() {
    // create list
    ListArray list = new ListArray();
    // call replace, saving value returned
    int count = UsingListArray.replace(list, "A","X");
    // compare expected result to actual result
    assertEquals(true,list.isEmpty());
    assertEquals(0,count);
  }
  
  /* testListSizeOneNotThere
   * Input: a list with one entry, and a value not in the list
   * Test: replace an entry in a list of size one, entry not in the list
   * Result: list is not changed, 0 returned
   */
  public void testListSizeOneNotThere() {
    ListArray list = new ListArray();
    // create list containing a single entry
    list.add(1,"One");
    // attempt to replace entry not in list
    int count = UsingListArray.replace(list,"Two","Not there");
    // compare expected result to actual result
    assertEquals(0,count);
    assertEquals(list.get(1),"One");
    assertEquals(1,list.size());
  }
  
  /* testListSizeOneThere
   * Input: a list with one entry, and the value in the list 
   * Test: replace the single entry in the list
   * Result: list entry updated, 1 returned
   */
  public void testListSizeOneThere() {
    ListArray list = new ListArray();
    // create list containing a single entry
    list.add(1,"One");
    // attempt to replace that entry with a replacement value
    int count = UsingListArray.replace(list,"One","There");
    // compare expected result to actual result
    assertEquals(1,count);
    assertEquals(list.get(1),"There");
    assertEquals(1, list.size());
  }
  
  /* testLargerListReplaceNone
   * Input: a larger list, and the value in the list 
   * Test: replace an entry not in the list
   * Result: list is unchanged, 0 returned
   */
  public void testLargerListReplaceNone() {
    // create a larger list
    ListArray list = makeLargerList();
    int origSize = list.size();
    // call replace, attempt to replace an entry not in the list, save returned value
    int count = UsingListArray.replace(list, "XXX", "YYYYY");
    // compare expected result to actual result
    assertEquals(origSize, list.size());
    assertEquals(0, count);
    // note: since tester can see original values in helper method, we know "YYYYY" is not in the original list
    for (int i=1; i < list.size(); i++)
    { assertEquals(false, list.get(i).equals("YYYYY"));
    }
  }
  
  /* testLargerListReplaceOne
   * Input: a larger list, and a value which occurs once in the list  
   * Test: replace a single value in a larger list
   * Result: single list entry updated, 1 returned
   */
  public void testLargerListReplaceOne() {
    // create a larger list
    ListArray list = makeLargerList();
    int origSize = list.size();
    // call replace, attempt to replace an entry in the list once, save returned value
    int count = UsingListArray.replace(list, "GHI", "NEW");
    // compare expected result to actual result
    assertEquals(origSize, list.size());
    assertEquals(1, count);
    // note: since tester can see original values in helper method, we know "NEW" is not in the original list
    // and, we know "GHI" was at position 4
    for (int i=1; i < list.size(); i++)
    { if (i==4)
      { assertEquals(true, list.get(i).equals("NEW"));
      } else
      { assertEquals(false, list.get(i).equals("NEW"));
      }
    }  
  }
  
  /* testLargerListReplaceSome
   * Input: a larger list, and a value which occurs more than once in the list  
   * Test: replace multiple values in a larger list
   * Result: multiple entries in list updated, correct value returned
   */
  public void testLargerListReplaceSome() {
    // create a larger list
    ListArray list = makeLargerList();
    int origSize = list.size();
    // call replace, attempt to replace an entry in the list several times, save returned value
    int count = UsingListArray.replace(list, "DEF", "NEW");
    // compare expected result to actual result
    assertEquals(origSize, list.size());
    assertEquals(4, count);
    // note: since tester can see original values in helper method, we can compare entries one at a time
    assertEquals("ABC", list.get(1));
    assertEquals("NEW", list.get(2));
    assertEquals("NEW", list.get(3));
    assertEquals("GHI", list.get(4));
    assertEquals("NEW", list.get(5));
    assertEquals("IJK", list.get(6));
    assertEquals("NEW", list.get(7));
    assertEquals("ABC", list.get(8));
  }
  
  /* testLargerListReplaceAll
   * Input: a larger list in which all entries are the same, the common value  
   * Test: replace all values in a larger list
   * Result: all list entries updated, list.size() returned
   */
  public void testLargerListReplaceAll() {
    // initialize list to contain 10 copies of the value "A"
    ListArray list = new ListArray();
    String entry = "A";
    int size = 10;
    for (int i=1; i<=size; i++)
    { list.add(i,entry);
    }
    // call replace to change all values in list to "All" from "A"
    int count = UsingListArray.replace(list,entry, "All");
    // compare expected answer to actual answer
    assertEquals(size,list.size());
    assertEquals(size,count);
    for (int i=1; i <= size; i++)
    { assertEquals(list.get(i),"All");
    }
  }
  
  /* testEqualityOnly
   * Input: a larger list, an entry similar to but not equal to a value in the list
   * Test: replace matches with equals (but not equalsIgnoreCase)
   * Result: list is unchanged
   */
  public void testEqualityOnly()
  { // create list
    ListArray list = makeLargerList();
    // call replace with a value similar to, but not equal, to a list entry
    int count = UsingListArray.replace(list, "abc", "XXX");
    // compare expected answer to actual answer
    assertEquals(0,count);
  }
}
