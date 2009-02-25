import java.util.Scanner;

/** 
 * Scores a token according to the Scrabble (C) letter rules.
 * @author CS 133 Team!
 * @date 2005-10-11
 * @version 0.2
 *   - 0.2: Corrected documentation error: missing "v" in 4 point
 *   words
 *   - 0.1: initial revision
 */
public class ScrabbleScorer 
{ 
  public static void main (String[] args) 
  {
    // How much each letter is worth
    // 1 point: a, e, i, l, n, o, r, s, t, u
    String point1 = "aeilnorstu";
    // 2 points: d, g
    String point2 = "dg";
    // 3 points: b, c, m, p 
    String point3 = "bcmp";
    // 4 points: f, h, v, w, y
    String point4 = "fhvwy;";
    // 5 points: k
    String point5 = "k";
    // 8 points: j, x
    String point8 = "jx";
    // 10 points: q, z
    String point10 = "qz";
    
    // Get in a token 
    Scanner in = new Scanner(System.in);
    System.out.println("Input a word to be scored.");
    // Ignore multiple tokens. 
    String target = in.next();
    
    // Loop through the string, scoring each character
    int score = 0;
    int pos = 0; 
    
    while (pos < target.length())
    { 
      char currLetter = target.charAt(pos);
      if (point1.indexOf(currLetter) >= 0) 
      { 
        score += 1;
      } 
      else if (point2.indexOf(currLetter) >= 0) 
      { 
        score += 2;
      } 
      else if (point3.indexOf(currLetter) >= 0) 
      { 
        score += 3;
      } 
      else if (point4.indexOf(currLetter) >= 0)
      {  
        score += 4;
      } 
      else if (point5.indexOf(currLetter) >= 0) 
      { 
        score += 5;
      } 
      else if (point8.indexOf(currLetter) >= 0) 
      {   
        score += 8;
      }
      else if (point10.indexOf(currLetter) >= 0)
      { 
        score += 10;
      } // end if 
      
      pos++;
    } // end while 
    
    System.out.println("The score of \"" + target 
                + "\" is " + score);
    
  } // end main 
} // end class
