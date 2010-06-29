package ece428.tcp;

/**
 * Starts the TCP server
 */
public class serverTCP {
	public static void main(String[] args) throws Exception {
		new TCPSoccerServer().run();
	}
}
