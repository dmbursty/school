/**This class models a Delek in the game. A Delek has 
  * a position and can advance towards the Doctor.
  * @author Daniel Burstyn - 20206120
  */
public class Dalek
{
  private int row, col;
  private boolean crashed;
  
  /**Initializes the variables for a Dalek. 
    * @param theRow The row this Dalek starts at.
    * @param theCol The column this Dalek starts at.
    */
  public Dalek(int theRow, int theCol)
  { 
    row = theRow;
    col = theCol;
    crashed = false;
  }
  
  /**Attempts to move the Dalek towards the Doctor by the most direct route, 
    * moving up, down, right, left or diagonally. For example, if the 
    * Doctor is above and to the right of a Dalek, it will move diagonally. 
    * If the Doctor is directly below a Dalek, it will move down.
    * @param doc The Doctor to move towards.
    */
  public void advanceTowards(Doctor doc)
  {
    // Check if the dalek has crashed
    if (!crashed) {
      
      // The dalek has a chance to move one square horizontally, and/or one square vertically
      // This will result in the dalek moving at most one square diagonally
      
      // If the dalek is not in the same row as the doctor, then move vertically towards the doctor
      if (row > doc.getRow()) {
        row--;
      } else if (row < doc.getRow()) {
        row++;
      }
      
      // If the dalek is not in the same column as the doctor, then move horizontally towards the doctor
      if (col > doc.getCol()) {
        col--;
      } else if (col < doc.getCol()) {
        col++;
      }
    }
  }
  
  
  /**Returns the row of this Dalek.
    * @return This Dalek's row.
    */
  public int getRow()
  {
    return row;
  }
  
  
  /**Returns the column of this Dalek.
    * @return This Dalek's column.
    */
  public int getCol()
  {
    return col;
  }
  
  
  /**Sets the Dalek to be in a crashed state.
    */
  public void crash()
  {
    crashed = true;
  }
  
  
  /**Returns whether or not this Dalek has crashed.
    * @return true if this Dalek has crashed,
    *         false otherwise 
    */
  public boolean hasCrashed()
  {
    return crashed;
  }
  
}
