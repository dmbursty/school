public class A5Q1 {

 public static boolean checkForNestingAndBalanceIn( String str ) {
     // Pre:  str is not null.
     // Post:  returns true if the brackets (), [] and {} appearing in str are properly
     //  balanced and nested.
     //   Ignores all other characters in str, which therefore do not affect whether
     //  str is balanced and nested.
   
   // Make a stack to store the brackets in order
   StackInterface stack = DataFactory.makeStack();
      
   for (int i = 0; i < str.length(); i++)
   {
     // If the character is an open bracket, add it to the stack
     if (str.charAt(i) == '(' || str.charAt(i) == '{' || str.charAt(i) == '[' )
     {
       stack.push(new Character(str.charAt(i)));
     }
     // If the character is a close round bracket
     else if (str.charAt(i) == ')' )
     {
       // First check that the stack not empty, if it is, then the bracket is unbalanced
       if (stack.isEmpty())
       {
         return false;
       }
       // Check that the top of the stack is the corresponding open bracket
       if (!stack.pop().equals(new Character('(')))
       {
         return false;
       }
     }
     // If the character is the close curly bracket
     else if (str.charAt(i) == '}' )
     {
       // First check that the stack not empty, if it is, then the bracket is unbalanced
       if (stack.isEmpty())
       {
         return false;
       }
       // Check that the top of the stack is the corresponding open bracket
       if (!stack.pop().equals(new Character('{')))
       {
         return false;
       }
     }
     // If the character is the close square bracket
     else if (str.charAt(i) == ']' )
     {
       // First check that the stack not empty, if it is, then the bracket is unbalanced
       if (stack.isEmpty())
       {
         return false;
       }
       // Check that the top of the stack is the corresponding open bracket
       if (!stack.pop().equals(new Character('[')))
       {
         return false;
       }
     }
     // For any other character simply continue checking the string
   }
   // If there were no errors found along the way, and the stack is empty, then the string is proper, so return true
   if (stack.isEmpty())
   {
     return true;
   }
   // If the stack is not empty, then there is an unbalanced open bracket
   else
   {
     return false;
   }
 }
}
