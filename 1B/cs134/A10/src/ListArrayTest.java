import junit.framework.TestCase;


public class ListArrayTest extends TestCase
{

   private ListArray makeList()
   {  ListArray l = new ListArray();
      l.add(1, "One");
      l.add(2, "Two");
      l.add(3, "Three");
      l.add(4, "Four");
      return l;
   }
	
   public void testCreateEmptyList()
   {  // Test isEmpty, toString, and size methods on an empty list
      ListInterface l = new ListArray();
      assertEquals(true, l.isEmpty());
      assertEquals(0, l.size());
      assertEquals("", l.toString());
   }
 
   public void testNotEmpty()
   {  // Test isEmpty, toString, and size methods on a one-element list
      ListInterface l = new ListArray();
      l.add(1, "One");
      assertEquals(false, l.isEmpty());
      assertEquals(1, l.size());
      assertEquals("One", l.toString());
   }
 
   public void testAddAtEnd()
   {  // this:  ["One"]
      // Add a new element at the end of the list
      // result: ["One", "Two"]
      ListInterface l = new ListArray();
      l.add(1, "One");
      l.add(2, "Two");
      assertEquals("One Two", l.toString());
   }
 
   public void testAddAtBeginning()
   {  // this:  ["Two"]
      // Add a new element at the beginning of the list
      // result: ["One", "Two"]
      ListInterface l = new ListArray();
      l.add(1, "Two");
      l.add(1, "One");
      assertEquals("One Two", l.toString());
   }
 
   public void testAddInMiddle()
   {  // this:  ["One", "Three" "Four"]
      // Add a new element at the beginning of the list
      // result: ["One", "Two", "Three"]
      ListInterface l = new ListArray();
      l.add(1, "One");
      l.add(2, "Three");
      l.add(3, "Four");
      assertEquals("One Three Four", l.toString()); // set up as expected?
      l.add(2, "Two");
      assertEquals("One Two Three Four", l.toString());
   }
   
   public void testGetFromBeginning()
   {  // this: ["One", "Two", "Three", "Four"]
      // Get the first element
   	// returned: "One"
      // result: ["One", "Two", "Three", "Four"]
      ListInterface l = this.makeList();
      assertEquals("One", l.get(1));
      assertEquals("One Two Three Four", l.toString());
   }
   
   public void testGetFromEnd()
   {  // this: ["One", "Two", "Three", "Four"]
      // Get the last element
   	// returned: "Four"
      // result: ["One", "Two", "Three", "Four"]
      ListInterface l = this.makeList();
      assertEquals("Four", l.get(4));
      assertEquals("One Two Three Four", l.toString());
   }
   
   public void testGetFromMiddle()
   {  // this: ["One", "Two", "Three", "Four"]
      // Get the middle (3rd) element
   	// returned: "Three"
      // result: ["One", "Two", "Three", "Four"]
      ListInterface l = this.makeList();
      assertEquals("Three", l.get(3));
      assertEquals("One Two Three Four", l.toString());
   }
 
   public void testRemoveAtBeginning()
   {  // this: ["One", "Two", "Three", "Four"]
      // Remove the first element
      // result: ["Two", "Three", "Four"]
      ListInterface l = this.makeList();
      l.remove(1);
      assertEquals("Two Three Four", l.toString());
   }
 
   public void testRemoveAtEnd()
   {  // this: ["One", "Two", "Three", "Four"]
      // Remove the last element
      // result: ["One", "Two", "Three"]
      ListInterface l = this.makeList();
      l.remove(4);
      assertEquals("One Two Three", l.toString());
   }
 
   public void testRemoveFromMiddle()
   {  // this: ["One", "Two", "Three", "Four"]
      // Remove a middle element
      // result: ["One", "Three", "Four"]
      ListInterface l = this.makeList();
      l.remove(2);
      assertEquals("One Three Four", l.toString());
   }
 
   public void testRemoveAll()
   {  // this: ["One", "Two", "Three", "Four"]
      // Remove all elements
      // result: []
      ListInterface l = this.makeList();
      assertEquals(4, l.size());
      l.removeAll();
      assertEquals(0, l.size());
      assertEquals(true, l.isEmpty());
      assertEquals("", l.toString());
	 
      // can still add properly?
      l.add(1, "One");
      assertEquals("One", l.toString());
   }
}
