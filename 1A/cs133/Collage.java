/** 
 * Put description here
 */
public class Collage extends Picture 
{
  Picture[] pics = new Picture[1];
  Coordinate[] locations = new Coordinate[1];
  int numPics = 0;
  /** 
   * Put description here
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
    numPics++;
  } // end add
  
  public void drawMe(Board board, int row, int column)
  {
    for (int i = 0; i < numPics; i++)
    {
      int picRow = locations[i].getRow();
      int picCol = locations[i].getCol();
      pics[i].drawMe(board, row + picRow, column + picCol);
    }
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

