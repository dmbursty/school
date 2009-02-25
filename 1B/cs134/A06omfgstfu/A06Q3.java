public class A06Q3
{    
  public static void main(String[] args)
  {
    System.out.println(makePalindrome("abc"));
  }
    
    
    // Question 3(a)
    public static int gcd(int a, int b)
    // pre: a,b >= 0, a + b > 0
    // post: returns greatest common divisor of a and b.
    {
        if (a == 0 || b == 0)
        {
          return a + b;
        }
        else if (a == 1 || b == 1)
        {
          return 1;
        }
        else if (a == b)
        {
          return a;
        }
        else if (a < b)
        {
          return gcd(b,a%b);
        }
        else if (b < a)
        {
          return gcd(a,b%a);
        }
        return 0;
    }
 
    // Question 3(b)
    public static String makePalindrome(String s)
    // pre: s is not null
    // post: creates and returns the String s concatenated with
    // the reverse of s
    {
        if (s.equals(""))
        {
          return "";
        }
        return s.substring(0,1) + makePalindrome(s.substring(1,s.length())) + s.substring(0,1);
    }
  
    // Question 3(c)
    public static void makeAllPalindromes(SubListInterface s)
    // pre: s!= null, Entries in s are of type String and are not null
    // post: modifies s so that each entry in s is replaced with a
    // palindrome created from the entry
    {
      if (s.getRemainder() != null)
      {
        s.setRootItem(makePalindrome((String)s.getRootItem()));
        makeAllPalindromes(s.getRemainder());
      }
    }

    // Question 3(d)
    public static void replaceItems(SubListInterface s, Object target, Object changeTo)
    // pre: s != null, target != null, changeTo != null
    // post: modifies s so that all entries equivalent (using .equals)
    // to target are replaced with the value changeTo
    {
      if (s.getRemainder() != null)
      {
        if (s.getRootItem().equals(target))
        {
          s.setRootItem(changeTo);
        }
        replaceItems(s.getRemainder(), target, changeTo);
      }
    }
  
    //Question 3(e)
    public static int[] extremes(SubListInterface s)
    // pre: s!=null, !s.isEmpty(),
    //      all values in s are non-null and of type Integer
    // post: returns an array result of length 2, where
    // result[0] is the minimum int value in s, and
    // result[1] is the maximum int value in s.
    {
        int[] ret = new int[2];
        if (s.getRemainder() == null)
        {
          ret[0] = ((Integer)s.getRootItem()).intValue();
          ret[1] = ((Integer)s.getRootItem()).intValue();
          return ret;
        }
        ret[0] = Math.min(((Integer)s.getRootItem()).intValue(),extremes(s.getRemainder())[0]);
        ret[1] = Math.max(((Integer)s.getRootItem()).intValue(),extremes(s.getRemainder())[1]);
        return ret;
    }
}
