import java.awt.*;

/** 
 * This class models a picture of a square
 * @author Daniel Burstyn - 20206120
 */
public class Square extends Rectangle 
{
  /** 
   * Construct the square
   * @param color The colour of the square
   * @param size The length of the sides of the square
   */
  public Square(Color color, int size) 
  {
    super(color,size,size);
  }
  // end constructor
} // end class
