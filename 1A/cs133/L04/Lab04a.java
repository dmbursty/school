import java.util.*;
/**This class gets a string input from the user, and outputs
 * the largest integer embedded in the string
 * @author Daniel Burstyn - 20206120
 */
public class Lab04a 
{
  public static void main(String[] args)
  {
    // Instance Variables
    int maximum = 0;
    int currentInt;
    String userInput = new Scanner(System.in).nextLine(); // Read in the first line from the user
    String integerInput = " ";
    Scanner myScanner;
    
    // Itterate through every character of the input string
    for (int i = 0 ; i < userInput.length() ; i++)
    {
      // If the character is a digit, move it to a new string (integerInput)
      if (userInput.charAt(i) >= '0' && userInput.charAt(i) <= '9')
      {
        integerInput += userInput.substring(i, i+1);
      }
      else 
      {
        // Otherwise, if the last character in the new string is not already a white space,
        // then make it a white space.  Basically, add a delimiter after each string of digit characters
        if ( !integerInput.substring(integerInput.length()-1, integerInput.length()).equals(" ") )
        {
          integerInput += " ";
        }
      }
    }
    
    // Read the new string into a new scanner
    myScanner = new Scanner(integerInput);
    
    // Read through all of the integers in the scanner
    while (myScanner.hasNextInt())
    {
      currentInt = myScanner.nextInt();
      
      // If the current integer is larger than the current maximum, then change the maximum
      if (maximum < currentInt)
      {
        maximum = currentInt;
      }
    }
    
    // Output the final maximum integer
    System.out.println(maximum);
  }
}
