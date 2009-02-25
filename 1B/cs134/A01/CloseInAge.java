// CS134 Assignment 1
// Winter 2007
// Daniel Burstyn
// 20206120

/* This class lists the number of males and females in a social network that have
 * friends that are not close in age (3 or more years apart)
 */
public class CloseInAge 
{
  public static void main(String[] args)
  {
    // Declare and initialize variables
    SocialNetwork network = new SocialNetwork();
    int numMales = 0;
    int numFemales = 0;
    int size = network.getSize();
    
    // Declare varibles for storing current person's information
    int age;
    int[] friends;
    
    // Repeat for all people in the social network
    for (int i = 0; i < size; i++)
    {
      // Store the current person's age, and array of friend ID's
      age = network.getPersonIndex(i).getAge();
      friends = network.getPersonIndex(i).getFriends();
      
      // Iterate through all this person's friends
      for (int j = 0; j < friends.length; j++)
      {
        // If they are 3 or more years apart
        if (Math.abs(age - network.getPersonID(friends[j]).getAge()) >= 3)
        {
          // Increment the counter for the appropriate gender
          if (network.getPersonIndex(i).getGender())
          {
            numMales++;
          }
          else
          {
            numFemales++;
          }
          
          // Stop looping through friends, and move on to the next person
          break;
        }
      }
    }
    
    // Output the resulting totals
    System.out.println(numFemales);
    System.out.println(numMales);
  }    
}