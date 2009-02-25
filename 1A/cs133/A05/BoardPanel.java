import javax.swing.*;
import java.awt.*;

/**This class is the Panel on which the Board is drawn
  * @author Daniel Burstyn - 20206120
  */
public class BoardPanel
  extends JPanel
{
  // Instance variables
  int rows;
  int cols;
  String message;
  Color[][] grid;
  
  // Some constants for drawing the board.
  // You may need more, and may need to alter
  // these values for some of the extensions.
  public static final int X_DIM = 30;
  public static final int Y_DIM = 30;
  public static final int X_OFFSET = 30;
  public static final int Y_OFFSET = 50;

  // Board colours
  public static final Color GRID_COLOR_A = new Color(84,137,139);
  public static final Color GRID_COLOR_B = new Color(103,156,158);
  public static final Color BLACK = new Color(0,0,0);
  
  /**The constructor sets the variables and prepares the BoardPanel
    * that the Board is to be drawn on
    * @param rows Number of rows of the board
    * @param columns Number of columns of the board
    */
  public BoardPanel(int rows, int columns)
  {
    int xSize = 2*X_OFFSET + columns*X_DIM;
    int ySize = 2*Y_OFFSET + rows*Y_DIM;
    this.rows = rows;
    this.cols = columns;
    this.message = "";
    emptyGrid();
    setPreferredSize(new Dimension(xSize, ySize));
    this.setFont(new Font("SanSerif", Font.BOLD|Font.ITALIC, 16));
  }
  
  /**Reset the array of peg locations to clear the board of all pegs
    */
  public void emptyGrid()
  {
    grid = new Color[rows][cols];
    
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        grid[i][j] = null;
      }
    }
  }
  
  /**Paint each of the components of the Board
    * @param canvas The Graphics object that the Board is to be drawn on
    */
  public void paintComponent(Graphics canvas)
  {
    canvas.clearRect(0,0,2*X_OFFSET + cols*X_DIM,2*Y_OFFSET + rows*Y_DIM);
    
    paintGrid(canvas);
    paintPegs(canvas);
    
    canvas.setColor(BLACK);
    canvas.drawString(message, X_OFFSET, Y_OFFSET + 20 + rows*Y_DIM);
  }
  
  /**Paint the grid component of the Board
    * @param canvas The Graphics object that the Board is to be drawn on
    */
  public void paintGrid(Graphics canvas)
  {
    // Go through each square of the board
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        // Alternate grid square colours
        if ((i+j)%2 == 1)
        {
          canvas.setColor(GRID_COLOR_A);
        }
        else
        {
          canvas.setColor(GRID_COLOR_B);
        }
        // Draw each square
        canvas.fillRect(X_OFFSET + X_DIM*j, Y_OFFSET + Y_DIM*i, X_DIM, Y_DIM);
      }
    }
  }
  
  /**Paint the pegs that are on the Board
    * @param canvas The Graphics object that the Board is to be drawn on
    */
  public void paintPegs(Graphics canvas)
  {
    // Go through every square of the board
    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        // If there is a peg on that square, then draw it
        if (grid[i][j] != null)
        {
          int x = X_OFFSET + j*X_DIM + X_DIM/4;
          int y = Y_OFFSET + i*Y_DIM + Y_DIM/4;
          int xsize = X_DIM/2;
          int ysize = Y_DIM/2;
          canvas.setColor(grid[i][j]);
          canvas.fillOval(x, y, xsize, ysize);
        }
      }
    }
  }
  
  /**Places a peg of the specified colour at the input square
    * @param theColour The colour of the peg
    * @param row The row location of the peg
    * @param column The column location of the peg
    */
  public void putPeg(Color theColour, int row, int column)
  {
    // If the input location is valid, then place the peg
    if (row >= 0 && row < rows && column >= 0 && column < cols)
    {
      grid[row][column] = theColour;
    }
  }
  
  /**Removes the peg at the specified location
    * @param row The row location
    * @param column The column location
    */
  public void removePeg(int row, int column)
  {
    // If the input location is valid, then remove the peg at that location
    // If there is no peg at the input location, there will be no effect
    if (row >= 0 && row < rows && column >= 0 && column < cols)
    {
      grid[row][column] = null;
    }
  }
  
  /**Add a message below the board
    * @param theMessage The message that will be displayed
    */
  public void displayMessage(String theMessage)
  {
    // Set the message variable to the input message
    message = theMessage;
  }
}