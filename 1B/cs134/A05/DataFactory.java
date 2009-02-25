public class DataFactory
{
   public static StackInterface makeStack()
   {  // If you want to use the array-based implementation, change the line below
 return new StackLinked();
   }
}
