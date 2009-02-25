import junit.framework.TestCase;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class A5Q1Test extends TestCase {

  /**
   * A test method.
   * (Replace "X" with a name describing the test.  You may write as
   * many "testSomething" methods in this class as you wish, and each
   * one will be called when running JUnit over this class.)
   */
  public void testGivenTestCases() {
    assertEquals(true,A5Q1.checkForNestingAndBalanceIn("pi     = 3.1415926536"),A5Q1.checkForNestingAndBalanceIn("alpha  = { (42 + pi) * (1.5 + [e + 2]^0.42 ) } / 0.71;"));
    assertEquals(true,A5Q1.checkForNestingAndBalanceIn("beta   = { ( + )*([+2]^)}"));
    assertEquals(false,A5Q1.checkForNestingAndBalanceIn("gamma  = [ {1+2) * (3+4} ]"));
  }
  
  public void testEmptyString() {
    assertEquals(true,A5Q1.checkForNestingAndBalanceIn(""));
  }
  
  public void testNoBrackets() {
    assertEquals(true,A5Q1.checkForNestingAndBalanceIn("abc"));
  }
  
  public void testSingleBracket() {
    assertEquals(false,A5Q1.checkForNestingAndBalanceIn("a(b"),A5Q1.checkForNestingAndBalanceIn("a)b"));
    assertEquals(false,A5Q1.checkForNestingAndBalanceIn("a{b"),A5Q1.checkForNestingAndBalanceIn("a}b"));
    assertEquals(false,A5Q1.checkForNestingAndBalanceIn("a[b"),A5Q1.checkForNestingAndBalanceIn("a]b"));
  }
  
  public void testUnbalancedBracket() {
    assertEquals(false,A5Q1.checkForNestingAndBalanceIn("a(b]c)d"),A5Q1.checkForNestingAndBalanceIn("a[b{c]d"));
  }
  
  public void testImproperNesting() {
    assertEquals(false,A5Q1.checkForNestingAndBalanceIn("a(b[c)d]e"));
  }

}
