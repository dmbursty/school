/**This class takes input from the keyboard and outputs the text as spoken by the Swedish Chef from the Muppets.
  * @author Daniel Burstyn - 20206120
  */
import java.util.*;
public class SwedishChef
{
  public static void main(String[] args)
  {
    // Get keyboard input from user
    Scanner myScanner = new Scanner(System.in);
    
    // Take the input from the user and put it into a new scanner
    // This is done because the hasNext() function works improperly if the Scanner has System.in as input
    Scanner line = new Scanner(myScanner.nextLine());
    
    // Test Case
    // line = new Scanner("Them ether bath Ow tow vat Vat wear Wear An an nation mere initial out Out top comma, period. second sentance. third sentance. Fouth line END end of input");
    
    // Declare variables
    int sentanceCounter = 0;
    String word = "";
    String append = "";
    
    // Iterate through every word in the input
    for (;;) 
    {
      // Print a line break and exit loop if at the end of input
      if (!line.hasNext())
      {
        System.out.print("\n");
        break;
      }
      
      // Get the next word in the input
      word = line.next();
      
      // Print a line break and exit loop if the word is "END"
      if (word.equals("END"))
      {
        System.out.print("\n");
        break;
      }
      
      // Remove punctuation, and store in the variable "append"
      if (word.charAt(word.length()-1) == ',' || word.charAt(word.length()-1) == '.')
      {
        append = word.substring(word.length()-1,word.length());
        word = word.substring(0,word.length()-1);
      }
      
      // Apply rules
      word = word.replace("the","zee");
      word = word.replace("The","Zee");
      if (word.endsWith("th"))
      {
        word = word.substring(0,word.length()-1);
      }
      word = word.replace("ow","uh");
      word = word.replace("Ow","Uh");
      word = word.replace("v","f");
      word = word.replace("V","F");
      word = word.replace("w","v");
      word = word.replace("W","V");
      word = word.replace("an","un");
      word = word.replace("An","Un");
      word = word.replace("tion","shun");
      if (word.endsWith("e"))
      {
        word += "-a";
      }
      if (word.indexOf("i",1) >= 0)
      {
        word = word.substring(0,word.indexOf("i",1)) + "ee" + word.substring(word.indexOf("i",1)+1,word.length());
      }
      if (word.indexOf("o",1) >= 0)
      {
        word = word.charAt(0) + word.substring(1,word.length()).replace("o","u");
      }
      if (word.startsWith("o"))
      {
        word = "o" + word;
      }
      if (word.startsWith("O"))
      {
        word = "Oo" + word.substring(1,word.length());
      }
      
      //Replace punctuation, and a line break in the case of a period
      if (append.equals(".")) {
        append = ".\n";
        sentanceCounter++;
      }
      else {
        append = append + " ";
      }
      
      // Output the word reset the punctuation holder
      System.out.print(word + append);
      append = "";
      
      //Check for third sentance
      if (sentanceCounter == 3) {
        System.out.println("Bork bork bork!");
        sentanceCounter = 0;
      }
    }
  }
}
