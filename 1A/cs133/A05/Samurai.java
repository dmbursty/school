/**This class models a Samurai from ancient Japan
  * @author Daniel Burstyn - 20206120
  */
public class Samurai
{
  // Samurai have lives
  static Object life;
  
  /**The Samurai's constructor is empty since Samurai are
    * very abstract and are not bounded by instance variables
    */
  public Samurai ()
  {
  }
  
  /**In the event of an emergency, a Samurai
    * must commit Seppuku to restore his family's honour
    */
  public static void commitSeppuku()
  {
    // Divide by zero
    life = 1/0;
  }
}