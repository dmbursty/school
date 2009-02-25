import java.awt.*;

public class Pi
{
  public static void main(String[] args)
  {
    Collage pi = new Collage();
    Rectangle background = new Rectangle(Color.BLACK,10,10);
    Rectangle leg = new Rectangle(Color.WHITE,6,2);
    
    Collage top = new Collage();
    Rectangle topBar = new Rectangle(Color.WHITE,2,9);
    Triangle topBarTip = new Triangle(Color.WHITE,2,3);
    top.add(topBar,0,1);
    top.add(topBarTip,0,0);
    
    Collage left = new Collage();
    Circle leftBall = new Circle(Color.WHITE,2);
    left.add(leg,0,1);
    left.add(leftBall,5,0);
    
    Collage right = new Collage();
    Circle rightBall = new Circle(Color.WHITE,4);
    Peg rightClear = new Peg(Color.BLACK);
    right.add(leg,0,0);
    right.add(rightBall,3,0);
    right.add(rightClear,3,2);
    right.add(rightClear,4,2);
    
    pi.add(background,0,0);
    pi.add(top,1,0);
    pi.add(left,3,1);
    pi.add(right,3,6);
    
    Board board = new Board(10,10);
    pi.drawMe(board,0,0);
  }
}