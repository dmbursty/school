/** 
 * Demonstrate saving and restoring MyBoard objects
 */
public class BoardSaver 
{ 
  public static void main(String[] args)
  { 
    MyBoard oneD = new MyBoard(6);
    oneD.putPeg(Board.GREEN, 3);
    oneD.putPeg(Board.BLUE, 5);
    oneD.save("oneD-savefile");

    MyBoard twoD = new MyBoard(4, 7);
    twoD.putPeg(Board.RED, 3, 3);
    twoD.putPeg(Board.PINK, 2, 6);
    twoD.putPeg(Board.ORANGE, 0, 0);
    twoD.save("twoD-savefile");

    MyBoard twoDcloneA = MyBoard.restore("twoD-savefile");
    MyBoard twoDcloneB = MyBoard.restore("twoD-savefile");
  } 
} 
