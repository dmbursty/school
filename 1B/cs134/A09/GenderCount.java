import java.io.*;

public class GenderCount {
 
  public static void main (String args[]) {
    try {
      SocialNetwork network = new SocialNetwork("data2.txt");
      System.out.println(network.genderCount("FEMALE"));
      System.out.println(network.genderCount("MALE"));      
    } catch (FileNotFoundException e) {
      System.err.println(e);
    }
  }
}