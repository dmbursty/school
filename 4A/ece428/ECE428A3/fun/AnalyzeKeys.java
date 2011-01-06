import java.io.ObjectInputStream;

public class AnalyzeKeys {

	public static void main(String[] args) throws Exception {

	}

	public static void analyze() throws Exception {
		ObjectInputStream oIn = new ObjectInputStream(System.in);

		try {
			while (true) {
				byte[] key = (byte[]) oIn.readObject();
			}
		} finally {

		}
	}
}
