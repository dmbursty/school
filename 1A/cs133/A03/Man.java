/** The Man class is represented by a peg on a Board object. Its only 
 *  information is the colour of the peg representing it
 *  @author CS133 staff
 */
public class Man
{
  private boolean isRed;

  /** Constructs a Man object
   * @param isRed whether or not the peg representing this Man is red
   */ 
  public Man (boolean isRed)
  {
    this.isRed = isRed;
  }

  /** Returns the colour of the peg representing this Man as red or not
   * @return true if the peg representing this Man is red, false 
   * otherwise
   */
  public boolean isRed ()
  {
    return isRed;
  }
}

