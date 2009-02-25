public class MagicSquare
{
    public static boolean isMagic( int[][] a )
    {
        // Check that the 2D array is not null.
        if (a == null)
        {
          return false;
        }
        
        // Step 1: Is it a square?
        if (!isSquare(a)) {
          return false;
        }
        
        // Step 2: Are all the values positive?
        if (!allPositive(a)) {
          return false;
        }
        
        // Step 3: Compute the expected sum.
        int sum = expectedSum(a);
        
        // Step 4: Check the rows.
        if (!allRowsSame(a,sum))
        {
          return false;
        }
        
        // Step 5: Check the columns.
        if (!allColsSame(a,sum))
        {
          return false;
        }
        
        // Step 6: Check the diagonals
        // Your code here.
        
        // Step 7: Check that all values are unique.
        // Your code here.
        
        // For use initially as a stub.
        return true;
    }
    
    private static boolean isSquare(int[][] a)
    {
      // Get the size of the square
      int size = a.length;
      
      // Check all the rows to see if they are the same size
      for (int i = 0 ; i < size ; i++ )
      {
        if (a[i].length != size) {
          return false;
        }
      }
      return true;
    }
    
    private static boolean allPositive(int[][] a)
    {
      // Go through every row
      for (int i = 0 ; i < a.length ; i++)
      {
        // Go through every element in every row and check for negatives
        for (int j = 0 ; j < a[i].length ; j++ )
        {
          if (a[i][j] < 0) {
            return false;
          }
        }
      }
      return true;
    }
    
    private static int expectedSum(int[][] a)
    {
      int sum = 0;
      
      // Add up all the vaules in the first row of the square
      for (int i = 0; i < a[0].length ; i++)
      {
        sum += a[0][i];
      }
      return sum;
    }
    
    private static boolean allRowsSame(int[][] a, int checkSum)
    {
      int sum = 0;
      
      // Go through every row
      for (int i = 0 ; i < a.length ; i++)
      {
        // Find the sum of each row and check that it's the same as the expected sum
        for (int j = 0 ; j < a[i].length ; j++)
        {
          sum += a[i][j];
        }
        if (sum != checkSum)
        {
          return false;
        }
        sum = 0;
      }
      return true;
    }
    
    private static boolean allColsSame(int[][] a, int checkSum)
    {
      int sum = 0;
      
      // Go through every column
      for (int i = 0 ; i < a.length ; i++)
      {
        // Find the sum of each row and check that it's the same as the expected sum
        for (int j = 0 ; j < a.length ; j++)
        {
          sum += a[j][i];
        }
        if (sum != checkSum)
        {
          return false;
        }
        sum = 0;
      }
      return true;
    }
    
    public static void main( String[] args )
    {
        int[][] a;
        
        // Null array
        System.out.println( MagicSquare.isMagic(null) );
        
        // Not a square
        a = new int[2][3];
        a[0][0] =  1; a[0][1] =  2; a[0][2] = -3;
        a[0][0] = -1; a[0][1] = -2; a[0][2] =  3;
        System.out.println( MagicSquare.isMagic(a) );
        
        // Not all positive
        a = new int[3][3];
        a[0][0] = -8; a[0][1] = -1; a[0][2] = -6;
        a[1][0] = -3; a[1][1] = -5; a[1][2] = -7;
        a[2][0] = -4; a[2][1] = -9; a[2][2] = -2;
        System.out.println( MagicSquare.isMagic(a) );
        
        // Rows don't add up.
        a = new int[3][3];
        a[0][0] =  1; a[0][1] = 5; a[0][2] = 3;
        a[1][0] = 11; a[1][1] = 9; a[1][2] = 7;
        a[2][0] =  6; a[2][1] = 4; a[2][2] = 8;
        System.out.println( MagicSquare.isMagic(a) );
        
        // Columns don't add up.
        a = new int[3][3];
        a[0][0] = 1; a[0][1] = 11; a[0][2] = 6;
        a[1][0] = 5; a[1][1] =  9; a[1][2] = 4;
        a[2][0] = 3; a[2][1] =  7; a[2][2] = 8;
        System.out.println( MagicSquare.isMagic(a) );
        
        // Diagonals don't add up.
        a = new int[3][3];
        a[0][0] = 3; a[0][1] = 5; a[0][2] = 7;
        a[1][0] = 8; a[1][1] = 1; a[1][2] = 6;
        a[2][0] = 4; a[2][1] = 9; a[2][2] = 2;
        System.out.println( MagicSquare.isMagic(a) );
        
        // Values aren't unique.
        a = new int[3][3];
        a[0][0] = 2; a[0][1] = 2; a[0][2] = 2;
        a[1][0] = 2; a[1][1] = 2; a[1][2] = 2;
        a[2][0] = 2; a[2][1] = 2; a[2][2] = 2;
        System.out.println( MagicSquare.isMagic(a) );
        
        // Valid magic square
        a = new int[3][3];
        a[0][0] = 8; a[0][1] = 1; a[0][2] = 6;
        a[1][0] = 3; a[1][1] = 5; a[1][2] = 7;
        a[2][0] = 4; a[2][1] = 9; a[2][2] = 2;
        System.out.println( MagicSquare.isMagic(a) );
        
        // Valid magic square
        a = new int[5][5];
        a[0][0] = 17; a[0][1] = 24; a[0][2] =  1; a[0][3] =  8; a[0][4] = 15;
        a[1][0] = 23; a[1][1] =  5; a[1][2] =  7; a[1][3] = 14; a[1][4] = 16;
        a[2][0] =  4; a[2][1] =  6; a[2][2] = 13; a[2][3] = 20; a[2][4] = 22;
        a[3][0] = 10; a[3][1] = 12; a[3][2] = 19; a[3][3] = 21; a[3][4] =  3;
        a[4][0] = 11; a[4][1] = 18; a[4][2] = 25; a[4][3] =  2; a[4][4] =  9;
        System.out.println( MagicSquare.isMagic(a) );
    }
}
