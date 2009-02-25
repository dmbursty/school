import junit.framework.TestCase;


public class QueueLinkedTest extends TestCase
{

   public void testEmptyQueue()
   {  // Test the construction of an empty queue.
   	QueueInterface q = new QueueLinked();
      assertNotNull(q);
      assertEquals(true, q.isEmpty());
   }
	
   public void testOneElementQueue()
   { 	// Test enqueuing and dequeuing a single element
   	QueueInterface q = new QueueLinked();
      q.enqueue("One");
      assertEquals(false, q.isEmpty());
      assertEquals("One", q.dequeue());
   }
	
   public void testTwoElementQueue()
   {  // Test enquing and dequeuing in a queue that already has one element
   	QueueInterface q = new QueueLinked();
      q.enqueue("One");
      q.enqueue("Two");
      assertEquals(false, q.isEmpty());
      assertEquals("One", q.peek());
      assertEquals("One", q.dequeue());
      assertEquals("Two", q.peek());
      assertEquals("Two", q.dequeue());
   }
	
   public void testDequeueAll()
   {  // Test dequeuing all elements.
   	QueueInterface q = new QueueLinked();
      q.enqueue("One");
      q.enqueue("Two");
      q.dequeueAll();
      assertEquals(true, q.isEmpty());
   }
   
   public void testToStringEmpty()
   {
   	// Test toString on an emtpy queue
   	QueueInterface q = new QueueLinked();
   	assertEquals("", q.toString());
   }
   
   public void testToStringNonEmpty()
   {
   	// Test toString on an emtpy queue
   	QueueInterface q = new QueueLinked();
      q.enqueue("One");
      q.enqueue("Two");
      q.enqueue("Three");
   	assertEquals("One Two Three", q.toString());
   }
}
