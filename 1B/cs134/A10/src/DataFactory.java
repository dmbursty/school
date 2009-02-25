public class DataFactory
{
   public static BinaryTreeInterface makeBinaryTree()
   {  return new BinaryTree();
   }

   public static BinaryTreeInterface makeBinaryTree(Object root)
   {  return new BinaryTree(root);
   }

   public static BinaryTreeInterface makeBinaryTree(Object root,
         BinaryTreeInterface left, BinaryTreeInterface right)
   {  return new BinaryTree(root, left, right);
   }

   public static TableInterface makeTable()
   {  return new TableBST();
   }

   public static QueueInterface makeQueue()
   {  return new QueueLinked();
   }

   public static StackInterface makeStack()
   {  return new StackLinked();
   }

   public static ListInterface makeList()
   {  return new ListLinked();
   }

   public static SubListInterface makeSubList()
   {  return new SubListRecursive();
   }

   public static SubListInterface makeSubList(Object root)
   {  return new SubListRecursive(root);
   }

   public static SubListInterface makeSubList(Object root, SubListInterface remainder)
   {  return new SubListRecursive(root, remainder);
   }
}
