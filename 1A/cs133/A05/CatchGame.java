import java.util.*;

/**This class manages the interactions between the different pieces of 
  * the game: the Board, the Daleks, and the Doctor. It determines when
  * the game is over and whether the Doctor won or lost.
  * @author Daniel Burstyn - 20206120
  */
public class CatchGame implements DalekGame
{
  // Instance Variables
  private int level;
  private int safeTeleports;
  private boolean gameOver;
  private boolean levelContinues;
  private DalekBoard room;  
  private Dalek[] daleks;
  private Doctor daniel;
  
  /**The constructor initializes the variables for the game.
    */
  public CatchGame()
  {
    level = 1;
    safeTeleports = 5;
    gameOver = false;
    levelContinues = false;
    room = new DalekBoard(20,20);
    room.addBoardListener(this);
  }
  
  /**Restarts the game from level 1
    */
  public void restartGame()
  {
    emptyBoard();
    level = 1;
    safeTeleports = 5;
    newLevel(1);
  }
  
  /**Resets the board, and variables for the new level
   * @param newLevel The level that should be initialized
   */
  private void newLevel(int newLevel) {
    
    // Create a new array of daleks based on the level number
    daleks = new Dalek[newLevel*2 + 1];
    
    // Place the appropriate number of daleks
    for (int i = 0; i < daleks.length ; i++)
    {
      daleks[i] = new Dalek((int)(Math.random()*20),(int)(Math.random()*20));
      // Check all previously made daleks and check for overlaps
      for (int j = 0; j < i; j++)
      {
        // If there is an overlap stop checking, and try generating the same dalek again
        if (daleks[j].getRow() == daleks[i].getRow() && daleks[j].getCol() == daleks[i].getRow())
        {
          i--;
          break;
        }
      }
    }
    
    // Repeat until the doctor is in a safe location
    for (int safeDoc = 0; safeDoc < 1; safeDoc++)
    {
      daniel = new Doctor((int)(Math.random()*20),(int)(Math.random()*20));
      for (int i = 0; i < daleks.length; i++)
      {
        // If the doctor isn't in a safe position, decrement the for loop index to try placing the doctor again
        if (daniel.getRow() == daleks[i].getRow() && daniel.getCol() == daleks[i].getCol())
        {
          safeDoc--;
          break;
        }
      }
    }
    
    // Draw the doctor and each of the daleks on the board
    room.putPeg(room.GREEN,daniel.getRow(),daniel.getCol());
    for (int i = 0; i < daleks.length ; i++)
    {
      room.putPeg(Board.BLACK,daleks[i].getRow(),daleks[i].getCol());
    } 
    room.displayMessage("Safe Teleports Left: "+safeTeleports);
    
  }
  
  /**Clears the board of all pegs
    */
  private void emptyBoard()
  {
    for (int i = 0; i < room.getRows(); i++) 
    {
      for (int j = 0; j < room.getColumns(); j++)
      {
        room.removePeg(i,j);
      }
    }
  }
  
  /**The playGame method starts the game.  It covers everything
    * up until the user's first click on the board, so that the
    * pegs are initially displayed.  The rest of the game is handled by squareClicked
    */
  public void playGame()
  {
    newLevel(1);
  }
  
  /**This method covers everything in the turn after the doctor has moved
    * this includes moving daleks, checking for end of level/game, and drawing the board
    */
  public void restOfTurn()
  {
    // Move the daleks
    for (int i = 0; i < daleks.length ; i++)
    {
      daleks[i].advanceTowards(daniel);
    }
    
    // Check if any daleks have crashed
    // Iterate through every dalek
    for (int i = 0; i < daleks.length ; i++)
    {
      // Check all successive daleks in the array
      for (int j = i + 1; j < daleks.length ; j++)
      {
        // If they are in the same location, then they have crashed
        if (daleks[i].getRow() == daleks[j].getRow() && daleks[i].getCol() == daleks[j].getCol())
        {
          daleks[i].crash();
          daleks[j].crash();
        }
      }
    }
    
    // Move speedy daleks again
    for (int i = 0; i < (int)(level/2 - 1); i++)
    {
      daleks[i].advanceTowards(daniel);
    }
    
    // Check if any speedy daleks have crashed
    // Iterate through every dalek
    for (int i = 0; i < (int)(level/2 - 1) ; i++)
    {
      // Check all successive daleks in the array
      for (int j = i + 1; j < daleks.length ; j++)
      {
        // If they are in the same location, then they have crashed
        if (daleks[i].getRow() == daleks[j].getRow() && daleks[i].getCol() == daleks[j].getCol())
        {
          daleks[i].crash();
          daleks[j].crash();
        }
      }
    }
    
    // Draw the doctor and the daleks
    room.putPeg(room.GREEN,daniel.getRow(),daniel.getCol());
    for (int i = 0; i < daleks.length ; i++)
    {
      room.putPeg(daleks[i].hasCrashed()?Board.RED:Board.BLACK,daleks[i].getRow(),daleks[i].getCol());
    }
    room.displayMessage("Safe Teleports Left: "+safeTeleports);
    
    // Check if the doctor is on the same square as a dalek (crashed or not)
    for (int i = 0; i < daleks.length ; i++)
    {
      // If the doctor is on a dalek, whether it has crashed or not, then display a message and end the game
      if (daniel.getRow() == daleks[i].getRow() && daniel.getCol() == daleks[i].getCol())
      {
        room.putPeg(Board.YELLOW,daniel.getRow(),daniel.getCol());
        room.displayMessage("You've been captured!");
        gameOver = true;
      }
    }
    
    // If the game has ended, then remove the Board Listener
    if (gameOver)
    {
      room.removeBoardListener();
    }
    
    // Check if all the daleks have crashed
    levelContinues = false;
    for (int i = 0; i < daleks.length ; i++)
    {
      // If any dalek has not crashed, then the level will continue
      if (!daleks[i].hasCrashed())
      {
        levelContinues = true;
      }
    }
    // If all of the daleks have crashed, empty the board, and start the new level
    if (!levelContinues) {    
      emptyBoard();
      newLevel(level==12 ? level : ++level);
    }
  }
  
  /**The squareClicked method handles all the events of the game.
    * Whenever the board is clicked, the squareClicked method is called
    * and the appropriate effects are applied
    * @param coord The coordinates of the square that was clicked
    */
  public void squareClicked(Coordinate coord)
  {
    // Remove the doctor and daleks so they don't appear in succesive turns
    emptyBoard();
    
    // Move the doctor, and then the daleks appropriately
    daniel.move(coord.getRow(),coord.getCol());
    restOfTurn();
  }
  
  /**This method is called from DalekGame and is used to teleport the doctor
    * @param isSafe Whether the doctor is Safe teleporting, or Unsafe teleporting
    */
  public void teleport(boolean isSafe)
  {
    // Empty the board of pegs
    emptyBoard();
    
    // If the doctor should teleport safely
    if (isSafe)
    {
      // Keep repeating until the doctor is in a safe location
      for (int safeDoc = 0; safeDoc < 1; safeDoc++)
      {
        // move(-2,-2) will always result in the doctor teleporting
        daniel.move(-2,-2);
        for (int i = 0; i < daleks.length; i++)
        {
          // If the doctor isn't in a safe position, decrement the for loop index to try placing the doctor again
          if (Math.abs(daniel.getRow() - daleks[i].getRow()) < 2 && Math.abs(daniel.getCol() - daleks[i].getCol()) < 2)
          {
            safeDoc--;
            break;
          }
        }
      }
      // Decrement number of safe teleports, and disable the menu item if
      // there are no more safe teleports
      safeTeleports--;
      if (safeTeleports == 0)
      {
        room.safeTeleport.setEnabled(false);
      }
    }
    // Otherwise, simply teleport the doctor
    else
    {
      daniel.move(-2,-2);
    }
    // Finish the rest of the events for the turn
    restOfTurn();
  }
  
  /**This method is called from DalekBoard and is used when
    * the user declares that they will make no more moves for the turn
    */
  public void keepWaiting()
  {
    // Remove the board listener so the user can't make moves
    room.removeBoardListener();
    
    // For every uncrashed dalek, add one more safe teleport
    // When the user declares wait, then either all uncrashed daleks
    // will crash, or the user will lose, therefore this method
    // of adding safe teleports is sufficient
    for (int i = 0; i < daleks.length; i++)
    {
      if (!daleks[i].hasCrashed())
      {
        safeTeleports+=1;
      }
    }
    // Limit the number of safe teleports to 5
    if (safeTeleports > 5)
    {
      safeTeleports = 5;
    }
    // Reenable the Safe Teleport menu item if it has been disabled
    room.safeTeleport.setEnabled(true);
    
    // Repeat until either the end of game or end of level
    while (true)
    { 
      // Have the turn execute without moving the doctor
      emptyBoard();
      restOfTurn();
      
      // If the game or level is over, then leave the loop
      if (gameOver || !levelContinues)
      {
        break;
      }
    }
    // Replace the board listener
    room.addBoardListener(this);
  }
}