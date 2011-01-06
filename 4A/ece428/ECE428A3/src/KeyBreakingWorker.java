import java.util.concurrent.Callable;

/**
 * Worker class that uses the KeyGenerator to brute force encrypted data.
 * 
 * @author shdwfeather
 *
 */
public class KeyBreakingWorker implements Callable<byte[]> {
    
    public KeyGenerator gen;
    public Decrypter enc = new Decrypter();
    public byte[] data;
    
    public KeyBreakingWorker(int zeroes, int[] start, int[] end, byte[] data) {
        gen = new KeyGenerator(zeroes, start, end);
        this.data = data;
    }
    
    public KeyBreakingWorker(int zeroes, byte[] data) {
        gen = new KeyGenerator(zeroes);
        this.data = data;
    }


    public byte[] call() throws Exception {
    	int interruptCount = 0; 
        for (byte[] key = gen.getNext(); key != null; key = gen.getNext()) {
        	if(++interruptCount % 1000 == 0) {
        		Thread.sleep(0);
        	}
            enc.setKey(key);
            byte[] decrypted = enc.decrypt(data);
            if (decrypted != null) {
                return key;
            }
        }
        
        return null;
    }
    
}
