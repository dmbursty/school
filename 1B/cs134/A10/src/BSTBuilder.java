
public class BSTBuilder 
{

	public static BinaryTreeInterface buildBalancedBST(Comparable[] items, int n)
	{	// pre:  items is non-null; n >= 0; values in items[0..n-1] are unique
		// post: returns a balanced binary search tree constructed from the 
		//       items in items[0..n-1] such that for each subtree, the size 
		//       of the left and right subtrees differ by at most one.
       
		BSTBuilder.sort(items, n);
		return BSTBuilder.buildBSTFromSortedData(items, 0, n-1);
	}
	
	public static void sort(Comparable[] items, int n)
	{	//	 pre:  items is non-null; n >= 0; values in items[0..n-1] are unique
		//	 post: values in items have been rearranged to satisfy 
		//	       items[i] < items[i+1], for i=0..n-2.
		
		// Inertion sort
		for (int unsorted = 1; unsorted < n; unsorted++)  
		{	Comparable nextItem = items[unsorted]; 
		  	int loc = unsorted; 
		 
		  	while ((loc > 0) && (items[loc-1].compareTo(nextItem) > 0)) 
		  	{	items[loc] = items[loc-1]; 
		  		loc--; 
		  	} 
		  
		  	items[loc] = nextItem; 
		} 
	}
	
	public static BinaryTreeInterface buildBSTFromSortedData(Comparable[] items, int left, int right)
	{	// pre:  items is non-null; items[i] < items[i+1], for i=left..right-1.
	   //       values in items are unique.
		// post: returns a balanced binary search tree constructed from the 
		//       items in items[left..right] such that for each subtree, the size 
		//       of the left and right subtrees differ by at most one.

	   // YOUR CODE GOES HERE
          
          BinaryTreeInterface tree = DataFactory.makeBinaryTree();
          
          if (left > right)
          {
            // If there are no elements, leave an empty tree
          }
          else if (left == right)
          {
            // If there is only one value, make a leaf
            tree.setRootItem(items[left]);
          }
          else if (right - left == 1)
          {
            // If there are only two values, make a root, with a leaf on the right
            tree.setRootItem(items[left]);
            tree.attachRight(items[right]);
          }
          else
          {
            // If there are more than two, set the midpoint to the root, and recursively add each
            // half of the array to the tree
            int mid = (left + right)/2;
            tree.setRootItem(items[mid]);
            tree.attachLeftSubtree(buildBSTFromSortedData(items,left,mid-1));
            tree.attachRightSubtree(buildBSTFromSortedData(items,mid+1,right));
          }
          
          // Return the tree
          return tree;
	}
}

