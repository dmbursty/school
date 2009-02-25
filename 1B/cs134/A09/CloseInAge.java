import java.io.*;

public class CloseInAge {

  public static void main (String args[]) {
    try {
      SocialNetwork network = new SocialNetwork("data2.txt");
      System.out.println(network.closeInAgeCount("FEMALE"));
      System.out.println(network.closeInAgeCount("MALE"));      
    } catch (FileNotFoundException e) {
      System.err.println(e);
    }
  }
}