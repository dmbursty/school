import java.awt.*;

public class Ubuntu
{
  public static void main(String[] args)
  {
    Collage ubuntu = new Collage();
    Circle main = new Circle(new Color(212,0,0),16);
    Circle middle = new Circle(Color.BLACK,10);
    Circle erase = new Circle(Color.BLACK,2);
    Circle head1 = new Circle(new Color(244,73,0),4);
    ubuntu.add(main,2,2);
    ubuntu.add(middle,5,5);
    ubuntu.add(erase,9,15);
    ubuntu.add(erase,9,16);
    ubuntu.add(erase,4,6);
    ubuntu.add(erase,3,5);
    ubuntu.add(erase,14,6);
    ubuntu.add(erase,15,5);
    ubuntu.add(head1,8,0);
    
    Board board = new Board(20,20);
    ubuntu.drawMe(board,0,0);
  }
}