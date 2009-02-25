/** 
 * Represent a quadratic function of the form f(x) = ax^2 + bx + c
 * @author Daniel Burstyn - 20206120
 */
public class QuadraticFunction
{ 
  // Instance variables
  private double a, b, c; // the coefficients

  
  /** 
   * Construct a new function by setting the coefficients. 
   * @param a the coefficient for x^2
   * @param b the coefficient for x^1
   * @param c the coefficient for x^0
   */
  public QuadraticFunction(double a, double b, double c)
  { 
    this.a = a;
    this.b = b;
    this.c = c;
  } // end constructor

  /**
   * Evaluate this function for the given value: 
   * f(x) = ax^2 + bx + c. 
   * @param val the value to substitute in
   * @return the function evaluated at val
   */
  public double evaluate(double val)
  { 
    return a*val*val + b*val + c;
  } // end evaluate

  /** 
   * Test for equality.
   * @param other the function to test. pre: NONE
   * @return true iff this and other represent the same 
   * quadratic function (i.e. they evaluate to the same value
   * for every value of x)
   */
  public boolean equals(QuadraticFunction other) 
  { 
    return (a == other.getCoeff(2) && b == other.getCoeff(1) && c == other.getCoeff(0));
  } // end equals

  /** 
   * Return a string representation of this. 
   * <p> Examples: 
   * For a=-4.0, b=-3.2, c=2.1, 
   * the string representation is
   * <p>
   * "f(x) = -4.0x^2 - 3.2x + 2.1"
   * <p>
   * and for a=34.67, b=1.0, c=0.0, the string representation
   * is
   * <p>
   * "f(x) = 34.67x^2 + 1.0x + 0.0"
   * @return the string representation of this function.
   */
  public String toString()
  { 
    return "f(x) = " + a + "x^2 " + ((b < 0) ? "-" : "+") + " " + Math.abs(b) + "x " + ((c < 0) ? "-" : "+") + " " + Math.abs(c);
  } // end toString

  /** 
   * Get the coefficients for the 2nd, 1st and 0th powers of x. 
   * @param which the coefficient to get. pre: which=0,1,2
   * @return the corresponding coefficient. 
   */
  public double getCoeff(int which)
  { 
    if (which == 2)
    {
      return a;
    }
    else if (which == 1)
    {
      return b;
    }
    else
    {
      return c;
    }
  } // end getCoeff

  /** 
   * Determine whether the roots of this are real. 
   * @return true iff the roots of this are real. 
   */
  public boolean hasRealRoots() 
  { 
    return ((b*b - 4*a*c) >= 0);
  } // end hasRealRoots

  /**
   * Get the root of this that is larger numerically, or 
   * one of the roots if both roots are equal.
   * pre: ???
   * @return a larger root.
   */
  public double getBiggerRoot() 
  { 
    return (-1*b + Math.sqrt(b*b - 4*a*c))/(2*a);
  } // end getBiggerRoot

  /**
   * Get the root of this that is smaller numerically, or 
   * one root if both roots are equal.
   * pre: ???
   * @return a smaller root
   */
  public double getSmallerRoot()
  { 
    return (-1*b - Math.sqrt(b*b - 4*a*c))/(2*a);
  } // end getSmallerRoot
  
    /** 
   * Run some hard-coded tests on the class.
   */
  public static void main(String[] args) 
  { 
      // Make some functions
      // The zero quadratic
      QuadraticFunction q1 = new QuadraticFunction(0.0, 0.0, 0.0);

      // Two identical ones
      QuadraticFunction q2 = new QuadraticFunction(9.2, 1.0, -3.0);
      QuadraticFunction q3 = new QuadraticFunction(9.2, 1.0, -3.0);

      // Some with varying signs on the coeffs
      QuadraticFunction q4 = new QuadraticFunction(-4.7, 3.2, -17.0);
      QuadraticFunction q5 = new QuadraticFunction(4.7, -3.2, 17);
      QuadraticFunction q6 = new QuadraticFunction(4.7, 3.2, 17);

      System.out.println();
      System.out.println("**Testing toString");
      System.out.println(q1);
      System.out.println(q2);
      System.out.println(q3);
      System.out.println(q4);
      System.out.println(q5);
      System.out.println(q6);

      System.out.println();
      System.out.println("**Testing getCoeff");
      System.out.println(q1 + 
          ": a = " + q1.getCoeff(2) + ", b = " + q1.getCoeff(1)
          + ", c = " + q1.getCoeff(0));

      System.out.println(q2 + 
          ": a = " + q2.getCoeff(2) + ", b = " + q2.getCoeff(1)
          + ", c = " + q2.getCoeff(0));

      System.out.println(q4 + 
          ": a = " + q4.getCoeff(2) + ", b = " + q4.getCoeff(1)
          + ", c = " + q4.getCoeff(0));


      System.out.println();
      System.out.println("**Testing evaluate");
      System.out.println(q1 + " at " + 2.3 + " = " + q1.evaluate(2.3));
      System.out.println(q1 + " at " + -7.4 + " = " + q1.evaluate(-7.4));
      System.out.println(q1 + " at " + 0.0 + " = " + q1.evaluate(0.0));

      System.out.println(q2 + " at " + 2.3 + " = " + q2.evaluate(2.3));
      System.out.println(q2 + " at " + -7.4 + " = " + q2.evaluate(-7.4));
      System.out.println(q2 + " at " + 0.0 + " = " + q2.evaluate(0.0));

      System.out.println(q5 + " at " + 2.3 + " = " + q5.evaluate(2.3));
      System.out.println(q5 + " at " + -7.4 + " = " + q5.evaluate(-7.4));
      System.out.println(q5 + " at " + 0.0 + " = " + q5.evaluate(0.0));

      System.out.println();
      System.out.println("**Testing hasRealRoots"); 
      
      System.out.println(q1 + " has real roots? " + q1.hasRealRoots());
      System.out.println(q3 + " has real roots? " + q2.hasRealRoots());
      System.out.println(q4 + " has real roots? " + q4.hasRealRoots());
      System.out.println(q5 + " has real roots? " + q5.hasRealRoots());
      System.out.println(q6 + " has real roots? " + q6.hasRealRoots());

      System.out.println();
      System.out.println("**Testing getBiggerRoot, getSmallerRoot");

      System.out.println(q1 + ": big = " + q1.getBiggerRoot() 
          + ", small = " + q1.getSmallerRoot());

      System.out.println(q3 + ": big = " + q3.getBiggerRoot() 
          + ", small = " + q3.getSmallerRoot());

      System.out.println();
      System.out.println("**Testing equals");

      System.out.println(q1 + ".equals(" + q1 + ")? " + 
         q1.equals(q1));

      System.out.println(q1 + ".equals(" + q2 + ")? " + 
         q1.equals(q2));

      System.out.println(q2 + ".equals(" + q3 + ")? " + 
         q2.equals(q3));

      System.out.println(q4 + ".equals(" + q5 + ")? " + 
         q4.equals(q5));

  } // end main 

} // end class
