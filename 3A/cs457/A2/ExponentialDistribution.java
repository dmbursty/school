public class ExponentialDistribution {
  static double getValue(double mean) {
    return (-1 * mean) * Math.log(1 - Math.random());
  }
}
