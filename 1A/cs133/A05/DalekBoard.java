import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**This class models a board for a Dalek Game
  * @author - Daniel Burstyn 20206120
  */
public class DalekBoard extends Board implements ActionListener
{
  //Instance Variables
  public DalekGame game;
  public JMenuItem safeTeleport;
  
  public DalekBoard(int rows, int columns)
  {
    // Call Boards constructor to initialize the variables and create the board
    super (rows, columns);
    
    // Create GUI Drop-down menu
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem;
    
    menuBar = new JMenuBar();
    
    menu = new JMenu("File");
    menuBar.add(menu);
    
    menuItem = new JMenuItem("Restart");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menuItem = new JMenuItem("Quit");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menu = new JMenu("Teleport");
    menuBar.add(menu);
    
    safeTeleport = new JMenuItem("Safe teleport");
    safeTeleport.addActionListener(this);
    menu.add(safeTeleport);
    
    menuItem = new JMenuItem("Unsafe teleport");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    menu = new JMenu("Wait");
    menuBar.add(menu);
    
    menuItem = new JMenuItem("Wait");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    
    this.setJMenuBar(menuBar);
    this.setVisible(true);  
  }
  
  /**This method is called when the user clicks on the board
    * this method is needed to override Board's because there
    * is a constant offset of the GUI Menu Bar that must be compensated
    * for when listening for mouse clicks
    */
  public void mouseClicked(MouseEvent e)
  {
    // Translate the users click into X and Y coordinates of a square
    int squareX = (int)Math.floor( (e.getPoint().getX() - 4 - BoardPanel.X_OFFSET)/BoardPanel.X_DIM );
    int squareY = (int)Math.floor( (e.getPoint().getY() - 50 - BoardPanel.Y_OFFSET)/BoardPanel.Y_DIM );
    
    // If the square is valid, call squareClicked
    if (game != null && squareX >= 0 && squareX < cols && squareY >= 0 && squareY < rows) {
      game.squareClicked(new Coordinate(squareY, squareX));
    }
  }
  
  /**This method is called when a menu item is selected
    * the appropriate actions are taken depending on which menu item was selected
    */
  public void actionPerformed(ActionEvent e) {
    // Prevent the user from using the menu if there is no dalek game
    if (game != null)
    {
      // Get the source of the action event
      JMenuItem source = (JMenuItem)(e.getSource());
      
      // Take action depending on the source of the action event
      // Most of these just involve calling the methods outlined in DalekGame
      if (source.getText() == "Restart")
      {
        game.restartGame();
      }
      else if (source.getText() == "Quit")
      {
        System.exit(0);
      }
      else if (source.getText() == "Safe teleport")
      {
        game.teleport(true);
      }
      else if (source.getText() == "Unsafe teleport")
      {
        game.teleport(false);
      }
      else if (source.getText() == "Wait")
      {
        game.keepWaiting();
      }
    }
  }
  
  /**Adds a DalekGame to this dalek board
    * We override Board's addBoardListener to add a DalekGame instead
    * This makes it much easier for CatchGame and DalekBoard to communicate with eachother
    * @param listener The Dalek to be added to the Board
    */
  public void addBoardListener(DalekGame listener)
  {
    game = listener;
  }
  
  /**Removes the Dalek from this dalek board
    */
  public void removeBoardListener()
  {
    game = null;
  }
  
}