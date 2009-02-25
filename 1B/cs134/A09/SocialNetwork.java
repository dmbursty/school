import java.util.*;
import java.io.*;
import java.lang.Math;

public class SocialNetwork {

  private TableInterface people = DataFactory.makeTable();
  private int numPeople = 0;
  
  public SocialNetwork(String fileName) throws FileNotFoundException{
 // pre: fileName is not null and refers to properly formatted file
 // post: reads social network from file
     this(new Scanner(new File(fileName)));
  }
  
  public SocialNetwork(Scanner in) {
   // pre: in is not null and refer to properly formatted file
   // post: reads social network from scanner
    while (in.hasNext()) {
      people.insert(this.readOnePerson(in));
      numPeople++;
    }
  }
  
  private KeyedItem readOnePerson(Scanner in) {
    int id = in.nextInt();
    String gender = in.next();
    int age = in.nextInt();
    int numFriends = in.nextInt();
    int[] friends = new int[numFriends];
    for (int i=0; i < friends.length; i++) {
      friends[i] = in.nextInt();
    }
    
    return new KeyedItem(new Integer(id), new Person(id, gender, age, friends));
  }
  
  private int getAge(int id) {
   // pre: id refers to an ID number that exists in the network
   // post: returns age of the corresponding person
    return ((Person)people.retrieve(new Integer(id)).getValue()).getAge();
  }
  
  public int genderCount(String gender) {
   // pre: gender is either MALE or FEMALE
   // post: returns number of people in network with this gender
    int num = 0;
    Iterator search = people.getIterator();
    while (search.hasNext())
    {
       Person current = (Person)((KeyedItem)search.next()).getValue();
       if (current.getGender().equals(gender)) num++;
    }
    return num;
  }
  
   public int closeInAgeCount(String gender) {
    // pre: gender is either MALE or FEMALE
    // post: returns number of people of gender "gender" in 
    // network that have a friend that is not close in age
    int num = 0;
    
    Iterator search = people.getIterator();
    
    while (search.hasNext())
    {
      Person current = (Person)((KeyedItem)search.next()).getValue();
      if (current.getGender().equals(gender))
      {
        int[] friends = current.getFriends();
        for (int j=0; j < friends.length; j++) {
          if (Math.abs(current.getAge() - getAge(friends[j])) > 2) {
            num++;
            break;
          }
        }
      }
    }
    return num;
  }
}
