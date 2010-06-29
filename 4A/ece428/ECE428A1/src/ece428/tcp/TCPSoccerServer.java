package ece428.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ece428.Player;
import ece428.PortPasser;

/**
 * The TCP server as described in A1
 */
public class TCPSoccerServer {
	/**
	 * Main method of server
	 * <p>
	 * Receives a list of players and a country from the client, returns all
	 * players of the given country to the client
	 */
	@SuppressWarnings("unchecked")
	public void run() throws IOException, InterruptedException, ClassNotFoundException {
		ServerSocket serverSocket = new ServerSocket(0); // 0 means any port
		PortPasser.savePort(serverSocket.getLocalPort());

		try {
			Socket socket = serverSocket.accept();
			try {
				ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());

				String selectedCountry = (String) objIn.readObject();
				List<Player> allPlayers = (List<Player>) objIn.readObject();

				List<Player> selectedPlayers = new ArrayList<Player>();
				for (Player player : allPlayers) {
					if (player.getCountry().equals(selectedCountry)) {
						selectedPlayers.add(player);
					}
				}

				objOut.writeObject(selectedPlayers);

				objOut.close();
				objIn.close();
			} finally {
				socket.close();
			}
		} finally {
			serverSocket.close();
		}
	}
}
