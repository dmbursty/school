/**This interface allows the GUI Menus of DalekBoard to
  * communicate directly with catchGame
  * @author Daniel Burstyn - 20206120
  */
public interface DalekGame extends BoardListener
{
  /**Resarts the instance of DalekGame from level 1
    */
  public void restartGame();
  
  /**Teleports the doctor either safely or unsafely
    * @param isSafe Whether the teleport is safe or not
    */
  public void teleport(boolean isSafe);
  
  /**Declare that the doctor will make no more moves
    * on this level, the rest of the level will proceed as
    * the user continually waits
    */
  public void keepWaiting();
}