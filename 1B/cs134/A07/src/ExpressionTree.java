public class ExpressionTree implements ExpressionTreeInterface
{
  
  // Instance Variables
  private Integer term; // If this is a term, stores the value
  private Character operator; // If this is an operator, stores which operator
  private ExpressionTreeInterface leftChild; // Reference to left subtree
  private ExpressionTreeInterface rightChild; // Reference to right subtree
  
  // Creates an expression of just a single term
  public ExpressionTree(int term)
  {
    this.term = term;
    this.operator = null;
    this.leftChild = null;
    this.rightChild = null;
  }
  
  // Creates an expression of the form left op right
  public ExpressionTree(ExpressionTreeInterface left, char op, ExpressionTreeInterface right)
  {
    this.term = null;
    this.operator = new Character(op);
    this.leftChild = left;
    this.rightChild = right;
  }
  
  // post: returns true if this is a term (leaf); false otherwise.
   public boolean isTerm()
   {
     // If the operator variable is null, then this must be a term
     return (operator == null);
   }
   
   // pre:   op is one of {'+', '-', '*', '/'};  b is not null
   // post:  returns an expression tree representing (this op b)
   public ExpressionTreeInterface combine(char op, ExpressionTreeInterface b)
   {
     // Make a tree with this on the left, b on the right, and op as the operator
     return DataFactory.makeExpressionTree(this, op, b);
   }
   
   // post:  returns the number of terms (leaves) in this
   public int numTerms()
   {
     // If this is a term, return 1
     if (this.isTerm())
     {
       return 1;
     }
     // Otherwise return the sum of the left and right subtrees' terms
     else
     {
       return this.leftChild.numTerms() + this.rightChild.numTerms();
     }
   }
   
   // post:  returns a String representing this.  Each subexpression
   //        (an operator together with its children) must be enclosed
   //        in parentheses.
   public String toString()
   {
     // If this is a term, simply return the term
     if (this.isTerm())
     {
       return this.term.toString();
     }
     // Otherwise return (left op right) where left and right are the strings of the subtrees, and op is this node's operator
     else
     {
       return "(" + this.leftChild.toString() + " " + this.operator + " " + this.rightChild.toString() + ")";
     }
   }
   
   // post: returns a list including all of the terms in this
   //       For any subexpression s of this, the left-most term of s 
   //       will have a smaller index in the resulting list than any 
   //       other item from s.
   public ListInterface listOfTerms()
   {
     // Create a list for the terms
     ListInterface list = DataFactory.makeList();
     // Change this into a string
     String string = this.toString();
     // Create a new string to work with
     String newString = "";
     // Create a counter, starting at 0, to go through string
     int current = 0;
     
     // Repeat until we've reached the end of string
     while (current < string.length())
     {
       // If the current character is an operator, skip this character, and the next, since simply skipping the operator would leave a double space
       if (string.charAt(current) == '+' || string.charAt(current) == '-' || string.charAt(current) == '*' || string.charAt(current) == '/')
       {
         current += 2;
       }
       // If this character is a bracket, skip it
       else if (string.charAt(current) == '(' || string.charAt(current) == ')')
       {
         current++;
       }
       // If this character is a space, add it to the new string, and move to the next character
       // We don't have to worry about the spaces before AND after operators, since we skip them in the check for operator
       else if (string.charAt(current) == ' ')
       {
         newString = newString + " ";
         current++;
       }
       // For any other value (should only be numbers), move the current digit to the new string, and move to the next character
       else
       {
         newString += string.charAt(current);
         current++;
       }
     }
     
     // We will now discard string, and use newString
     // newString should have a value of a space delimited list of the terms of this expression, from left to right

     // If there are no spaces, the expression is a term, so simply output the term
     if (newString.indexOf(' ') == -1)
     {
       list.add(1, new Integer(Integer.parseInt(newString)));
     }
     // Otherwise
     else
     {
       // Declare two counter variables
       int index = 0; // index will be used to index characters in newString and keep our place in the string
       int count = 1; // count will keep track of how many terms have been added to the list already
       // Repeat until there are no more spaces in the remainder of newString
       while (newString.indexOf(' ', index) != -1)
       {
         // Find the first term, from index to the next space, change it into an Integer object, and add it to the list
         list.add(count, new Integer(Integer.parseInt(newString.substring(index,newString.indexOf(' ', index)))));
         // Increment the list counter
         count++;
         // Move index to the next space, plus one (the beginning of the next number)
         index = newString.indexOf(' ', index) + 1;
       }
         // If there are no more spaces, then we are still missing the last term, so repeat one more time
         System.out.println("Added term " + newString.substring(index,newString.length()));
         list.add(count, new Integer(Integer.parseInt(newString.substring(index,newString.length()))));
     }
     
     // Finally, return the list
     return list;
   }
   
   // pre:  no subexpression in this results in a division by zero
   // post:  returns the result of evaluating this.  
   public int evaluate()
   {
     // If this is a term, return the value of the term
     if (this.isTerm())
     {
       return term.intValue();
     }
     // Otherwise return the value of the subtrees based on the left and right subtree's evaluation, and this node's operator
     else
     {
       if (this.operator.charValue() == '+')
       {
         return this.leftChild.evaluate() + this.rightChild.evaluate();
       }
       else if (this.operator.charValue() == '-')
       {
         return this.leftChild.evaluate() - this.rightChild.evaluate();
       }
       else if (this.operator.charValue() == '*')
       {
         return this.leftChild.evaluate() * this.rightChild.evaluate();
       }
       else if (this.operator.charValue() == '/')
       {
         return this.leftChild.evaluate() / this.rightChild.evaluate();
       }
     }
     
     // If preconditions are med, this should not be executed
     return -1;
   }
   
   // pre:  n != 0
   // post: returns a new expression tree which is the same as this except
   //       that every term t = i*n such that i is an integer larger than 1
   //       is replaced by the expression (i * n).
   public ExpressionTreeInterface factorBy(int n)
   {
     // If this is a term
     if (this.isTerm())
     {
       // If this term is divisible by n, and is not n itself, then create a tree of the form (value/n * n), and try to factor it again, and return the resulting expression
       if (this.term.intValue()%n == 0 && this.term.intValue() != n)
       {
         return DataFactory.makeExpressionTree(DataFactory.makeExpressionTree(this.term.intValue() / n), '*', DataFactory.makeExpressionTree(n)).factorBy(n);
       }
       // If this term is not divisible by n, or is n, then return a single term expression containing only this term
       else
       {
         return DataFactory.makeExpressionTree(this.term.intValue());
       }
     }
     // If this is an operator, return itself after factoring this' left and right subtrees
     else
     {
       return DataFactory.makeExpressionTree(this.leftChild.factorBy(n), this.operator.charValue(), this.rightChild.factorBy(n));
     }
   }
  
      // overrides equals(Object)
   public boolean equals(Object anotherTree) {
   // pre:  anotherTree is null, anotherTree is castable to ExpressionTree
   // post: returns true iff this.item equals anotherTree.item and the left
   //       and right subtrees are equal      
      assert (anotherTree != null);
      ExpressionTree expr = (ExpressionTree) anotherTree;
      
      // base case: if this is a term, then compare only the item
      if (this.isTerm())
         return (expr.isTerm() && ((Integer) term).equals((Integer) expr.term));
      
      // recursive case: not a term, then compare operator, left and right subtrees
      return (!expr.isTerm() && ((ExpressionTree) leftChild).equals(expr.leftChild) 
               && ((Character) operator).equals((Character) expr.operator) 
               && ((ExpressionTree) rightChild).equals(expr.rightChild));
   }
  
}