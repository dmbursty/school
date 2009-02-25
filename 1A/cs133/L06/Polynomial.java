public class Polynomial
{
  private double[] coefficients;
  
  public Polynomial(double[] coefficients)
  {
    if (coefficients == null)
    {
      // For simplicity, we treat a null value as the polynomial "0".
      this.coefficients = new double[1];
      this.coefficients[0] = 0.0;
    }
    else
    {
      /* We make a copy of the coefficient array to prevent changes
       from outside this class. */
      this.coefficients = new double[coefficients.length];
      for (int i = 0; i < coefficients.length; i++)
      {
        this.coefficients[i] = coefficients[i];
      }
    }
  }
  
  public Polynomial(double a, double b, double c)
  {
    if (a != 0) {
      this.coefficients = new double[3];
      this.coefficients[2] = a;
      this.coefficients[1] = b;
      this.coefficients[0] = c;
    }
    else if (b != 0) 
    {
      this.coefficients = new double[2];
      this.coefficients[1] = b;
      this.coefficients[0] = c;
    }
    else
    {
      this.coefficients = new double[1];
      this.coefficients[0] = c;
    }
  }
  
  public int degree()
  {
    return coefficients.length - 1;
  }
  
  public double eval(double x)
  {
    int total = 0;
    for (int i = 0; i < coefficients.length; i++)
    {
      total += coefficients[i] * Math.pow(x,i);
    }
    return total;
  }
  
  public String toString()
  {
    String output = "";
    
    // Repeat for every term in the polynomail
    for (int i = coefficients.length - 1; i >= 0; i--)
    {
      // As long as the term has a non-zero constant, or the polynomial contains only a constant
      // This second part ensures that the string will be output for a polynomial equal to just zero
      if (coefficients[i] != 0 || coefficients.length == 1) 
      {
        // If this is the first term in the polynomial, format the sign a certain way
        if (i == coefficients.length - 1)
        {
          output += (coefficients[i] < 0 ? "-" : "");
        }
        
        // Otherwise, format the sign differently
        else
        {
          output += (coefficients[i] < 0 ? " - " : " + ");
        }
        
        // Add the constant to the string as long as it is not 1 or -1
        if (Math.abs(coefficients[i]) != 1 || i == 0)
        {
        output += Math.abs(coefficients[i]);
        }
        
        // If this is the x^1 term, omit the "^1"
        if (i == 1)
        {
          output += "x";
        }
        
        // If this is the x^0 term, omit the "x^0"
        else if (i == 0)
        {
        }
        
        // Otherwise identify the degree of the x
        else
        {
          output += "x^" + (i);
        }
      }
    }
    
    // Return the resulting string
    return output;
  }
  
  public boolean equals(Polynomial other)
  {
    return (this.toString().equals(other.toString()));
  }
  
  
  public Polynomial add(Polynomial other)
  {
    return new Polynomial(null);
  }
  
  public Polynomial multiply(Polynomial other)
  {
    return new Polynomial(null);
  }
  
  public static void main(String[] args)
  {
    double [] a;
    Polynomial p0, p1, p2, p3, p4, p5, p6, p7, p8;
    
    // 7.0x^3 + 5.0x^2 + 3.0x + 1.0
    a = new double[4];
    a[3] = 7.0; a[2] = 5.0; a[1] = 3.0; a[0] = 1.0;
    p0 = new Polynomial(a);
    
    // 15.0x^2 + 10.0x + 5.0
    a = new double[3];
    a[2] = 15.0; a[1] = 10.0; a[0] = 5.0;
    p1 = new Polynomial(a);
    
    // x^2 + 2.0x + 3.0
    p2 = new Polynomial(1.0, 2.0, 3.0);
    
    // 15.0x^2 + 10.0x + 5.0 (again)
    p3 = new Polynomial(15.0, 10.0, 5.0);
    
    // 5.0x^2 + 3.0x + 1.0
    p4 = new Polynomial(5.0, 3.0, 1.0);
    
    // -4.0x^2 - 1.0
    p5 = new Polynomial(-4.0, 0.0, -1.0);
    
    // 0.0
    p6 = new Polynomial(null);
    
    // 0.0
    a = new double[1];
    a[0] = 0.0;
    p7 = new Polynomial(a);
    
    // -2.0
    a = new double[1];
    a[0] = -2.0;
    p8 = new Polynomial(a);
    
    a = new double[4];
    a[3] = 7.0; a[2] = 5.0; a[1] = 3.0; a[0] = 1.0;
    System.out.println(new Polynomial(a));
    
    a = new double[4];
    a[3] = 3.0; a[2] = 1.0; a[1] = 0.0; a[0] = 1.0;
    System.out.println(new Polynomial(a));
    
    System.out.println(new Polynomial(1.0,2.0,3.0));
    System.out.println(new Polynomial(1.0,0.0,-16.0));
    System.out.println(new Polynomial(0,2,3));
    System.out.println(new Polynomial(0,0,10.0));
    
    System.out.println("some space");
    
    
    System.out.println("Expected: 3  Actual: " + p0.degree());
    System.out.println("Expected: 2  Actual: " + p1.degree());
    
    System.out.println("Expected: 83.0  Actual: " + p0.eval(2));
    System.out.println("Expected: -41.0  Actual: " + p0.eval(-2));
    System.out.println("Expected: 16.0  Actual: " + p0.eval(1.0));
    
    System.out.println("Expected: 7.0x^3 + 5.0x^2 + 3.0x + 1.0");
    System.out.println("  Actual: " + p0);
    System.out.println("Expected: 15.0x^2 + 10.0x + 5.0");
    System.out.println("  Actual: " + p1);
    System.out.println("Expected: x^2 + 2.0x + 3.0");
    System.out.println("  Actual: " + p2);
    System.out.println("Expected: -4.0x^2 - 1.0");
    System.out.println("  Actual: " + p5);
    System.out.println("Expected: 0.0");
    System.out.println("  Actual: " + p6);
    System.out.println("Expected: 0.0");
    System.out.println("  Actual: " + p7);
    System.out.println("Expected: -2.0");
    System.out.println("  Actual: " + p8);
    
    System.out.println("Expected: false  Actual: " + p1.equals(p0));
    System.out.println("Expected: true  Actual: " + p1.equals(p1));
    System.out.println("Expected: false  Actual: " + p1.equals(p2));
    System.out.println("Expected: true  Actual: " + p1.equals(p3));
    System.out.println("Expected: false  Actual: " + p0.equals(p4));
    
    System.out.println("Expected: 7.0x^3 + 20.0x^2 + 13.0x + 6.0");  
    System.out.println("  Actual: " + p0.add(p1));
    System.out.println("Expected: 15.0x^4 + 40.0x^3 + 70.0x^2 + 40.0x + 15.0");  
    System.out.println("  Actual: " + p1.multiply(p2));
  }
  
}  

