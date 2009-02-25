import java.util.*;
/**This Class gets a date input from the user, and displays it
 * in a calendar of the month of the input date.
 * @author Daniel Burstyn - 20206120
 */
public class Lab05a
{
   public static void main(String[] args)
   {
      // Variables
      Scanner myScanner;
      int day;
      int month;
      int year;
      int monthStarts;
      int daysInMonth;
      MyDate myDate;
      Board myBoard;
      
      // Get input from user
      System.out.println("Please input the day of the month");
      myScanner = new Scanner(System.in);
      day = myScanner.nextInt();
      System.out.println("Please input the month (1-12)");
      month = myScanner.nextInt();
      System.out.println("Please input the year (1800-2199)");
      year = myScanner.nextInt();
      
      // Create MyDate object
      myDate = new MyDate(day,month,year);
      
      // Create Board (The calendar)
      myBoard = new Board(6,7);
      
      // Calculate the starting day of the month
      monthStarts = myDate.dayOfWeek() - myDate.getDay();
      while (monthStarts < 0) {
        monthStarts += 7;
      }
      
      // Calculate the number of days in the month
      switch(myDate.getMonth()) {
        case 1:
        case 3:
        case 5:
        case 7:
        case 8:
        case 10:
        case 12: daysInMonth = 31; break;
        case 4:
        case 6:
        case 9:
        case 11: daysInMonth = 30; break;
        case 2:
        {
          if ( myDate.getYear() % 4 == 0 && myDate.getYear() % 100 != 0 )
          {
            daysInMonth = 29;
          }
          else
          {
            daysInMonth = 28;
          }
        }
        break;
        default: daysInMonth = 0; break;
      }
        
      // Draw a black1 peg for each day in the month
      for (int i = 1 ; i < daysInMonth ; i++) {
        myBoard.putPeg(myBoard.BLACK,(int)Math.floor((monthStarts + i)/7),(monthStarts + i) % 7);
      }
          
      // Draw a yellow peg for the input date
      myBoard.putPeg(myBoard.YELLOW,(int)Math.floor((monthStarts + myDate.getDay())/7),(monthStarts + myDate.getDay()) % 7);
      
      // Add a message of the input date
      myBoard.displayMessage(myDate.toString());
   }
}
