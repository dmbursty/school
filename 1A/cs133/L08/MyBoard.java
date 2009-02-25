import java.awt.Color;
import java.io.*;
import java.util.*;

/** 
 * A version of Board that permits queries, and save/restore operations
 * on files.
 */
public class MyBoard extends Board 
{
  private Color[][] grid;
    
  /** 
   * Create a 2D board with rows rows and cols columns. 
   * @param rows the number of rows for this board. pre: rows > 0
   * @param cols the number of columns for this board. pre: cols > 0
   */
  public MyBoard(int rows, int cols) 
  {
    super(rows, cols);
    grid = new Color[rows+1][cols+1];
  }

  /** 
   * Create a 1D board with cols columns.
   * @param cols the number of columns for this board. pre: cols > 0
   */
  public MyBoard(int cols) 
  {
    super(cols);
    grid = new Color[1][cols + 1];
  }

  /** 
   * Create a new board by reading a file.
   * @param filename the path to the savefile. pre: filename != null. 
   * @return a new board object specified according to the file 
   *   in filename.
   */
  public static MyBoard restore(String filename)
  {
    return new MyBoard(1,1);
  }
  
  /** 
   * Put a peg on the (2D) board. 
   * @param color the peg colour. pre: color != null
   * @param row, col the row and column for the peg. 
   *   pre: 0 <= row < this.getRows(), 0 <= col < this.getColumns()
   */
  public void putPeg(Color color, int row, int col) 
  {
    super.putPeg(color, row, col);
    grid[row][col] = color;
  }
  
  /** 
   * Put a peg on a 1D board. 
   * @param color the peg colour. pre: color != null
   * @param col the column for this peg. pre: 0 <= col < this.getColumns()
   */
  public void putPeg(Color color, int col) 
  {
    super.putPeg(color, col);
    grid[0][col] = color;
  }
  
  /** 
   * Remove a peg from a 2D board. 
   * @param row, col the row and column to clear. 
   * pre: 0 <= row < this.getRows(), 0 <= col < this.getColumns()
   */
  public void removePeg(int row, int col)
  {
    super.removePeg(row, col);
    grid[row][col] = null;
  }
  
  /** 
   * Remove a peg from a 1D board. 
   * @param col the column to clear. 
   * pre: 0 <= col < this.getColumns()
   */
  public void removePeg(int col)
  {
    super.removePeg(col);
    grid[0][col] = null;
  }
  
  /** 
   * Get the colour of a peg. 
   * @param row, col the position to query. 
   * pre: 0 <= col < this.getColumns(), 0 <= row < this.getRows()
   * @return the color of the peg at (row, col), or null if there
   * is no peg at that position.
   */
  public Color getPeg(int row, int col) 
  {
    return grid[row][col];
  }
  
  /** 
   * Get the colour of a peg. 
   * @param col the position to query. 
   * pre: 0 <= col < this.getColumns()
   * @return the color of the peg at col, or null if there
   * is no peg at that position.
   */
  public Color getPeg(int col) 
  {
    return grid[0][col];
  }

  /** 
   * Save the state of this board to a file (not including messages
   * and lines). 
   * @param filename the file to save this board to. 
   * pre: filename != null.
   * pre: this board only has peg colours defined by Board constants.
   */
  public void save(String filename) 
  { 
    // Your code here.
  } 

  /** 
   * Demonstrate the new board. 
   */
  public static void main(String[] args) 
  {
    MyBoard myBoard = new MyBoard(5, 5);
    myBoard.putPeg(Board.GREEN, 0, 0);
    myBoard.putPeg(myBoard.getPeg(0, 0), 4, 4);
    myBoard.putPeg(Board.YELLOW, 0, 4);
    myBoard.putPeg(myBoard.getPeg(0, 4), 4, 0);
  }
  
}
