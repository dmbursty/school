import junit.framework.TestCase;

public class StackArrayTest extends TestCase
{
   public void testEmpty()
   {  // Input: nothing
      // Test: create an empty stack
      // Result: an empty stack
      StackInterface s = new StackArray();
      assertNotNull(s);
      assertEquals(true, s.isEmpty());
   }
   
   public void testPushPopEmpty()
   {  // Input: an empty stack
      // Test: pushing one value on empty stack
      // Result: pop off the pushed item
      StackInterface s = new StackArray();
      s.push("TestPush");
      assertEquals(false, s.isEmpty());
      assertEquals("TestPush", s.pop());
   }
   
   public void testPushPopNonEmpty()
   {  // Input: a non-empty stack
      // Test: push onto a non-empty stack
      // Result: pop the pushed item; rest of stack is the same as before
      StackInterface s = new StackArray();
      s.push("TestPush1");
      s.push("TestPush2");
      assertEquals(false, s.isEmpty());
      assertEquals("TestPush2", s.pop());
      assertEquals("TestPush1", s.pop());
   }
   
   public void testPeekNonEmpty()
   {  // Input: a non-empty stack
      // Test: peek at the top item of a non-empty stack
      // Result: peek at the top item; stack is unchanged
      StackInterface s = new StackArray();
      s.push("TestPeek1");
      assertEquals("TestPeek1", s.peek());
      s.push("TestPeek2");
      assertEquals("TestPeek2", s.peek());
      assertEquals(false, s.isEmpty());
      // do pops still work correctly?
      assertEquals("TestPeek2", s.pop());
      assertEquals("TestPeek1", s.pop());
   }
   
   public void testToStringEmpty()
   {  // Input: []
      // Test: toString with empty stack
      // Result: ""
      StackInterface s = new StackArray();
      assertEquals("", s.toString());
   }
   
   public void testToStringOneELement()
   {  // Input: ["One"]
      // Test: toString with one element
      // Result: "One"
      StackInterface s = new StackArray();
      s.push("One");
      assertEquals("One", s.toString());
   }
   
   public void testToStringNonEmpty()
   {  // Input: ["One", "Two", "Three"]
      // Test: toString
      // Result: "One Two Three"
      StackInterface s = new StackArray();
      s.push("Three");
      s.push("Two");
      s.push("One");
      assertEquals("One Two Three", s.toString());
   }

   public void testGrowIfNecessary()
   {  // Input:  [] (with zero size)
      // Test: push several values; check if stack grows
      // Result: ["One", "Two", "Three", "Four", "Five"]
      StackInterface s = new StackArray(2);
      s.push("Five");
      s.push("Four");
      s.push("Three");
      s.push("Two");
      s.push("One");
      assertEquals("One Two Three Four Five", s.toString());     
   }
   
   public void testPopAll()
   {  // Input: ["One", "Two", "Three"]
      // Test: pop all items
      // Result: []
      StackInterface s = new StackArray();
      s.push("Three");
      s.push("Two");
      s.push("One");
      s.popAll();
      assertEquals(true, s.isEmpty());
      
      // still works to push
      s.push("One");
      assertEquals("One", s.toString());          
   }
}
