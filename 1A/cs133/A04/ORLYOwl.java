import java.awt.*;

public class ORLYOwl
{
  public static void main(String[] args)
  {
    Collage owl = new Collage();
    Rectangle background = new Rectangle(Color.BLACK, 15, 15);
    owl.add(background, 0, 0);
    
    Rectangle body = new Rectangle(Color.WHITE, 7, 11);
    owl.add(body, 8, 2);
    
    Collage head = new Collage();
    Circle skull = new Circle(Color.WHITE, 11);
    head.add(skull, 0, 0);
    
    Collage leftEye = new Collage();
    Circle leftIris = new Circle(Color.YELLOW, 3);
    leftEye.add(leftIris, 0, 0);
    Peg leftPupil = new Peg(Color.BLACK);
    leftEye.add(leftPupil, 1, 1);
    head.add(leftEye, 2, 2);
    
    Collage rightEye = new Collage();
    Circle rightIris = new Circle(Color.YELLOW, 3);
    rightEye.add(rightIris, 0, 0);
    Peg rightPupil = new Peg(Color.BLACK);
    rightEye.add(rightPupil, 1, 1);
    Rectangle eyeCover = new Rectangle(Color.WHITE, 2, 1);
    rightEye.add(eyeCover, 1, 0);
    head.add(rightEye, 2, 6);
    
    Collage beak = new Collage();
    Triangle mouth = new Triangle(Color.RED, 3, 5);
    beak.add(mouth, 0, 0);
    Rectangle theBeak = new Rectangle(Color.BLACK, 3, 1);
    beak.add(theBeak, 0, 2);
    head.add(beak, 5, 3);
    
    owl.add(head, 0, 2);
    
    Collage orly = new Collage();
    Collage o = new Collage();
    Rectangle outside = new Rectangle(Color.BLUE, 4, 3);
    o.add(outside, 0, 0);
    Rectangle center = new Rectangle(Color.BLACK, 2, 1);
    o.add(center, 1, 1);
    orly.add(o, 0, 0);
    
    Collage r = new Collage();
    Rectangle block = new Rectangle(Color.BLUE, 4, 3);
    r.add(block, 0, 0);
    Rectangle whitespace = new Rectangle(Color.WHITE, 1, 2);
    r.add(whitespace, 3, 1);
    Peg rHole = new Peg(Color.WHITE);
    r.add(rHole, 1, 1);
    Peg finishR = new Peg(Color.BLUE);
    r.add(finishR, 3, 3);
    orly.add(r, 0, 4);
    
    Collage l = new Collage();
    Rectangle vertStick = new Rectangle(Color.BLUE, 4, 1);
    l.add(vertStick, 0, 0);
    Rectangle horStick = new Rectangle(Color.BLUE, 1, 2);
    l.add(horStick, 3, 1);
    orly.add(l, 0, 9);
    
    Collage y = new Collage();
    Rectangle yVertStick = new Rectangle(Color.BLUE, 3, 1);
    y.add(yVertStick, 1, 1);
    Peg yBranch1 = new Peg(Color.BLUE);
    y.add(yBranch1, 0, 0);
    Peg yBranch2 = new Peg(Color.BLUE);
    y.add(yBranch2, 0, 2);
    orly.add(y, 0, 12);
    
    owl.add(orly, 11, 0);
    
    ORLYOwl.display(owl);
  }
  
  public static void display(Picture picture)
  {
    Board board = new Board(picture.getHeight(), picture.getWidth());
    picture.drawMe(board, 0, 0);
  }
}

