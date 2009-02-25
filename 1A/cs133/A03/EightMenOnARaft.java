/**This class models the game 'Eight men on a raft'
 * @author Daniel Burstyn - 20206120
 */
public class EightMenOnARaft
{
  // Instance variables
  private Man[] men;
  private Board myBoard;
  private int startSquare;
  private int targetSquare;
  private boolean gameWin;
  private String gameCondition;
  
  /**Constructs an instance of the game
   */ 
  public EightMenOnARaft ()
  {
    // Initialize the board, game status, and array of men
    myBoard = new Board(9);
    gameWin = false;
    men = new Man[9];
    
    for (int i=0 ; i<4 ; i++)
    {
      men[i] = new Man(false);
      men[8-i] = new Man(true);
    }
    men[4] = null;
  }
  
  /** Starts the game
   */
  public void play()
  {
    // Draw the men onto the board
    for (int i=0 ; i<9 ; i++)
    {
      if (men[i] != null) 
      {
        myBoard.putPeg( men[i].isRed() ? myBoard.RED : myBoard.BLUE , i );
      }
    }
    
    // Repeat until the user has either won or run out of moves
    while (!gameWin)
    {
      
      // Repeat forever, until the user clicks on a peg (not the empty space)
      while (true)
      {
        startSquare = myBoard.getPosition();
        
        if (men[startSquare] == null)
        {
          myBoard.displayMessage("You must click on a peg");
        }
        else
        {
          break;
        }
      }
      
      // Colour the peg that has been selected
      myBoard.putPeg(myBoard.YELLOW, startSquare);
      
      // Get the target location from the user
      targetSquare = myBoard.getPosition();
      
      // If the user clicks on the same square, just return the peg to it's normal colour
      if (startSquare - targetSquare == 0 )
      {
        myBoard.putPeg( men[startSquare].isRed() ? myBoard.RED : myBoard.BLUE , startSquare );
      }
      
      // If the user wants to move a blue peg left, show an error message and return the peg to normal
      else if (startSquare > targetSquare && !men[startSquare].isRed())
      {
        myBoard.displayMessage("Blue pegs can only move right");
        myBoard.putPeg( men[startSquare].isRed() ? myBoard.RED : myBoard.BLUE , startSquare );
      }
      
      // If the user wants to move a red peg right, show an error message and return the peg to normal
      else if (startSquare < targetSquare && men[startSquare].isRed())
      {
        myBoard.displayMessage("Red pegs can only move left");
        myBoard.putPeg( men[startSquare].isRed() ? myBoard.RED : myBoard.BLUE , startSquare );
      }
      
      // If the user wants to move a peg onto another peg, show an error adn return the peg to normal
      else if (men[targetSquare] != null) 
      {
        myBoard.displayMessage("There is a peg in that spot already");
        myBoard.putPeg( men[startSquare].isRed() ? myBoard.RED : myBoard.BLUE , startSquare );
      }
      
      // If the user is moving a peg one square, then change the board accordingly,
      // since all of the illegal one square moves have been checked already
      else if (Math.abs(startSquare - targetSquare) == 1)
      {
        myBoard.removePeg(startSquare);
        myBoard.putPeg( men[startSquare].isRed() ? myBoard.RED : myBoard.BLUE , targetSquare );
        men[targetSquare] = men[startSquare];
        men[startSquare] = null;
        myBoard.displayMessage("");
      }
      
      // If the user is moving two squares...
      else if (Math.abs(startSquare - targetSquare) == 2)
      {
        // Check that the peg being jumped over is not the same colour
        // The peg being jumped cannot be the null square since the target square would then be a peg,
        // and that case has already been checked
        if (men[startSquare - ((startSquare - targetSquare)/2)].isRed() == men[startSquare].isRed()) 
        {
          myBoard.displayMessage("You cannot jump over a peg of the same colour");
          myBoard.putPeg( men[startSquare].isRed() ? myBoard.RED : myBoard.BLUE , startSquare );
        }
        else
        {
          myBoard.removePeg(startSquare);
          myBoard.putPeg( men[startSquare].isRed() ? myBoard.RED : myBoard.BLUE , targetSquare );
          men[targetSquare] = men[startSquare];
          men[startSquare] = null;
          myBoard.displayMessage("");
        }
      }
      
      // Otherwise, the user is trying to move more than two squares.
      // Display an error and return the peg to normal
      else
      {
        myBoard.displayMessage("You cannot move that far");
        myBoard.putPeg( men[startSquare].isRed() ? myBoard.RED : myBoard.BLUE , startSquare );
      }
      
      // Check if the user has won
      if (men[4] == null && men[0].isRed() && men[1].isRed() && men[2].isRed() && men[3].isRed() && !men[5].isRed() && !men[6].isRed() && !men[7].isRed() && !men[8].isRed())
      {
        gameWin = true;
      }
      
      // Check if there are any more legal moves
      // First create a string to represent the squares within two spaces of the null square
      gameCondition = "";
      for (int i = 0 ; i < 9 ; i++)
      {
        if ( men[i] == null )
        {
          for (int j = -2 ; j <= 2 ; j++)
          {
            if (j == 0) {
              gameCondition += "o";
            }
            else if (i+j < 0 || i+j > 8) {
              gameCondition += "W";
            }
            else {
              gameCondition += men[i+j].isRed() ? "R" : "B";
            }
          }
        }
      }
      
      // There are no more legal moves if there are no blue pegs on the left, and no red pegs on the left
      if (gameCondition.substring(0,2).indexOf('B') == -1 && gameCondition.substring(3,5).indexOf('R') == -1) 
      {
        break;
      }
      
    }
    
    // Now that the game is finished, display a message depending on whether the user won or lost
    if (gameWin)
    {
      myBoard.displayMessage("You win!");
    }
    else
    {
      myBoard.displayMessage("You have no more moves");
    }
  }
}
