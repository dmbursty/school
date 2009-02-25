import junit.framework.TestCase;

public class StackLinkedTest extends TestCase
{
	public void testEmpty()
	{
		// Test creating an empty stack.
		StackInterface s = new StackLinked();
		assertNotNull(s);
		assertEquals(true, s.isEmpty());
	}
	
	public void testPushPopEmpty()
	{
		// Test pushing and popping an element onto an empty stack
		StackInterface s = new StackLinked();
		s.push("TestPush");
		assertEquals(false, s.isEmpty());
		assertEquals("TestPush", s.pop());
	}
	
	public void testPushPopNonEmpty()
	{
		// Test pushing and popping an element onto a non-empty stack
		StackInterface s = new StackLinked();
		s.push("TestPush1");
		s.push("TestPush2");
		assertEquals(false, s.isEmpty());
		assertEquals("TestPush2", s.pop());
		assertEquals("TestPush1", s.pop());
	}
	
	public void testPeekNonEmpty()
	{
		// Test peeking the top element on a non-empty stack.
		StackInterface s = new StackLinked();
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
	{
		StackInterface s = new StackLinked();
		assertEquals("", s.toString());
	}
	
	public void testToStringOneELement()
	{
		StackInterface s = new StackLinked();
		s.push("One");
		assertEquals("One", s.toString());
	}
	
	public void testToStringNonEmpty()
	{
		StackInterface s = new StackLinked();
		s.push("Three");
		s.push("Two");
		s.push("One");
		assertEquals("One Two Three", s.toString());
	}
}
