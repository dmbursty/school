public class ZipfDistribution implements Distribution {

  public int getItem(int M) {
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

}
