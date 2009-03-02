public class UniformDistribution implements Distribution {
  public int getItem(int M) {
    return (int) Math.ceil(Math.random() * M);
  }
}
