import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * UDP Server
 */
public class p3server {
	// Key change message header
	public final static String KEY_CHANGE = "   KEY CHANGE   ";
	
	private byte[] currKey = null;

	// Socket used
	private DatagramSocket socket;
	
	public static final int BUFFER_LENGTH = 65536; // max packet size

	// Constructor for p3server
	public p3server() throws IOException, InterruptedException {
		socket = new DatagramSocket(); // any port
	}

	// Receives a single packet from the client
	public DatagramPacket receive() throws IOException {
		byte[] buffer = new byte[BUFFER_LENGTH];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		socket.receive(packet);

		return packet;
	}

	// Closes socket
	public void close() {
		socket.close();
	}

	// Retrieves local port from the socket being used
	public int getLocalPort() {
		return socket.getLocalPort();
	}

	// Extracts byte array from datagram packet
	public static byte[] getBytes(DatagramPacket packet) {
		return Arrays.copyOfRange(packet.getData(), packet.getOffset(), packet
				.getOffset()
				+ packet.getLength());
	}

	// Makes server, starts client
	public static void main(String[] args) throws Exception {
		// Make server
		p3server server = new p3server();
		int port = server.getLocalPort();

		Runtime run = Runtime.getRuntime();
		Process proc = run.exec("/home/tripunit/p3client -s " + port + " -n " + args[1] + " -f " + args[0]);
		InputStreamReader isr = new InputStreamReader(proc.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		System.out.println("Server:");
		System.out.println("Port is: " + port);
		System.out.println("F is: " + args[0]);
		System.out.println("N is: " + args[1]);

		System.out.println("Client:");
		System.out.println(br.readLine());
		System.out.println(br.readLine());
		

		// Get number of zeros from arguments
		int n = Integer.parseInt(args[1]);

		server.go(n);
	}
	
	// Business logic of the server
	public void go(int numZeros) throws Exception {
		// Decrypts packets
		final Decrypter enc = new Decrypter();
		// Output streams
		final FileOutputStream outFile = new FileOutputStream(new File(
				"out.dat"));
		final OutputStreamWriter out = new OutputStreamWriter(outFile);

		try {
			while (true) {
				// Get a packet
				DatagramPacket packet = receive();
				final byte[] data = getBytes(packet);
				System.out.print("Packet received: ");
				if (Arrays.equals(KEY_CHANGE.getBytes(), data)) {
					System.out.println("Key change");
					// clear the saved key
				    currKey = null;
				    
				} else if(currKey == null) {
					System.out.print("Brute forcing ");
                    // Brute forces cipher text with every possible key
					currKey = Administrator.decrypt(numZeros, data);
				}

				if(currKey != null){
					System.out.println("Key known");
                    enc.setKey(currKey);
                    byte[] decrypted = enc.decrypt(data);
                    if (decrypted == null) {
                        throw new RuntimeException("Key changed?!");
                    }
                    out.write(new String(decrypted));
					out.write("\n ============= \n");
                    out.flush();
                    
				}

            }
		} finally {
			// Close the socket
			close();
		}	
	}
}
// vi: ts=4 sw=4 sts=0 noet:
