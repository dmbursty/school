import java.util.*;
/**This Class models a date
 * @author Daniel Burstyn - 20206120
 */
public class MyDate
{
    // Instance variables
    private int day;
    private int month;
    private int year;

    /**Constructs the Date object by initializing day, month, and year variables
     * @param day The day of the month.
     * @param month The month of the year (1-12).
     * @param year The year (1800-2199).
     */
    public MyDate(int day, int month, int year)
    {
      // If the input date is invalid, initialize the date to October 1st, 1988
      if ( year < 1800 || year >= 2200 || month < 1 || month > 12 || day < 1 || day > 31 ) {
        this.day = 1;
        this.month = 10;
        this.year = 1988;
      }
      else if ( ( day == 31 && ( month == 2 || month == 4 || month == 6 || month == 9 || month == 11 ) ) || ( day == 30 && month == 2 ) || ( day == 29 && month == 2 && ( year % 4 != 0 || year % 100 == 0 ) ) )
      {
        this.day = 1;
        this.month = 10;
        this.year = 1988;
      }
      
      // Otherwise initialize the date to the input date
      else
      {
        this.day = day;
        this.month = month;
        this.year = year;
      }
    }
    
    /**Returns the day of the month of this date
     * @return This date's day of the month.
     */
    public int getDay()
    {
      return day;
    }
    
    /**Returns the month of this date
     * @return This date's month (1-12).
     */
    public int getMonth()
    {
      return month;
    }
    
    /**Returns the year of this date
     * @return This date's year.
     */
    public int getYear()
    {
      return year;
    }
    
    /**Returns a String representation of the date
     * @return The date, as a string
     */
    public String toString()
    {
      String fullDate;
      switch(month) {
        case 1: fullDate = "January"; break;
        case 2: fullDate = "February"; break;
        case 3: fullDate = "March"; break;
        case 4: fullDate = "April"; break;
        case 5: fullDate = "May"; break;
        case 6: fullDate = "June"; break;
        case 7: fullDate = "July"; break;
        case 8: fullDate = "August"; break;
        case 9: fullDate = "September"; break;
        case 10: fullDate = "October"; break;
        case 11: fullDate = "November"; break;
        case 12: fullDate = "December"; break;
        default : fullDate = "Invalid month error"; break;
      }
      fullDate += " " + day + ", " + year;
      return fullDate;
    }
    
    /**Returns true if this date is equal to the other input date
     * @return True, if the two dates are the same, False otherwise
     */
    public boolean equals(MyDate other)
    {
      if (day == other.getDay() && month == other.getMonth() && year == other.getYear())
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    
    /**Returns the day of the week of this date, as an integer from 0-6
     * 0 being sunday, and 6 being saturday.
     * @return The day of the week of this date from 0 (sunday) to 6 (saturday)
     */
    public int dayOfWeek()
    {
      int dayOfWeek = 0;
      
      // Calculate the day of the week using tables corresponding to the century, and month
      // as well as some arithmetic algorithms applied to the year and day.
      
      // Take a number from the century table
      switch((int)Math.floor(year/100)) {
        case 19: dayOfWeek = 0; break;
        case 18: dayOfWeek = 2; break;
        case 21: dayOfWeek = 4; break;
        case 20: dayOfWeek = 6; break;
      }
      
      // Add the last 2 digits of the year
      dayOfWeek += year%100;
      
      // Divide the last 2 digits of the year by 4, drop the decimal, and add it
      dayOfWeek += (int)Math.floor((year%100)/4);
      
      // Add a number from the table for months
      switch(month) {
        case 1:
        case 10: dayOfWeek += 0; break;
        case 5: dayOfWeek += 1; break;
        case 8: dayOfWeek += 2; break;
        case 2:
        case 3:
        case 11: dayOfWeek += 3; break;
        case 6: dayOfWeek += 4; break;
        case 9:
        case 12: dayOfWeek += 5; break;
        case 4:
        case 7: dayOfWeek += 6; break;
      }
      
      // Adjust for leap years
      if ( year % 4 == 0 && year % 100 != 0 ) 
      {
        dayOfWeek--;
      }
      
      // Add the day of the month
      dayOfWeek += day;
      
      // Divide the total by 7, and return the remainder (The day of the week)
      return dayOfWeek % 7;
    }
}
