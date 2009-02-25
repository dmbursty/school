public class CoordinateMaker
{
  public static Coordinate createCoordinate(int rows, int columns)
  {
    return new Coordinate(rows, columns);
  }
  
  public static void main(String[] args)
  {
    Coordinate coord = null;
    coord = createCoordinate(3, 7);
    System.out.println("coord has the value (" + coord.getRow() + ", " + coord.getCol() + ")");
  }
}
