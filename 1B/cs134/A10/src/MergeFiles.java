import java.util.*;
import java.io.*;

public class MergeFiles
{
   public static void main(String[] args)
   {
      // Exit program if there are not three arguments
      if (args.length != 3)
      {
        System.out.println("Proper usage is java MergeFiles inFile1 inFile2 outFile");
        System.exit(0);
      }
      
      // Declare Variables
      FileReader in1,in2;
      FileWriter out;
      Scanner sc1,sc2;
      
      // Open files
      try {
        in1 = new FileReader(args[0]);
        in2 = new FileReader(args[1]);
      }
      catch (FileNotFoundException e)
      {
        e.printStackTrace();
        return;
      }
      
      // Create Scanners
      sc1 = new Scanner(in1);
      sc2 = new Scanner(in2);
      
      try {
        // Create a filewriter for the output file
        out = new FileWriter(args[2]);
        PrintWriter write = new PrintWriter(out);
        
        // Declare two variables for the most recent line in each input file
        String line1;
        String line2;
        
        // If the file is non-empty, set the variable to the first line for each file
        if (sc1.hasNext()) line1 = sc1.nextLine();
        else line1 = null;
        
        if (sc2.hasNext()) line2 = sc2.nextLine();
        else line2 = null;
        
        // Continue until break
        while (true)
        {
            // If we've gone through all of file 1
            if (line1 == null)
            {
              // Write from file 2 and increment
              write.println(line2);
              if (sc2.hasNext()) line2 = sc2.nextLine();
              else line2 = null;
            }
            // If we've gone through all of file 2
            else if (line2 == null)
            {
              // Write from file 1 and increment
              write.println(line1);
              if (sc1.hasNext()) line1 = sc1.nextLine();
              else line1 = null;
            }
            // Otherwise neither file has ended
            else
            {
              // If line1 comes first lexicographically, or the strings are the same
              if (line1.compareTo(line2) <= 0)
              {
                // Write from file 1 and increment
                write.println(line1);
                if (sc1.hasNext()) line1 = sc1.nextLine();
                else line1 = null;
              }
              // Otherwise
              else
              {
                // Write from file 2 and increment
                write.println(line2);
                if (sc2.hasNext()) line2 = sc2.nextLine();
                else line2 = null;
              }
            }
            // Break when both files have been fully traversed
            if (line1 == null && line2 == null) break;
        }
        
        // Close the output stream
        out.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      
   }
}
