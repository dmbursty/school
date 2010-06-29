package ece428.tcp;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import ece428.FileIO;
import ece428.Player;
import ece428.PortPasser;

/**
 * The TCP client as described in A1
 */
public class TCPSoccerClient {
	public TCPSoccerClient(String fileName, String selectedCountry) {
		this.inputFile = new File(fileName);
		this.selectedCountry = selectedCountry;
	}

	/**
	 * Main method of client
	 * <p>
	 * Reads players from file, sends players to server along with
	 * selectedCountry, saves server response to file
	 */
	public void run() throws IOException, NumberFormatException, InterruptedException,
			ClassNotFoundException {
		List<Player> allPlayers = FileIO.readPlayersFromFile(inputFile);

		int port = PortPasser.retrievePort();
		Socket socket = new Socket(InetAddress.getLocalHost(), port);
		try {
			ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());

			objOut.writeObject(selectedCountry);
			objOut.writeObject(allPlayers);

			@SuppressWarnings("unchecked")
			List<Player> selectedPlayers = (List<Player>) objIn.readObject();

			FileIO.writePlayersToFile(selectedCountry, selectedPlayers, outputFile);

			objOut.close();
			objIn.close();
		} finally {
			socket.close();
		}
	}

	protected final File outputFile = new File("outTCP.dat");

	protected File inputFile;
	protected String selectedCountry;
}
