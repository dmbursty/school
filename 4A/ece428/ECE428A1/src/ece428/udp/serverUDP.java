package ece428.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import ece428.Player;
import ece428.PortPasser;

/**
 * UDP Server
 * 
 * @author yubin.kim
 *
 */
public class serverUDP {

    private DatagramSocket socket;
    private int clientPort;

    public static final int BUFFER_LENGTH = 65536; // max packet size

    public serverUDP() throws IOException, InterruptedException {
        socket = new DatagramSocket(); // any port
        PortPasser.savePort(getLocalPort());
    }

    /**
     * Sends message to localhost at the clientPort that's filled in during
     * receiveMessage.
     * 
     * @param msg String message to send
     * @throws IOException
     */
    public void sendMessage(String msg) throws IOException {
        byte[] buffer = new byte[BUFFER_LENGTH];
        buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
                InetAddress.getLocalHost(), clientPort);
        socket.send(packet);
    }

    public DatagramPacket receive() throws IOException {
        byte[] buffer = new byte[BUFFER_LENGTH];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        socket.receive(packet);

        return packet;
    }

    // receives a string from client; it also stores the port of the client to
    // class variable
    public String recieveMessage() throws IOException {
        DatagramPacket packet = receive();
        clientPort = packet.getPort();
        String msg = new String(packet.getData(), 0, packet.getLength());
        return msg;
    }

    public void close() {
        socket.close();
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }

    public static void main(String[] args) throws InterruptedException,
            IOException {
        // Start the server
        serverUDP server = new serverUDP();

        List<Player> allPlayers = new ArrayList<Player>();
        String country = null;
        String msg;
        int players = -1;

        // Recieve all the players
        for (int i = 0; players < 0 || i < players; i++) {
            msg = server.recieveMessage();

            if (msg.startsWith("!!ERROR!!")) {
                server.close();
                System.exit(1);
            } else if (msg.startsWith("DONE ")) {
                String[] triple = msg.split(" ");
                players = Integer.parseInt(triple[1]);
                country = triple[2];
            } else {
                String[] pair = msg.split(" ");
                allPlayers.add(new Player(pair[0], pair[1]));
            }
        }

        assert (country != null);

        // send the select players to client
        int playerCount = 0;
        for (Player player : allPlayers) {
            if (player.getCountry().equals(country)) {
                server.sendMessage(player.getName());
                playerCount++;
            }
        }

        // Tell that we have sent all players, and send our country request
        server.sendMessage("DONE " + playerCount + " " + country);
        server.close();
    }
}

// vi: ts=4 sw=4 sts=0 noet:
