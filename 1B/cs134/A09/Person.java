import java.util.Scanner;

public class Person {

  private int id;
  private int age;
  private String gender;
  private int[] friends;

 // The following are simple accessor methods.  
  public String getGender() { return gender; }
  public int getAge() { return age; }
  public int getId() { return id; }
  public int[] getFriends() { return friends; }

  public Person(int id, String gender, int age, int[] friends) {
    // pre: String is MALE or FEMALE.  age is non-negative.  friends is
    // not null.
    // post: creates one Person with these attributes.
    
    this.id = id;
    this.gender = gender;
    this.age = age;
    this.friends = friends;
  }
}
