/**
 * This interface defines a listener for receiving board clicks,
 * which are mouse clicks on a square in your implementation of 
 * the Board class.
 * 
 * A class that is interested in receiving such events should implement
 * this interface.  Objects created from such a class can then be
 * registered with a component using the <code>addBoardListener</code>
 * method.  When a mouse click occurs on a square in the board, a
 * Coordinate object giving the position of that click is created
 * and is used as the parameter to the <code>squareClicked</code>
 * method, which is invoked on the registered board listener object.
 * 
 * This listener replaces the <code>getClick</code> method that
 * existed in the original version of the <code>Board</code> class.
 */
public interface BoardListener
{
  /**
   * Invoked when a square is clicked on the board.
   * @param coord the coordinates of the click on the board.
   */
  public void squareClicked(Coordinate coord);
}
