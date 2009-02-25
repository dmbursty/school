import java.awt.*;

public class MarioMario
{
  public static void main(String[] args)
  {
    Collage mario = new Collage();
    
    // Hat!
    Collage hat = new Collage();
    Rectangle hat1 = new Rectangle(Color.RED, 1, 5);
    hat.add(hat1, 0, 1);
    Rectangle hat2 = new Rectangle(Color.RED, 1, 9);
    hat.add(hat2, 1, 0);
    mario.add(hat, 0, 2);
    // End Hat
    
    // Head
    Collage head = new Collage();
    
    Rectangle face = new Rectangle(Color.ORANGE, 4, 7);
    head.add(face, 1, 2);
    
    // Hair
    Collage hair = new Collage();
    
    Rectangle longHair = new Rectangle(Color.DARK_GRAY, 3, 4);
    hair.add(longHair, 1, 0);
    Rectangle topHair = new Rectangle(Color.DARK_GRAY, 1, 3);
    hair.add(topHair, 0, 1);
    Rectangle ear = new Rectangle(Color.ORANGE, 2, 1);
    hair.add(ear, 1, 1);
    Rectangle cheekbone = new Rectangle(Color.ORANGE, 1, 2);
    hair.add(cheekbone, 3, 2);
    Peg moreEar = new Peg(Color.ORANGE);
    hair.add(moreEar, 1, 3);
    
    head.add(hair, 0, 0);
    // End hair
    
    Rectangle forehead = new Rectangle(Color.ORANGE, 1, 4);
    head.add(forehead, 0, 4);
    Rectangle eye = new Rectangle(Color.DARK_GRAY, 2, 1);
    head.add(eye, 0, 6);
    
    // Moustache
    Collage moustache = new Collage();
    
    Rectangle pushbroom = new Rectangle(Color.DARK_GRAY, 2, 4);
    moustache.add(pushbroom, 0, 0);
    Peg eyeSkin = new Peg(Color.ORANGE);
    moustache.add(eyeSkin, 0, 0);
    Rectangle upperLip = new Rectangle(Color.ORANGE, 1, 3);
    moustache.add(upperLip, 0, 2);
    
    head.add(moustache, 2, 6);
    // End moustache
    
    Peg nose = new Peg(Color.ORANGE);
    head.add(nose, 1, 9);
    
    mario.add(head, 2, 1);
    // End head
    
    // Midsection
    Collage midsection = new Collage();
    
    // Overalls
    Collage overalls = new Collage();
    
    Rectangle overallsMain = new Rectangle(Color.RED, 5, 8);
    overalls.add(overallsMain, 1, 0);
    Peg overallsMainPlus = new Peg(Color.RED);
    overalls.add(overallsMainPlus, 0, 2);
    
    Square straps = new Square(Color.DARK_GRAY, 2);
    overalls.add(straps, 0, 3);
    Peg strap = new Peg(Color.DARK_GRAY);
    overalls.add(strap, 0, 5);
    
    Peg buttonLeft = new Peg(Color.ORANGE);
    overalls.add(buttonLeft, 3, 2);
    Peg buttonRight = new Peg(Color.ORANGE);
    overalls.add(buttonRight, 3, 5);
    
    Rectangle legLeft = new Rectangle(Color.RED, 1, 3);
    overalls.add(legLeft, 6, 0);
    Rectangle legRight = new Rectangle(Color.RED, 1, 3);
    overalls.add(legRight, 6, 5);
    
    midsection.add(overalls, 0, 2);
    // End overalls
    
    // Arms
    Collage leftArm = new Collage();
    
    Rectangle leftUpperArm = new Rectangle(Color.DARK_GRAY, 2, 3);
    leftArm.add(leftUpperArm, 1, 1);
    Peg leftWrist = new Peg(Color.DARK_GRAY);
    leftArm.add(leftWrist, 2, 0);
    Square leftShoulder = new Square(Color.DARK_GRAY, 2);
    leftArm.add(leftShoulder, 0, 2);
    
    midsection.add(leftArm, 0, 0);
    
    Collage rightArm = new Collage();
    
    Rectangle rightUpperArm = new Rectangle(Color.DARK_GRAY, 2, 3);
    rightArm.add(rightUpperArm, 0, 0);
    Peg rightShoulder = new Peg(Color.DARK_GRAY);
    rightArm.add(rightShoulder, 1, 3);
    Peg rightWrist = new Peg(Color.DARK_GRAY);
    rightArm.add(rightWrist, 2, 1);
    
    midsection.add(rightArm, 1, 8);
    // End arms
    
    // Hands
    Collage leftHand = new Collage();
    Rectangle leftPalm = new Rectangle(Color.ORANGE, 3, 2);
    leftHand.add(leftPalm, 0, 0);
    Peg leftThumb = new Peg(Color.ORANGE);
    leftHand.add(leftThumb, 1, 2);
    midsection.add(leftHand, 3, 0);
    
    Collage rightHand = new Collage();
    Rectangle rightPalm = new Rectangle(Color.ORANGE, 3, 2);
    rightHand.add(rightPalm, 0, 1);
    Peg rightThumb = new Peg(Color.ORANGE);
    rightHand.add(rightThumb, 1, 0);
    midsection.add(rightHand, 3, 9);
    // End hands
    
    mario.add(midsection, 7, 0);
    // End midsection
    
    // Shoes
    Collage shoes = new Collage();
    
    // Left shoe
    Rectangle leftShoeMain = new Rectangle(Color.DARK_GRAY, 2, 3);
    shoes.add(leftShoeMain, 0, 1);
    Peg leftShoeToe = new Peg(Color.DARK_GRAY);
    shoes.add(leftShoeToe, 1, 0);
    
    // Right shoe
    Rectangle rightShoeMain = new Rectangle(Color.DARK_GRAY, 2, 3);
    shoes.add(rightShoeMain, 0, 8);
    Peg rightShoeToe = new Peg(Color.DARK_GRAY);
    shoes.add(rightShoeToe, 1, 11);
    
    mario.add(shoes, 14, 0);
    // End shoes
    
    MarioMario.display(mario);
  }
  
  public static void display(Picture picture)
  {
    Board board = new Board(picture.getHeight(), picture.getWidth());
    picture.drawMe(board, 0, 0);
  }
}

