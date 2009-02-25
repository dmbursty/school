import java.awt.*;

/** 
 * This class models a picture of a circle
 * @author Daniel Burstyn - 20206120
 */
public class Circle extends Picture 
{
  // Instance variables
  Color color;
  int diameter;
  
  /** 
   * Construct the circle
   * @param color The colour of the circle
   * @param diameter The diameter of the circle
   */
  public Circle(Color color, int diameter) 
  {
    this.color = color;
    this.diameter = diameter;
  } // end constructor
  
  /**
   * Draw the circle
   * @param board The board the circle is to be drawn on
   * @param row The row where the circle should be drawn from
   * @param col The column where the circle should be drawn from
   */
  public void drawMe(Board board, int row, int col)
  {
    int radius = (diameter + (diameter % 2 == 1 ? 1 : 0)) / 2;
    
    // Iterate through every row and column in the box containing the circle
    // We go from -radius to +radius and use the the equation of a circle (x^2 + y^2 <= r^2)
    // to draw a circle around what would be 0,0.  Then when we draw the pegs, we shift each
    // peg to compensate and move 0,0 to the top left corner.
    for (int x = -radius ; x < radius ; x++)
    {
      for (int y = -radius ; y < radius ; y++)
      {
        // Shift x and y if the circle has an even diameter
        double X = x + (diameter % 2 == 1 ? 0 : 0.5);
        double Y = y + (diameter % 2 == 1 ? 0 : 0.5);
        
        // If the peg satisfies the equation for the circle, and is on the board, then draw it
        if (X*X + Y*Y <= diameter*diameter*0.25 && x + col + radius - (diameter % 2 == 1 ? 1 : 0) >= 0 && y + row + radius - (diameter % 2 == 1 ? 1 : 0) >= 0 && x + col + radius - (diameter % 2 == 1 ? 1 : 0) < board.getColumns() && y + row + radius - (diameter % 2 == 1 ? 1 : 0) < board.getRows()) {
          board.putPeg(color, y + row + radius - (diameter % 2 == 1 ? 1 : 0) , x + col + radius - (diameter % 2 == 1 ? 1 : 0));
        }
      }
    }
  }
  
  /** 
   * Get the height of this circle (it's diameter)
   * @return this picture's height (diameter)
   */
  public int getHeight() {
    return diameter;
  }
  
  /**
   * Get the width of this circle (it's diameter)
   * @return this picture's width (diameter)
   */
  public int getWidth() {
    return diameter;
  }
  
  public static void main(String[] args)
  {    
    /*Picture orangeOne = new Circle(Board.ORANGE, 1);
    Picture yellowOne = new Circle(Board.YELLOW, 4);
    Picture whiteOne = new Circle(Board.WHITE, 7);
    Picture greenOne = new Circle(Board.GREEN, 2);
    */
    Picture redOne = new Circle(Board.RED, 10);
    
    Board b = new Board(12, 12);
    /*orangeOne.drawMe(b, 4, 1);
    yellowOne.drawMe(b, 7, 1);
    whiteOne.drawMe(b, 1, 4);
    greenOne.drawMe(b, 9, 7);
    */
    redOne.drawMe(b, 0,0);
    
  } // end main
  
} // end class

