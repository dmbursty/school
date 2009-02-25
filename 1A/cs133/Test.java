import java.util.*;
public class Test {
  public static void main(String args[]) {
    for (int i=0;i<20;i++) {
      System.out.println(Math.round(Math.random()*12));
    }
    System.out.println((int)12.0000001);
  }
}