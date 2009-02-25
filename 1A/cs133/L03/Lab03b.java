import java.util.*;
/**This class gets an integer input from the user and
 * outputs that number's factorial.  If the number is too
 * large, an error message will be printed instead
 * @author Daniel Burstyn - 20206120
 */
public class Lab03b 
{
  public static void main(String[] args)
  {
    System.out.println("Input an integer");
    
    // Read an integer from the user
    int n = new Scanner(System.in).nextInt();
    
    long factorial = 1;
    
    // Check if the integer is small enough
    if (n <= 20)
    {
      
      // Calculate and output the factorial result
      for (int i = n ; i > 0 ; i--)
      {
        factorial *= i;
      }
      System.out.println(n + "! = " + factorial);
    }
    else
    {
      // Output an error message
      System.out.println("That integer is too large, try a smaller number");
    }
  }
}
