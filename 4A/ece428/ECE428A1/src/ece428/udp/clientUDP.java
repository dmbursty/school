package ece428.udp;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import ece428.FileIO;
import ece428.Player;
import ece428.PortPasser;

/**
 * UDP Client 
 * 
 * @author yubin.kim
 *
 */
public class clientUDP {

    protected DatagramSocket socket;

    protected int port;

    public clientUDP() throws InterruptedException, IOException,
            SocketException {
        port = PortPasser.retrievePort();
        socket = new DatagramSocket();
    }

    /**
     * Send message to localhost at server port as specified by class variable.
     * 
     * @param msg
     *            String message to send
     * @throws IOException
     */
    public void sendMessage(String msg) throws IOException {
        byte[] buffer = new byte[serverUDP.BUFFER_LENGTH];
        buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
                InetAddress.getLocalHost(), port);
        socket.send(packet);
    }

    public DatagramPacket receive() throws IOException {
        byte[] buffer = new byte[serverUDP.BUFFER_LENGTH];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        socket.receive(packet);

        return packet;
    }

    /**
     * Wait to receive a message in UDP.
     * 
     * @return String extracted from the data of the recieved datagram.
     * @throws IOException
     */
    public String recieveMessage() throws IOException {
        DatagramPacket packet = receive();
        String msg = new String(packet.getData(), 0, packet.getLength());
        return msg;
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }

    public void close() {
        socket.close();
    }

    public static void main(String[] args) throws InterruptedException,
            SocketException, IOException {
        
        // Start UDP client
        clientUDP client = new clientUDP();
        
        // make sure we have 2 args
        if (args.length != 2) {
            System.out.println("Expected input file and country arguments");
            client.sendMessage("!!ERROR!!");
            client.close();
            System.exit(1);
        }

        File inputFile = new File(args[0]);
        String country = args[1];
        int playerCount = 0;
        
        // make sure input file is valid
        if (!inputFile.canRead()) {
            client.sendMessage("!!ERROR!!");
            client.close();
            System.exit(1);
        }      

        // read players from list and send to server
        List<Player> inputPlayers = FileIO.readPlayersFromFile(inputFile);

        playerCount = inputPlayers.size();
        for (Player player : inputPlayers) {
            client.sendMessage(player.getName() + " " + player.getCountry());
        }

        // Tell that we have sent all players, and send our country request
        client.sendMessage("DONE " + playerCount + " " + country);

        // Receive selected players from server
        List<Player> selectedPlayers = new ArrayList<Player>();
        String msg;
        int players = -1;

        for (int i = 0; players < 0 || i < players; i++) {
            msg = client.recieveMessage();

            if (msg.startsWith("DONE ")) {
                String[] triple = msg.split(" ");
                players = Integer.parseInt(triple[1]);
                assert (country.equals(triple[2]));
            } else {
                selectedPlayers.add(new Player(msg, country));
            }
        }

        // write received players to file
        File outputFile = new File("outUDP.dat");
        FileIO.writePlayersToFile(country, selectedPlayers, outputFile);
        client.close();
    }
}

// vi: ts=4 sw=4 sts=0 noet:
