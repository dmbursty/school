// CS134 Assignment 1
// Winter 2007
// Daniel Burstyn
// 20206120

// This class models a person in a social network
public class Person
{
  // Instance Variables
  private int ID;
  private int age;
  private boolean isMale;
  private int[] friends; //Array of friend's ID's
  
  // Constructs a person
  public Person(int ID, boolean isMale, int age, int[] friends)
  {
    this.ID = ID;
    this.age = age;
    this.isMale = isMale;
    this.friends = friends;
  }
  
  //Accessor method for this person's ID
  public int getID()
  {
    return ID;
  }
  
  //Accessor method for this person's age
  public int getAge()
  {
    return age;
  }
  
  // Accessor method for this person's gender
  // returns true if male, and false if female
  public boolean getGender()
  {
    return isMale;
  }
  
  // Accessor method for this person's array of friend ID's
  public int[] getFriends()
  {
    return friends;
  }
}