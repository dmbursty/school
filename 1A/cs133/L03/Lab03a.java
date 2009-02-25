/**This class creates a board, and draws a pattern of
 * red and black pegs on it
 */
public class Lab03a 
{
  public static void main(String[] args)
  {
    // Create the board
    Board myBoard = new Board(8, 8);
    
    // Iterate through each square of the board
    for (int row = 0 ; row < 8 ; row++) 
    {
      for (int col = 0 ; col < 8 ; col++)
      {
        // Check if the square is a light square
        if ((row+col)%2 == 0) 
        {
          // Check which colour peg should be placed
          if (row <= 2)
          {
            myBoard.putPeg(myBoard.RED, row, col);
          }
          else if (row >= 5)
          {
            myBoard.putPeg(myBoard.BLACK, row, col);
          } 
        }
      }
    }
  }
}
