/**This class draws a 10x10 board, and tells the user which
 * square they have clicked
 * @author Daniel Burstyn - 20206120
 */
public class BoardFun
{
  public static void main(String[] args)
  {
    // Instance Variables
    Board myBoard = new Board(10,10);
    Coordinate lastClick;
    
    // Repeat while the user doesn't click on 0,0
    do 
    {
      // Get click location and display appropriate message
      lastClick = myBoard.getClick();
      myBoard.displayMessage("You clicked " + lastClick.getRow() + ", " + lastClick.getCol() + ".");
    }
    while ( !( lastClick.getRow() == 0 && lastClick.getCol() == 0 ) );
    
    // Display an exit message
    myBoard.displayMessage("You clicked 0, 0.  Goodbye.");
  }
}
