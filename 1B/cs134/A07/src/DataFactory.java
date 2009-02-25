public class DataFactory
{
   public static ExpressionTreeInterface makeExpressionTreeBonus(int term)
   // post: returns a new expression tree consisting of a single term
   {
     return new ExpressionTerm(term);
   }

   public static ExpressionTreeInterface makeExpressionTreeBonus(ExpressionTreeInterface left, 
                                                  char op, 
                                                  ExpressionTreeInterface right)
   // pre:  left and right are not null;  op is one of {+,-,*,/}
   // post: returns a new expression tree consisting of the give left and
   //       right subexpressions and the given operator.
   {
     return new ExpressionTreeBonus(left, op, right);
   }
                                                  
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
     
   public static BinaryTreeInterface2 makeBinaryTree2()
   { return new BinaryTree2();
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
   
     public static ExpressionTreeInterface makeExpressionTree(int term)
  // post: returns a new expression tree consisting of a single term
  {
    return new ExpressionTree(term);
  }

  public static ExpressionTreeInterface makeExpressionTree(ExpressionTreeInterface left, 
                                                  char op, 
                                                  ExpressionTreeInterface right)
  // pre:  left and right are not null;  op is one of {+,-,*,/}
  // post: returns a new expression tree consisting of the give left and
  //       right subexpressions and the given operator.
  {
    return new ExpressionTree(left, op, right);
  }
}
