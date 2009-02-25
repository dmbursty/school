/** 
 * This class models a collage of separate pictures
 * @author Daniel Burstyn - 20206120
 */
public class Collage extends Picture 
{
  // Instance Variables
  Picture[] pictures;
  int[][] offsets;
  int numPics;
  int height;
  int width;
  
  /** 
   * Constructs the collage
   */
  public Collage() 
  {
    pictures = new Picture[0];
    offsets = new int[0][2];
    numPics = 0;
    height = 0;
    width = 0;
  } // end constructor
  
  /** 
   * Add a picture to the collage. 
   * @param picture the picture to add. pre: picture != null
   * @row, column the top-left corner for this picture. 
   *   pre: row, column >= 0 
   */
  public void add(Picture picture, int row, int column) 
  {
    // Increase the size of the arrays to make room for the new picture
    // First create a new array with one more space
    Picture[] newPictures = new Picture[numPics + 1];
    int[][] newOffsets = new int[numPics + 1][2];
    
    // Read the array of pictures and offsets into the new arrays
    for (int i = 0 ; i < numPics; i++)
    {
      newPictures[i] = pictures[i];
      newOffsets[i] = offsets[i];
    }
    
    // Set the arrays to their respective new arrays, and add the new picture
    pictures = newPictures;
    offsets = newOffsets;
    pictures[numPics] = picture;
    offsets[numPics][0] = row;
    offsets[numPics][1] = column;
    
    // Calculate the new height and width of the collage if it changes
    height = Math.max(height,pictures[numPics].getHeight() + offsets[numPics][0]);
    width = Math.max(width,pictures[numPics].getWidth() + offsets[numPics][1]);
    
    // Update the number of pictures variable
    numPics++;

  } // end add
  
  /** 
   * Draw the collage
   * @param board The board the collage is to be drawn on
   * @param row The row where the collage should be drawn from
   * @param col The column where the collage should be drawn from
   */
  public void drawMe(Board board, int row, int col)
  {
    // Iterate through all the pictures in the collage and raw them
    for (int i = 0 ; i < numPics ; i++)
    {
      pictures[i].drawMe(board, offsets[i][0] + row, offsets[i][1] + col);
    }
  }
  
  /**
   * Get the height of this collage
   * @return this collage's height
   */
  public int getHeight()
  {
    return height;
  }
  
  /**
   * Get the width of this collage
   * @return this collage's width
   */
  public int getWidth()
  {
    return width;
  }
  
  public static void main(String[] args) 
  {
    Collage test = new Collage();
    Picture a = new Square(Board.GREEN, 2);
    Picture b = new Square(Board.YELLOW, 3);
    Picture c = new Peg(Board.RED);
    test.add(a, 0, 0);
    test.add(b, 4, 2);
    test.add(c, 2, 4);
    
    Board board = new Board(10, 10);
    test.drawMe(board, 2, 2);
  } // end main
} // end class

