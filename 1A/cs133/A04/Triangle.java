import java.awt.Color;

/**
 * This class models a picture of a triangle
 * @author Daniel Burstyn - 20206120
 */
public class Triangle extends Picture 
{
  // Instance variable
  Color color;
  int height;
  int width;
  
  /**
   * Construct the triangle
   * @param color The colour of the triangle
   * @param height The height of the triangle
   * @param width The width of the triangle
   */
  public Triangle(Color color, int height, int width) 
  {
    this.color = color;
    this.height = height;
    this.width = width;
  }
  
  /**
   * Draw the triangle
   * @param board The board the triangle is to be drawn on
   * @param row The row where the triangle should be drawn from
   * @param col The column where the triangle should be drawn from
   */
  public void drawMe(Board board, int row, int col)
  {
    // Iterate through every row and column in the box containing the triangle
    for (int x = 0 ; x < width ; x++ )
    {
      for (int y = 0 ; y < height ; y++ )
      {
        // For the left half of the triangle
        if (x <= width / 2)
        {
          // If the point is within the triangle defined by y <= mx where
          // m is the slope of the edge (m = 2*height / width)
          // and if it is on the board, then draw it
          if (height - 1 - y <= (x * (2d*(double)height / (double)width)) && x + col >= 0 && y + row >= 0 && x + col < board.getColumns() && y + row < board.getRows() )
          {
            board.putPeg(color, y + row, x + col);
          }
        }
        // For the right half of the triangle
        else
        {
          // If the point is within the triangle defined by y <= (-m)x where
          // -m is the slope of the edge (m = 2*height / width)
          // and if it is on the board, then draw it
          // Since 0,0 is in the top left, we (width - x - 1) for -x
          if (height - 1 - y <= ((width - x - 1) * (2d*(double)height / (double)width)) && x + col >= 0 && y + row >= 0 && x + col < board.getColumns() && y + row < board.getRows() )
          {
            board.putPeg(color, y + row, x + col);
          }
        }
      }
    }
  }
  
  /**
   * Get the height of this triangle
   * @return the triangle's height
   */
  public int getHeight()
  {
    return height;
  }
  
  /**
   * Get the width of this triangle
   * @return the triangle's width
   */
  public int getWidth()
  {
    return width;
  }
  
  public static void main (String [] args) 
  {
    Board b = new Board(12, 12);
    Picture test1 = new Triangle(Board.ORANGE, 6, 7);
    test1.drawMe(b, 3, 2);
    System.out.println(test1.getHeight());
  }
}

