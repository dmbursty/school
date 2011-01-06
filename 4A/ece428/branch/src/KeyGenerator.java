
public class KeyGenerator {

  private byte[][] masks;
  private int[] a;
  private byte[] res;
  private int n = 128;
  private int r;
  private boolean first = true;

  /**
   * @param r The number of 1's in the bit string. NOT ZEROS.
   */
  public KeyGenerator (int r) {
    this.r = r;
    a = new int[r];
    res = new byte[16];
    masks = new byte[128][16];

    for (int i = 0; i < 128; i++) {
      masks[i][i/8] |= 1 << (7-i%8);
    }

    reset ();
  }

  /**
   * Resets the generator; use if you want to reuse the class instance. 
   */
  public void reset () {
		first = true;
		for (int k = 0; k < 16; k++) {
			res[k] = 0;
		}

    for (int i = 0; i < a.length; i++) {
      a[i] = i;
      for (int k = 0; k < 16; k++) {
        res[k] ^= masks[a[i]][k];
      }
    }
  }

  /**
   * Generates all possible keys; if there are no more, this will return null.
   */
  public byte[] getNext () {
    if (first) {
      first = false;
      return res;
    }

    int i = r - 1;
    while (i > -1 && a[i] == n - r + i) {
      i--;
    }

    if (i == -1) return null;
    
    for (int k = 0; k < 16; k++) {
      res[k] ^= masks[a[i]][k];
    }
    a[i] = a[i] + 1;
    for (int k = 0; k < 16; k++) {
      res[k] ^= masks[a[i]][k];
    }

    for (int j = i + 1; j < r; j++) {
      for (int k = 0; k < 16; k++) {
        res[k] ^= masks[a[j]][k];
      }
      a[j] = a[i] + j - i;
      for (int k = 0; k < 16; k++) {
        res[k] ^= masks[a[j]][k];
      }
    }

    return res;
  }

  public static void main(String[] args) {
    KeyGenerator gen = new KeyGenerator(Integer.parseInt(args[0]));

    byte[] key = gen.getNext();
    while (key != null) {
			/*
      for (int j = 0; j < 16; j++) {
        System.out.printf("%02x", key[j]);
      }
      System.out.println();
			*/
      key = gen.getNext();
    }

  }
}


