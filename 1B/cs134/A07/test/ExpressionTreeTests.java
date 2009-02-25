import junit.framework.*;
public class ExpressionTreeTests extends TestCase {
  
  // Creates an expression of a single term
  public ExpressionTreeInterface makeTerm()
  {
    return DataFactory.makeExpressionTree(12);
  }
  
  // Creates an expression
  public ExpressionTreeInterface makeExpression()
  {
    return DataFactory.makeExpressionTree(DataFactory.makeExpressionTree(DataFactory.makeExpressionTree(3), '*', DataFactory.makeExpressionTree(2)), '+', DataFactory.makeExpressionTree(5));
  }
  
  public void testIsTerm() {
  // Input:  12
  // Test:   If a term is a term
  // Result: returns true
    ExpressionTreeInterface tree = makeTerm();
    assertEquals(true, tree.isTerm());
  }
  
  public void testIsTermExpression() {
  // Input:  ((3 * 2) + 5)
  // Test:   If an expression is a term
  // Result: returns false
    ExpressionTreeInterface tree = makeExpression();
    assertEquals(false, tree.isTerm());
  }
  
  public void testCombine1() {
  // Input:  ((3 * 2) + 5) and 12
  // Test:   Combining expressions
  // Result: (((3 * 2) + 5) * 12)
    ExpressionTreeInterface tree = makeTerm().combine('*', makeExpression());
    assertEquals("(12 * ((3 * 2) + 5))", tree.toString());
    tree = makeExpression().combine('*', makeTerm());
    assertEquals("(((3 * 2) + 5) * 12)", tree.toString());
  }
  
  public void testCombine2() {
  // Input: 12 and  ((3 * 2) + 5)
  // Test:   Combining expressions
  // Result: (12 * ((3 * 2) + 5))
    ExpressionTreeInterface tree = makeTerm().combine('*', makeExpression());
    assertEquals("(12 * ((3 * 2) + 5))", tree.toString());
    tree = makeExpression().combine('*', makeTerm());
    assertEquals("(((3 * 2) + 5) * 12)", tree.toString());
  }
  
  public void testNumTermsTerm() {
  // Input:  12
  // Test:   number of terms of a term
  // Result: returns 1
    ExpressionTreeInterface tree = makeTerm();
    assertEquals(1, tree.numTerms());
  }
  
  public void testNumTermsExpression() {
  // Input:  ((3 * 2) + 5)
  // Test:   number of terms of an expression
  // Result: returns 3
    ExpressionTreeInterface tree = makeExpression();
    assertEquals(3, tree.numTerms());
  }
  
  public void testListOfTermsTerm() {
  // Input:  12
  // Test: listing terms of a single term
  // Result: <12>
    ExpressionTreeInterface tree = makeTerm();
    assertEquals("12", tree.listOfTerms().toString());
  }
  
  public void testListOfTermsExpression() {
  // Input:  ((3 * 2) + 5)
  // Test:   listing terms of an expression
  // Result: <3 2 5>
    ExpressionTreeInterface tree = makeExpression();
    assertEquals("3 2 5", tree.listOfTerms().toString());
  }
  
  public void testFactorByTerm() {
  // Input:  12
  // Test:   factor a single term
  // Result: ((3 * 2) * 2)
    ExpressionTreeInterface tree = makeTerm();
    assertEquals("((3 * 2) * 2)", tree.factorBy(2).toString());
  }
  
  public void testFactorByExpression() {
  // Input:  ((3 * 2) + 5)
  // Test:   factor an expression combined with a term
  // Result: (((3 * 2) + 5) * ((3 * 2) * 2))
    ExpressionTreeInterface tree = makeExpression();
    assertEquals("(((3 * 2) + 5) * ((3 * 2) * 2))", tree.combine('*', makeTerm()).factorBy(2).toString());
  }

  
}
