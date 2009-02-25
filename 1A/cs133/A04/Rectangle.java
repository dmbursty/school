import java.awt.*;

/** 
 * This class models a picture of a rectangle
 * @author Daniel Burstyn - 20206120
 */
public class Rectangle extends Picture 
{
   // Instance Variables
   Color color;
   int height;
   int width;

   /** 
    * Construct the rectangle
    * @param color The colour of the rectangle
    * @param height The height of the rectangle
    * @param width The width of the rectangle
    */
   public Rectangle(Color color, int height, int width) 
   {
     this.height = height;
     this.width = width;
     this.color = color;
   }

   /** 
   * Draw the rectangle
   * @param board The board the rectangle is to be drawn on
   * @param row The row where the rectangle should be drawn from
   * @param col The column where the rectangle should be drawn from
   */
   public void drawMe(Board board, int row, int col) 
   {
     // Iterate through every row and every column in the rectangle
     for (int y = 0 ; y < height ; y++ )
     {
       for (int x = 0 ; x < width ; x++ )
       {
         // If the peg is within the board, draw it
         if (y + row >= 0 && x + col >= 0 && y + row < board.getRows() && x + col < board.getColumns())
         {
           board.putPeg(color, y + row, x + col);
         }
       }
     }
   }
   
   /**
    * Get the height of this rectangle
    * @return this rectangle's height
    */
   public int getHeight()
   {
     return height;
   }
   
   /**
    * Get the width of this rectangle
    * @return this rectangle's width
    */
   public int getWidth()
   {
     return width;
   }

   public static void main(String[] args) 
   {
     Picture test = new Rectangle(Board.BLUE, 5, 6);
     Board board = new Board(9, 12);
     test.drawMe(board, 2, 3);
   }
}
