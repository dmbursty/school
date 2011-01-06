import java.io.IOException;
import java.io.ObjectInputStream;


public class KeyReader {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(System.in);
		while(true) {
			byte[] key = (byte[])ois.readObject();
			for (int i = 0; i < 16; i++) {
				System.out.print(key[i] + " ");
			}
			System.out.println("");
		}
	}
	                                        
}
