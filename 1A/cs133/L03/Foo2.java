import java.util.Scanner;
/** 
 * My assignment.
 * @author Paul Nijjar
 * @date today
 */
public class Foo2 
{ 

  public static void main(String[] args) 
  { 
    Scanner temp = new Scanner(System.in); // Initialize a Scanner
    String temp1 = temp.next(); // Get a string 
    boolean temp2 = true; // a boolean variable
    boolean temp3 = true; // another boolean 
    // For loop 
    for (int tmp1 = 0; tmp1 < temp1.length(); tmp1++)
    { 
      char temp4 = temp1.charAt(tmp1);
      // compare temp4 > 'a' and temp4 < 'z'
      if (temp4 > 'a' && temp4 < 'z') 
      {  
        // temp2 = true;  // Set temp2 to true 
        temp3 = false;    // set temp3 to false
      } else if (temp4 >=0 && temp4 <=9)  
      { 
        temp2 = false;   // set temp2 to true 
        // temp3 = true; // set temp3 to true
      } else 
      { 
        temp2 = false;   // set temp2 to false
        temp3 = false;   // set temp2 to false
      } // close brace
    } // close brace
    // if statement 
    if (!(temp2==false)) 

    { 
      // All letters
      




      System.out.println("All letters");
    } else if (temp3) 
    {
      // All digits
      System.out.println("All digits");
    } else 



    { 
        
      // Mixed
      
        
        
      System.out.println("Mixed");

    } // close brace
  } // close brace
  
} // close brace 
