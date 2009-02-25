import junit.framework.TestCase;

public class TableBSTTest extends TestCase
{

	public void testEmptyTable()
	{	// Input:  none
		// Test:  create an empty table
		// Result: an empty table
		TableInterface tbl = new TableBST();
		assertNotNull(tbl);
		assertEquals(0, tbl.length());
		assertEquals(true, tbl.isEmpty());
		assertEquals("", tbl.toString());
	}
	
	public void testOneElementTable()
	{	// Situation: create a table and insert one element into it
		// Result: a table with one element
		// Check: not null, size is 1, not empty, toString
		TableInterface tbl = this.oneElementTable();
		assertNotNull(tbl);
		assertEquals(1, tbl.length());
		assertEquals(false, tbl.isEmpty());
		assertEquals("One:ItemOne", tbl.toString());
	}
	
	private TableBST oneElementTable()
	{
		TableBST tbl = new TableBST();
		tbl.insert(new KeyedItem("One", "ItemOne"));
		return tbl;
	}
}
