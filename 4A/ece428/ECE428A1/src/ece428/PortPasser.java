package ece428;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Allows a port to be passes from a server to a client. This implementation
 * uses a file placed at an agreed upon location.
 */
public class PortPasser {

	/**
	 * Saves a port to file. The file is deleted on exit.
	 */
	public static void savePort(int port) throws IOException, InterruptedException {
		PrintStream output = new PrintStream(new FileOutputStream(portFile));
		try {
			output.println(port);
		} finally {
			output.close();
			portFile.deleteOnExit();
		}
	}

	/**
	 * Waits for the server to save which port it is using, then returns that
	 * port.
	 */
	public static int retrievePort() throws NumberFormatException, IOException,
			InterruptedException {
		while (!portFile.canRead()) {
			Thread.sleep(100);
		}
		Thread.sleep(100);

		BufferedReader input = new BufferedReader(new FileReader(portFile));
		try {
			return Integer.parseInt(input.readLine());
		} finally {
			input.close();
		}
	}

	protected static final File portFile = new File("port.named_to_reduce_colisions.txt");
}
