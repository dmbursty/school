import junit.framework.TestCase;


public class QueueArrayTest extends TestCase
{

   public void testEmptyQueue()
   {  QueueInterface q = new QueueArray(10);
      assertNotNull(q);
      assertEquals(true, q.isEmpty());
      assertEquals("", q.toString());
   }
	
   public void testOneElementQueue()
   {  QueueInterface q = new QueueArray(10);
      q.enqueue("One");
      assertEquals(false, q.isEmpty());
      assertEquals("One", q.toString());
      assertEquals("One", q.dequeue());
   }
	
   public void testTwoElementQueue()
   {  QueueInterface q = new QueueArray(10);
      q.enqueue("One");
      q.enqueue("Two");
      assertEquals(false, q.isEmpty());
      assertEquals("One Two", q.toString());
      assertEquals("One", q.peek());
      assertEquals("One", q.dequeue());
      assertEquals("Two", q.peek());
      assertEquals("Two", q.dequeue());
   }
	
   public void testDequeueAll()
   {  QueueInterface q = new QueueArray(10);
      q.enqueue("One");
      q.enqueue("Two");
      q.dequeueAll();
      assertEquals(true, q.isEmpty());
   }

   public void testWrappingAroundEnd()
   {  // this:  a queue that has "migrated" down the array
      // Add more items so items wrap around from the end to the beginning
      QueueInterface q = new QueueArray(4);
      q.enqueue("Zero");
      q.enqueue("One");
      q.enqueue("Two");
      assertEquals("Zero", q.dequeue());
      q.enqueue("Three"); // last item in array is now filled
      q.enqueue("Four"); // wrapped around end; array is full
      assertEquals("One Two Three Four", q.toString());
      assertEquals("One", q.dequeue());
   	
      q.enqueue("Five"); // array is full again
      q.enqueue("Six"); // array should have enlarged
      assertEquals("Two", q.dequeue());
      assertEquals("Three", q.dequeue());
      assertEquals("Four", q.dequeue());
      assertEquals("Five", q.dequeue());
      assertEquals("Six", q.dequeue());
      assertEquals(true, q.isEmpty());
   }
}
