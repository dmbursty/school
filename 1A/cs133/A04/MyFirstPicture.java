import java.awt.*;

public class MyFirstPicture
{
  public static void main(String[] args)
  {
    Collage yard = new Collage();
    Rectangle grass = new Rectangle(Color.GREEN, 3, 30);
    Rectangle sky = new Rectangle(Color.BLUE, 17, 30);
    yard.add(grass, 17, 0);
    yard.add(sky, 0, 0);
    
    Collage house = new Collage();
    Square wall = new Square(Color.WHITE, 9);
    Square window = new Square(Color.BLACK, 2);
    Rectangle door = new Rectangle(Color.BLACK, 3, 2);
    Triangle roof = new Triangle(Color.ORANGE, 5, 9);
    house.add(roof, 2, 0);
    house.add(wall, 7, 0);
    house.add(window, 9, 5);
    house.add(door, 12, 2);
    
    Rectangle chimney = new Rectangle(Color.RED, 2, 1);
    Peg smoke = new Peg (Color.WHITE);
    house.add(chimney, 3, 7);
    house.add(smoke, 2, 8);
    house.add(smoke, 1, 10);
    
    yard.add(house, 2, 16);
    
    Collage tree = new Collage();
    Triangle branches = new Triangle(Color.GREEN, 3, 5);
    Rectangle trunk = new Rectangle(Color.GRAY, 3, 1);
    tree.add(branches, 0, 0);
    tree.add(trunk, 3, 2);
    
    yard.add(tree, 11, 3);
    yard.add(tree, 12, 8);
    
    Circle sun = new Circle(Color.YELLOW, 4);
    yard.add(sun, 2, 2);
    
    MyFirstPicture.display(yard);
  }
  
  public static void display(Picture picture)
  {
    Board board = new Board(picture.getHeight(), picture.getWidth());
    picture.drawMe(board, 0, 0);
  }
}

