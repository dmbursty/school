public class Iint
{
  public int in;
  
  public Iint (int in)
  {
    this.in = in;
  }
  
  public void dup1 (Iint other)
  {
    other = this;
  }
}