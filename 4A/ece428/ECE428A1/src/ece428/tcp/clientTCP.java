package ece428.tcp;

/**
 * Starts the TCP client
 */
public class clientTCP {
	public static void main(String[] args) throws Exception {
		new TCPSoccerClient(args[0], args[1]).run();
	}
}
