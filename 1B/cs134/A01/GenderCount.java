// CS134 Assignment 1
// Winter 2007
// Daniel Burstyn
// 20206120

// This class counts the number of males and females in a social network
public class GenderCount
{
  public static void main(String[] args)
  {
    // Declare and initialize variables
    SocialNetwork network = new SocialNetwork();
    int size = network.getSize();
    int numMales = 0;
    int numFemales = 0;
    
    // Search through each person in the social network
    for (int i = 0; i < size; i++)
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
    }
    
    // Output the resulting totals
    System.out.println(numFemales);
    System.out.println(numMales);
  }
}