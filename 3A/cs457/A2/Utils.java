public class Utils {
  // Probability distributions
  // Uniform distribution
  public static int uniform(int M) {
    return (int)Math.ceil(Math.random() * M);
  }
  
  // Zipf distribution
  public static int zipf(int M) {
    double c = 0;
    for(int k = 1; k <= M; k++) {
      c += 1.0/k;
    }
    c = 1.0/c;
    double x = Math.random();
    for(int m = 1; m <= M; m++) {
      x -= c/m;
      if (x <= 0) {
        return m;
      }
    }
    return -1;
  }
  
  // Exponential distribution
  public static double exponential(double lambda) {
    return (-1 / lambda) * Math.log(1 - Math.random());
  }
}
