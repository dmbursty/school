import java.util.*;

/**This class manages the interactions between the different pieces of 
  * the game: the Board, the Daleks, and the Doctor. It determines when
  * the game is over and whether the Doctor won or lost.
  * @author Daniel Burstyn - 20206120
  */
public class CatchGame
{
  // Instance Variables
  private int level;
  private boolean gameOver;
  private boolean levelContinues;
  private Board room;  
  private Dalek[] daleks;
  private Doctor daniel;
  private Coordinate lastClick;
  
  /**The constructor initializes the variables for the game.
    */
  public CatchGame()
  {
    level = 11;
    gameOver = false;
    levelContinues = false;
    room = new Board(20,20);
  }
  
  /**The playGame method controls the game: deals with when the user
    * selects a square, when the Daleks move, when the game is 
    * won/lost.
    */
  public void playGame()
  {
    // Initiate the daleks and doctor for the new level
    while(true)
    {
      level++;
      if (level == 13)
      {
        level--;
      }
      
      daleks = new Dalek[level*2 + 1];
      for (int i = 0; i < daleks.length ; i++)
      {
        daleks[i] = new Dalek((int)(Math.random()*20),(int)(Math.random()*20));
      }
      daniel = new Doctor((int)(Math.random()*20),(int)(Math.random()*20));
      
      // Clear the board for the new level
      for (int i = 0 ; i < room.getRows() ; i++ )
      {
        for (int j = 0 ; j < room.getColumns() ; j++ )
        {
          room.removePeg(i,j);
        }
      }
      
      // Start the level
      // Repeat until one of the end of game conditions have been met
      while(true)
      {
        
        // Check if any of the daleks have crashed with each other
        for (int i = 0; i < daleks.length ; i++)
        {
          for (int j = i + 1; j < daleks.length ; j++)
          {
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
        
        // Check if the doctor is on the same square as a dalek (crashed or not)
        for (int i = 0; i < daleks.length ; i++)
        {
          if (daniel.getRow() == daleks[i].getRow() && daniel.getCol() == daleks[i].getCol())
          {
            room.putPeg(Board.YELLOW,daniel.getRow(),daniel.getCol());
            room.displayMessage("You've been captured!");
            gameOver = true;
          }
        }
        
        if (gameOver)
        {
          break;
        }
        
        // Check if all the daleks have crashed
        levelContinues = false;
        for (int i = 0; i < daleks.length ; i++)
        {
          if (!daleks[i].hasCrashed())
          {
            levelContinues = true;
          }
        }
        if (!levelContinues) {
          //room.displayMessage("Congratulations! You've survived!");
          break;
        }
        
        // Get the user's move
        lastClick = room.getClick();
        
        // Clear the board so that the doctor and daleks don't leave a trail of their moves
        room.removePeg(daniel.getRow(),daniel.getCol());
        for (int i = 0; i < daleks.length ; i++)
        {
          room.removePeg(daleks[i].getRow(),daleks[i].getCol());
        }
        
        // Move the doctor, and then the daleks appropriately
        daniel.move(lastClick.getRow(),lastClick.getCol());
        for (int i = 0; i < daleks.length ; i++)
        {
          daleks[i].advanceTowards(daniel);
        }
        
      }
      
      if (gameOver)
      {
        break;
      }
      
    }   
  }
}