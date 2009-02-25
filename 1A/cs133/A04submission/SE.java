import java.awt.*;

public class SE
{
  public static void main(String[] args)
  {
    Color gray = new Color(134,134,134);
    
    Collage SE = new Collage();
    Collage s = new Collage();
    Collage e = new Collage();
    
    Collage topS = new Collage();
    Rectangle bar10w = new Rectangle(Color.WHITE,1,10);
    Rectangle bar6w = new Rectangle(Color.WHITE,1,6);
    Rectangle bar2w = new Rectangle(Color.WHITE,1,2);
    topS.add(bar6w,0,0);
    topS.add(bar6w,2,4);
    topS.add(bar10w,1,0);
    topS.add(bar2w,2,0);
    topS.add(bar2w,0,8);
    
    s.add(topS,0,2);
    
    Rectangle block5w = new Rectangle(Color.WHITE,3,5);
    Rectangle block10w = new Rectangle(Color.WHITE,3,10);
    
    s.add(block5w,3,1);
    s.add(block10w,6,2);
    s.add(block5w,9,8);
    s.add(block10w,12,2);
    
    Collage topE = new Collage();
    Rectangle bar10g = new Rectangle(gray,1,10);
    Rectangle bar6g = new Rectangle(gray,1,6);
    Rectangle bar2g = new Rectangle(gray,1,2);
    topE.add(bar6g,0,0);
    topE.add(bar6g,2,4);
    topE.add(bar10g,1,0);
    topE.add(bar2g,2,0);
    topE.add(bar2g,0,8);
    
    e.add(topE,0,2);
    
    Rectangle bigBlock = new Rectangle(gray,12,4);
    Rectangle smallBlock = new Rectangle(gray,3,6);
    
    e.add(bigBlock,3,2);
    e.add(smallBlock,6,5);
    e.add(smallBlock,12,6);
    
    Rectangle background = new Rectangle(Color.BLUE,20,22);
    
    SE.add(background,0,0);
    SE.add(e,4,9);
    SE.add(s,1,0);
    
    Board board = new Board(20,22);
    SE.drawMe(board,0,0);
  }
}