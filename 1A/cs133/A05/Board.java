import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** This class models a Board used in a board game
  * @author Daniel Burstyn - 20206120
  */
public class Board extends JFrame implements MouseListener
{
  // Some colour presets
  public static final Color YELLOW = Color.YELLOW;
  public static final Color BLUE = Color.BLUE;
  public static final Color CYAN = Color.CYAN;
  public static final Color GREEN = Color.GREEN;
  public static final Color PINK = Color.PINK;
  public static final Color BLACK = Color.BLACK;
  public static final Color WHITE = Color.WHITE;
  public static final Color RED = Color.RED;
  public static final Color ORANGE = Color.ORANGE;

  // Instance variables
  public int rows;
  public int cols;
  public BoardPanel board;
  public BoardListener boardListener;
  
  /**Initialize the instance variables and prepares the frame to be drawn on
    * @param rows The number of rows of the board
    * @param columns The number of columns of the board
    */
  public Board(int rows, int columns)
  {
    this.rows = rows;
    this.cols = columns;
    Container content = getContentPane();
    board = new BoardPanel(rows, columns);
    content.add(board);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
    setVisible(true);
    this.setTitle("Board Game");
    this.setResizable(false);
    addMouseListener(this);
  }
  
  /**Add a message below the boared
    * @param theMessage The message to be displayed
    */
  public void displayMessage(String theMessage)
  {
    board.displayMessage(theMessage);
    board.repaint();
  }
  
  /**Put a peg of the specified colour at the input location
    * @param theColour The colour of the peg
    * @param row The row location of the peg
    * @param col The column location of the peg
    */
  public void putPeg(Color theColour, int row, int col)
  {
    board.putPeg(theColour, row, col);
    board.repaint();
  }
  
  /**Removes the peg at the specified location
    * @param row The row location
    * @param col The column location
    */
  public void removePeg(int row, int col)
  {
    board.removePeg(row, col);
    board.repaint();
  }
  
  /**Returns the number of columns of the board
    * @return The number of columns of this board
    */
  public int getColumns()
  {
    return cols;
  }
  
  /**Returns the number of rows of the board
    * @return The number of rows of this board
    */
  public int getRows()
  {
    return rows;
  }
  
  /**This method is called when the user clicks on the frame
    * Translates the click location into a coordinate of a square on the board
    * and calls the squareClicked method of this board's BoardListener with that coordinate
    */
  public void mouseClicked(MouseEvent e)
  {
    // Translate the users click into X and Y coordinates of a square
    int squareX = (int)Math.floor( (e.getPoint().getX() - 4 - BoardPanel.X_OFFSET)/BoardPanel.X_DIM );
    int squareY = (int)Math.floor( (e.getPoint().getY() - 30 - BoardPanel.Y_OFFSET)/BoardPanel.Y_DIM );
    
    // If the square is valid, call squareClicked
    if (boardListener != null && squareX >= 0 && squareX < cols && squareY >= 0 && squareY < rows) {
      boardListener.squareClicked(new Coordinate(squareY, squareX));
    }
  }
  
  // Extraneous methods from the MouseListener class are not implemented
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  
  /**Adds a BoardListener to this board
    * @param listener The BoardListener to be added to the Board
    */
  public void addBoardListener(BoardListener listener)
  {
    boardListener = listener;
  }
  
  /**Removes the BoardListener from this board
    */
  public void removeBoardListener()
  {
    boardListener = null;
  }
}
