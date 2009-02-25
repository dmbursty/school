/**This class models the Doctor in the game. A Doctor has 
 * a position and can move to a new position.
 * @author Daniel Burstyn - 20206120
 */
public class Doctor
{
  private int row, col;
  
  /**Initializes the variables for a Doctor. 
   * @param theRow The row this Doctor starts at.
   * @param theCol The column this Doctor starts at.
   */
  public Doctor(int theRow, int theCol)
  {
    row = theRow;
    col = theCol;
  }
  
  
  /**Move the Doctor. If the player clicks on one of the squares 
   * immediately surrounding the Doctor, the peg is moved to that 
   * location. Clicking on the Doctor does not move the peg, but instead 
   * allows the Doctor to wait in place for a turn. Clicking on any 
   * other square causes the Doctor to teleport to a random square 
   * (perhaps by using a “sonic screwdriver”). Teleportation is 
   * completely random.
   * @param newRow The row the player clicked on.
   * @param newCol The column the player clicked on.
   */
  public void move(int newRow, int newCol)
  {
    // If the user has clicked in a square that is not adjacent to the doctor, set the new location to a random square
    if (Math.abs(row - newRow) > 1 || Math.abs(col - newCol) > 1) {
      newRow = (int)(Math.random()*11);
      newCol = (int)(Math.random()*11);
    }
    
    // Move the doctor to the new square
    row = newRow;
    col = newCol;
  }

  
  /**Returns the row of this Doctor.
   * @return This Doctor's row.
   */
  public int getRow()
  {
    return row;
  }
  
  
  /**Returns the column of this Doctor.
   * @return This Doctor's column.
   */
  public int getCol()
  {
    return col;
  }
  
}
