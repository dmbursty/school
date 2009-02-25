import josx.platform.rcx.*;

public class TestDrive 
{ 
 public static void main(String[] args) throws InterruptedException 
 {
  Sensor.S1.activate();
  Sensor.S3.activate();
 
  // Integer values to hold the sensor values for each colour
  //int colourGreen;
  //int colourWhite;
  //int colourSilver;
  //int colourBlack;

    
  //Get the values from the sensors for each colour
  TextLCD.print("GREEN");
  Button.PRGM.waitForPressAndRelease();
  final int colourGreen = (Sensor.S1.readValue() + Sensor.S3.readValue())/2;
  TextLCD.print("WHITE");
  Button.PRGM.waitForPressAndRelease();
  final int colourWhite = (Sensor.S1.readValue() + Sensor.S3.readValue())/2;
  TextLCD.print("Silver");
  Button.PRGM.waitForPressAndRelease();
  final int colourSilver = (Sensor.S1.readValue() + Sensor.S3.readValue())/2;
  TextLCD.print("BLACK");
  Button.PRGM.waitForPressAndRelease();
  final int colourBlack = (Sensor.S1.readValue() + Sensor.S3.readValue())/2;
  Button.RUN.waitForPressAndRelease();

  // Start The Motors
  Motor.A.setPower(7);
  Motor.B.setPower(7);
  Motor.A.forward();
  Motor.B.forward();



  Sensor.S1.addSensorListener(new SensorListener()
  {
   public void stateChanged(Sensor src, int oldValue, int newValue)
   {
    //LCD.showNumber (newValue);
    try
    {
     if (colourGreen - 1 <= newValue && colourGreen + 1 >= newValue)
     {
     }
     else if (colourWhite - 1 <= newValue && colourGreen + 1 >= newValue)
     {
     }
     else if (colourSilver - 1 <= newValue && colourSilver + 1 >= newValue)
     {
     }
     else if (colourBlack - 1 <= newValue && colourBlack + 1 >= newValue)
     {
     }
    }
    catch (InterruptedException e)
    {
     //nothing
    }
   }
  });
    
    
 }
 private void selfDestruct() 
 {
    
 }
}