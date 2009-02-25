/**This class is used to test the MyDate class
 * @author Daniel Burstyn - 20206120
 */
public class TestMyDate
{
  public static void main(String[] args)
  {
    // Create some MyDate objects
    MyDate myDate = new MyDate(29,2,1872);
    MyDate badDate = new MyDate(1,5,1712);
    MyDate anotherBadDate = new MyDate (-3,2,2004);
    
    // Test the methods of a valid date
    System.out.println(myDate.toString());
    System.out.println(myDate.dayOfWeek());

    // Test the methods of an invalid date
    System.out.println(badDate.toString());
    System.out.println(badDate.dayOfWeek());
    
    // Test the equals method
    System.out.println(myDate.equals(badDate));
    System.out.println(badDate.equals(anotherBadDate));

  }
}