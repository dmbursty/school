
/**
 * Generates all possible keys within a range.
 * Start and end are both inclusive.
 * 
 * @author shdwfeather
 *
 */
public class KeyGenerator {

    private byte[][] masks; // 128 keys with a 1 at each position
    private int[] a;        // positions of 1s
    private int[] start;    // starting point for "a"
    private int[] end;      // ending point for "a"

    private byte[] res;     // the key strings, but with 1s instead of 0s
    private byte[] notres;  // ~res, ready to be returned
    private int n = 128;
    private int r;          // number of 1s in res / 0s in notres
    private boolean first = true;

    
    /***
     * Generate ALL keys with r 0s.
     * 
     * @param r number of 0s
     */
    public KeyGenerator(int r) {
        this.r = r;
        
        start = new int[r];
        for (int i = 0; i < r; i++) {
            start[i] = i;
        }
        
        end = new int[r];
        for (int i = 0; i < r; i++) {
            end[i] = 128-r+i;
        }
        
        init();
    }
    
    /**
     * Generates keys that have "r" 0s, starting with positions in "start" 
     * and ending in position "end".
     * 
     * @param r number of 0s
     * @param start starting positions of 0s
     * @param end ending positions of 0s
     */
    public KeyGenerator(int r, int[] start, int[] end) {
        assert (start.length == end.length && end.length == r);
        
        this.start = start;
        this.end = end;
        this.r = r;
        init();
    }
    
    private void init () {
        a = new int[r];
        res = new byte[16];
        notres = new byte[16];
        masks = new byte[128][16];

        for (int i = 0; i < 128; i++) {
            masks[i][i / 8] |= 1 << (7 - i % 8);
        }

        reset();        
    }

    public int[] getStart() {
        return start;
    }

    public void setStart(int[] start) {
        this.start = start;
    }

    public int[] getEnd() {
        return end;
    }

    public void setEnd(int[] end) {
        this.end = end;
    }

    /**
     * Resets the generator; use if you want to reuse the class instance.
     */
    public void reset() {
        first = true;
        for (int k = 0; k < 16; k++) {
            res[k] = 0;
        }

        for (int i = 0; i < a.length; i++) {
            a[i] = start[i];
            for (int k = 0; k < 16; k++) {
                res[k] |= masks[a[i]][k];
            }
        }
        
        for (int j = 0; j < 16; j++) {
            notres[j] = (byte) ~res[j];
        }
    }

    /**
     * Generates all possible keys; if there are no more, this will return null.
     */
    public byte[] getNext() {
        if (first) {
            first = false;
            return notres;
        }
        
        // check if we're at ending
        boolean sameAsEnd = true;
        for (int j = 0; j < r; j++) {
            if (a[j] != end[j]) {
                sameAsEnd = false;
                break;
            }
        }
        
        if (sameAsEnd) {
            return null;
        }                

        int i = r - 1;
        while (i > -1 && a[i] == n - r + i) {
            i--;
        }

        if (i == -1) {
            return null;
        }

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
        
        for (int j = 0; j < 16; j++) {
            notres[j] = (byte) ~res[j];
        }

        return notres;
    }

    public static void main(String[] args) {
        /*
        int[] start = {0, 1, 2};
        int[] end = {0, 1, 10};
        KeyGenerator gen = new KeyGenerator(3, start, end);

        byte[] key = gen.getNext();
        while (key != null) {
            
            for (int j = 0; j < 16; j++) { System.out.printf("%02x", key[j]); }
            System.out.println();
            
            key = gen.getNext();
        }
        
        gen.setStart(new int[] {0, 1, 10});
        gen.setEnd(new int[] {0, 2, 3});
        gen.reset();
        key = gen.getNext();
        while (key != null) {
            
            for (int j = 0; j < 16; j++) { System.out.printf("%02x", key[j]); }
            System.out.println();
            
            key = gen.getNext();
        }
        */
        
        KeyGenerator gen = new KeyGenerator(2);
        byte[] key = gen.getNext();
        while (key != null) {
            
            for (int j = 0; j < 16; j++) { System.out.printf("%02x", key[j]); }
            System.out.println();
            
            key = gen.getNext();
        }

    }
}
