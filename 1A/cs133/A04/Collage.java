/** 
 * This class models a collage of separate pictures
 * @author Daniel Burstyn - 20206120
 */
public class Collage extends Picture 
{
  // Instance Variables
  Picture[] pics = new Picture[1];
  Coordinate[] locations = new Coordinate[1];
  int numPics = 0;
  int height = 0;
  int width = 0;
  
  /** 
   * Constructs the collage
   */
  public Collage() 
  {
  } // end constructor
  
  /** 
   * Add a picture to the collage. 
   * @param picture the picture to add. pre: picture != null
   * @row, column the top-left corner for this picture. 
   *   pre: row, column >= 0 
   */
  public void add(Picture picture, int row, int column) 
  {
    // increases array size if full of pictures
    if (numPics == pics.length)
    {
      Picture[] tempPics = new Picture[pics.length*2];
      for (int i = 0; i < pics.length; i++)
      {
        tempPics[i] = pics[i];
      }
      pics = tempPics;
      
      Coordinate[] tempCoords = new Coordinate[locations.length*2];
      for (int i = 0; i < locations.length; i++)
      {
        tempCoords[i] = locations[i];
      }
      locations = tempCoords;
    }
    
    // add new picture to the collage
    pics[numPics] = picture;
    Coordinate c = new Coordinate(row, column);
    locations[numPics] = c;
    
    // Calculates the new height and width of the collage if it changes
    height = Math.max(height,pics[numPics].getHeight() + locations[numPics].getRow());
    width = Math.max(width,pics[numPics].getWidth() + locations[numPics].getCol());
    
    numPics++;
  } // end add
  
  /**
   * Draws the collage
   * @param board The board the collage is to be drawn on
   * @param row The row where the collage should be drawn from
   * @param col The column where the collage should be drawn from
   */
  public void drawMe(Board board, int row, int column)
  {
    for (int i = 0; i < numPics; i++)
    {
      int picRow = locations[i].getRow();
      int picCol = locations[i].getCol();
      pics[i].drawMe(board, row + picRow, column + picCol);
    }
  }
  
  /**
   * Get the height of this collage
   * @return the collage's height
   */
  public int getHeight()
  {
    return height;
  }
  
  /**
   * Get the width of this collage
   * @return the collage's width
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

