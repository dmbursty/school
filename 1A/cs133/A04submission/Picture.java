/** 
 * Represent some basic information about a picture. 
 * @author Daniel Burstyn - 20206120
 */
public class Picture 
{
  // Instance variables
  private int height, width;
   /** 
    * Make a picture with zero height and width
    */
   public Picture() 
   { this.height = 0;
     this.width = 0;
   } // end picture 
   
   /** 
    * Make a picture with a given height and width.
    * @param newHeight  the number of rows used by this picture 
    * @param newWidth  the number of columns used by this picture
    * <p>
    * pre: height, width >= 0
    */
   public Picture (int newHeight, int newWidth)
   { 
     this.height = newHeight;
     this.width = newWidth;
   } // end constructor

   /** 
    * Get the height of this picture.
    * @return this picture's height.
    */
   public int getHeight() 
   {
     return this.height;
   } // end getHeight

   /** 
    * Get the width of this picture.
    * @return this picture's width.
    */
   public int getWidth() 
   {
     return this.width;
   } // end getWidth

   /** 
    * Represent this picture on a Board
    * @param board the board on which to draw the picture. 
    *         pre: board != null
    * @param row, column  the top left hand corner 
    *   on which to draw this picture. 
    */
   public void drawMe(Board board, int row, int column) 
   {  // NO IMPLEMENTATION GOES IN THIS CLASS
   } // end drawMe
   
} // end Picture
