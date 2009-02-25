// CS134 Assignment 1
// Winter 2007
// Daniel Burstyn
// 20206120

import java.util.*;
import java.io.*;

// This class models a social network of Person objects
public class SocialNetwork {
  
  // Instance variables
  private Person[] network;
  private int numPeople;

  // Constructs a social network from the file "data.txt"
  public SocialNetwork() {
    
    // Initialize variables
    network = new Person[1000];
    numPeople = 0;
    
    // Declare variables used for file reading
    FileReader in;
    int[] temp = new int[4];
    int[] tempFriends;
    
    // Open the file with a file reader
    try {
      in = new FileReader("data.txt");
    }
    catch (FileNotFoundException e)
    {
      return;
    }
    
    // Create a scanner for the file
    Scanner sc = new Scanner(in);
    
    // Repeat through the file for each person
    while (sc.hasNext()) {
      
      // Store the basic information for each person
      temp[0] = Integer.parseInt(sc.nextLine());       //Person's ID
      temp[1] = sc.nextLine().equals("MALE") ? 1 : 0;  //Perons's gender
      temp[2] = Integer.parseInt(sc.nextLine());       //Person's age
      temp[3] = Integer.parseInt(sc.nextLine());       //Person's number of friends
      
      // Create an array of appropriate size to store friend ID's
      tempFriends = new int[temp[3]];
      
      // Store each of the friends' IDs
      for (int i=0; i < temp[3]; i++)
      {
        tempFriends[i] = Integer.parseInt(sc.nextLine().trim());
      }
      
      // Add a new person with the stored information to the network and increment the network size
      network[numPeople] = new Person(temp[0], temp[1] == 1 ? true : false, temp[2], tempFriends);
      numPeople++;
    }
    
  }
  
  // Accessor method for the number of people in the network
  public int getSize()
  {
    return numPeople;
  }
  
  // Searches for and returns the Person object with the specified ID
  // Used when searching for a particular person
  // pre: A person with the input ID exists in the network
  // post: Returns the person object with the input ID. If the person doesn't exist, returns null
  public Person getPersonID(int findID)
  {
    for (int i = 0; i < numPeople; i++)
    {
      if (network[i].getID() == findID)
      {
        return network[i];
      }
    }
    return null;
  }
  
  // Searches for and returns the Person in the social network with the specified index
  // Indices are assigned in the order persons are added to the network
  // Used when iterating through all people in the network
  // pre: 0 <= index < numPeople
  // post: Returns the (index)th person to be added to the network. Returns null if precondition is not met
  public Person getPersonIndex(int index)
  {
    if (0 <= index && index < numPeople)
    {
      return network[index];
    }
    return null;
  }
}
          