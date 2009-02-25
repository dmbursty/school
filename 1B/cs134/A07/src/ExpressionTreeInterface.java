public interface ExpressionTreeInterface
{

   // post: returns true if this is a term (leaf); false otherwise.
   public boolean isTerm();
   
   // pre:   op is one of {'+', '-', '*', '/'};  b is not null
   // post:  returns an expression tree representing (this op b)
   public ExpressionTreeInterface combine(char op, ExpressionTreeInterface b);
   
   // post:  returns the number of terms (leaves) in this
   public int numTerms();
   
   // post:  returns a String representing this.  Each subexpression
   //        (an operator together with its children) must be enclosed
   //        in parentheses.
   public String toString();
   
   // post: returns a list including all of the terms in this
   //       For any subexpression s of this, the left-most term of s 
   //       will have a smaller index in the resulting list than any 
   //       other item from s.
   public ListInterface listOfTerms();
   
   // pre:  no subexpression in this results in a division by zero
   // post:  returns the result of evaluating this.  
   public int evaluate();
   
   // pre:  n != 0
   // post: returns a new expression tree which is the same as this except
   //       that every term t = i*n such that i is an integer larger than 1
   //       is replaced by the expression (i * n).
   public ExpressionTreeInterface factorBy(int n);
}