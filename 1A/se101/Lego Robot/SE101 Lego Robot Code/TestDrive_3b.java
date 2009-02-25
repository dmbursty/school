import josx.platform.rcx.*;

public class TestDrive 
{ 
 public static void main(String[] args) throws InterruptedException 
 {
  Sensor.S1.activate();
  

  
  Sensor.S3.activate();
     
  //Get the values from the sensors for each colour
  TextLCD.print("GREEN");
  Button.PRGM.waitForPressAndRelease();
  final int colourGreen1 = Sensor.S1.readValue();
  final int colourGreen3 = Sensor.S3.readValue();
  LCD.showNumber(colourGreen1);
  Thread.sleep(750);
  LCD.showNumber(colourGreen3);
  Thread.sleep(750);
  
  TextLCD.print("WHITE");
  Button.PRGM.waitForPressAndRelease();
  final int colourWhite1 = Sensor.S1.readValue();
  final int colourWhite3 = Sensor.S3.readValue();
  LCD.showNumber(colourWhite1);
  Thread.sleep(750);
  LCD.showNumber(colourWhite3);
  Thread.sleep(750);
  
  TextLCD.print("Silver");
  Button.PRGM.waitForPressAndRelease();
  final int colourSilver1 = Sensor.S1.readValue();
  final int colourSilver3 = Sensor.S3.readValue();
  LCD.showNumber(colourSilver1);
  Thread.sleep(750);
  LCD.showNumber(colourSilver3);
  Thread.sleep(750);
  
  TextLCD.print("BLACK");
  Button.PRGM.waitForPressAndRelease();
  final int colourBlack1 = Sensor.S1.readValue();
  final int colourBlack3 = Sensor.S3.readValue();
  LCD.showNumber(colourBlack1);
  Thread.sleep(750);
  LCD.showNumber(colourBlack3);
  Thread.sleep(750);
  
  Button.RUN.waitForPressAndRelease();

  
    Sensor.S1.addSensorListener(new SensorListener() {
    
    public void stateChanged(Sensor src, int oldValue, int newValue)
    {
      try
      {
        if ((colourGreen1 - 2) <= newValue && (colourGreen1 + 2) >= newValue)
        {
          //stop
          Motor.A.stop();
          //LCD.showNumber(newValue);
        }
        else if (colourSilver1 + 2 >= newValue)
        {
          //right turn, so stop
          Motor.A.stop();
        }
        else if (colourBlack1 - 2 <= newValue && colourBlack1 + 2 >= newValue)
        {
          //left turn, so forward()
          Motor.A.forward();
        }
        else
        {
          //keep going (white)
          Motor.A.forward();
        }
        Thread.sleep(20);
      }
      catch (InterruptedException e)
      {
        
      }
    }
  });
  
   Sensor.S3.addSensorListener(new SensorListener() {
    
    public void stateChanged(Sensor src, int oldValue, int newValue)
    {
      try
      {
        if ((colourGreen3 - 2) <= newValue && (colourGreen3 + 2) >= newValue)
        {
          //stop
          Motor.C.stop();
          //LCD.showNumber(newValue);
        }
        else if (colourSilver3 + 2 >= newValue)
        {
          //right turn, so flt()
          Motor.C.forward();
        }
        else if (colourBlack3 - 2 <= newValue && colourBlack3 + 2 >= newValue)
        {
          //left turn, so forward()
          Motor.C.stop();
        }
        else
        {
          //keep going (white)
          Motor.C.forward();
        }
        Thread.sleep(20);
      }
      catch (InterruptedException e)
      {
        
      }
    }
  });
  
  // Start The Motors
  Motor.A.setPower(1);
  Motor.C.setPower(1);
  Motor.A.forward();
  Motor.C.forward();
  Button.RUN.waitForPressAndRelease();
    
 }
 private void selfDestruct() 
 {
    
 }
}