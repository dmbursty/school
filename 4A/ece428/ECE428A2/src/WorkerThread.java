import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import ece428.JmyState;
import ece428.Packet;
import ece428.socket.T_DatagramSocket;

public class WorkerThread extends Thread {

    protected LinkedBlockingQueue<Byte> toSendByteQueue;
    protected LinkedBlockingQueue<Byte> receivedByteQueue;

    protected int sequence = 1;
    protected int ackNum = -1;
    protected T_DatagramSocket socket;
    protected InetSocketAddress otherAddress;
    protected JmyState state = JmyState.CLOSED;

    public static final int SO_TIMEOUT = 10;
    protected static final int RECEIVE_BUFFER_SIZE = Short.MAX_VALUE;
    protected static final int SEND_BUFFER_SIZE = RECEIVE_BUFFER_SIZE - Packet.HEADER_LENGTH;
    protected static final int QUEUE_MAX_SIZE = RECEIVE_BUFFER_SIZE * 5;
    protected static final int SYN = 1;
    protected static final int ACK = 1 << 1;
    protected static final int FIN = 1 << 2;

    protected static final int CLOSE_TIMEOUT = 1000;
    protected static final int CLOSE_TIMEOUT_COUNT = CLOSE_TIMEOUT / SO_TIMEOUT;

    protected volatile boolean shouldClose = false;
    protected int numberOfConsecutiveTimeouts = 0;

    private static Logger log = Logger.getLogger(WorkerThread.class.getName());

    public WorkerThread(T_DatagramSocket socket) throws SocketException {
        this.socket = socket;

        this.toSendByteQueue = new LinkedBlockingQueue<Byte>();
        this.receivedByteQueue = new LinkedBlockingQueue<Byte>();
    }

    /* Used to send data. len can be arbitrarily large or small */
    public void send(byte[] buf, int len) {
        if (!isAlive()) {
            throw new RuntimeException("Connection closed, cannot send more bytes");
        }

        // get the data and add it to the received queue
        List<Byte> dataList = new ArrayList<Byte>(Math.min(len, buf.length));
        for (int i = 0; i < Math.min(len, buf.length); i++) {
            dataList.add(buf[i]);
        }
        toSendByteQueue.addAll(dataList);
    }

    /*
     * Used to receive data. Max chunk of data received is len. The actual number of bytes received
     * is returned
     */
    public int receive(byte[] buf, int len, int timeout) throws InterruptedException {
        if (!isAlive() && receivedByteQueue.isEmpty()) {
            throw new RuntimeException("Connection closed, no more bytes to get");
        }

        int i;
        for (i = 0; i < len; i++) {
            Byte polledByte;
            if (i == 0) {
                polledByte = receivedByteQueue.poll(timeout, TimeUnit.MILLISECONDS);
            } else {
                polledByte = receivedByteQueue.poll();
            }

            if (polledByte == null) {
                break;
            }
            buf[i] = polledByte;
        }

        return i;
    }

    /* Used by client to connect to server */
    public void connect(InetSocketAddress serverAddr) throws IOException {
        otherAddress = serverAddr;

        Packet packet = null;
        while (packet == null) {
            // Send syn
            sendControlMsg(SYN, serverAddr, 0);
            setState(JmyState.SYN_SENT);
            // Wait for synack
            packet = doReceive();

            // Check that packet is not null, that it's not a synack, or it's
            // sequence is wrong
            if (packet != null
                    && (!(packet.isSyn() && packet.isAck()) || packet.getAckNumber() != sequence + 1)) {
                log.warning("Unexpected packet (expected synack)");
                packet = null;
            }
        }
        sequence++;

        log.info("Sending ack");
        ackNum = packet.getSequenceNumber() + 1;
        sendControlMsg(ACK, serverAddr, ackNum);
        sequence++;

        // Connection Established!
        log.info("Client: Connection established");

        setState(JmyState.IDLE);
    }

    /* Used by server to accept a new connection */
    /* Returns the IP & port of the client */
    public InetSocketAddress accept() throws IOException {
        setState(JmyState.LISTENING);

        Packet packet = null;
        while (packet == null) {
            // Wait for syn
            packet = doReceive();

            if (packet != null && !packet.isSyn()) {
                log.warning("Unexpected packet while accepting");
                packet = null;
            }
        }

        ackNum = packet.getSequenceNumber() + 1;

        packet = null;
        while (packet == null) {
            // Send synack
            sendControlMsg(SYN | ACK, otherAddress, ackNum);

            setState(JmyState.SYN_RECEIVED);

            // Wait for ack
            packet = doReceive();

            if (packet != null) {
                if (packet.isFin()) {
                    ackNum = packet.getSequenceNumber() + 1;
                    doFinReceived();
                    return otherAddress;
                } else if (!packet.isAck() || packet.getAckNumber() != sequence + 1) {
                    packet = null;
                }
            }
        }
        sequence++;
        ackNum = packet.getSequenceNumber() + 1;

        setState(JmyState.IDLE);

        // Connection Established!
        log.info("Server: Connection established");

        return otherAddress;
    }

    /**
     * Sets a bit which indicates that this worker thread should perform an active close as soon as
     * it can
     */
    public void setShouldClose() {
        shouldClose = true;
    }

    protected void sendControlMsg(int flags, InetSocketAddress server, int ackNum, int seqNum)
            throws IOException {
        int tmp = sequence;
        sequence = seqNum;
        sendControlMsg(flags, server, ackNum);
        sequence = tmp;
    }

    /**
     * Sends a packet with no data, and the provided flags set
     */
    protected void sendControlMsg(int flags, InetSocketAddress server, int ackNum)
            throws IOException {
        Packet packet = new Packet();
        packet.setSequenceNumber(sequence);
        packet.setAcknowledgementNumber(ackNum);

        if ((flags & SYN) == SYN) {
            packet.setSyn(true);
        } else {
            packet.setSyn(false);
        }

        if ((flags & ACK) == ACK) {
            packet.setAck(true);
        } else {
            packet.setAck(false);
        }

        if ((flags & FIN) == FIN) {
            packet.setFin(true);
        } else {
            packet.setFin(false);
        }

        packet.setChecksum();
        log.fine("Sending control message: " + packet.toString());

        byte[] contents = packet.getContents();
        socket.T_sendto(contents, contents.length, server);
    }
    
    protected void sendControlMsg1(int flags, InetSocketAddress server, int ackNum)
            throws IOException {
        Packet packet = new Packet();
        packet.setSequenceNumber(sequence);
        packet.setAcknowledgementNumber(ackNum);

        if ((flags & SYN) == SYN) {
            packet.setSyn(true);
        } else {
            packet.setSyn(false);
        }

        if ((flags & ACK) == ACK) {
            packet.setAck(true);
        } else {
            packet.setAck(false);
        }

        if ((flags & FIN) == FIN) {
            packet.setFin(true);
        } else {
            packet.setFin(false);
        }

        packet.setChecksum();

        byte[] contents = packet.getContents();
        socket.T_sendto(contents, contents.length, server);
    } 

    /*
     * Returns a packet from the socket Returns null on timeout
     */
    protected Packet doReceive() throws IOException {
        try {
            DatagramPacket dpacket = socket.T_recvfrom(RECEIVE_BUFFER_SIZE);
            Packet packet = new Packet(dpacket.getData(), 0, dpacket.getLength(), true);
            numberOfConsecutiveTimeouts = 0;

            // Check the checksum
            if (packet.isGood()) {
                log.fine("Good packet: " + packet.toString());
            } else {
                log.fine("Corruption! " + packet.toString());
                return null;
            }

            otherAddress = (InetSocketAddress) dpacket.getSocketAddress();
            return packet;
        } catch (SocketTimeoutException e) {
            // Timeout!
            return null;
        }
    }

    /**
     * Precondition: must be in established state. <br />
     * Sends a data packet filled with data available on queue to the other connected machine.
     * 
     * @throws IOException
     */
    protected void doSend() throws IOException {
        assert (state == JmyState.IDLE);
        setState(JmyState.DATA_SENT);

        // construct data packet from toSendByteQueue
        List<Byte> list = new ArrayList<Byte>(SEND_BUFFER_SIZE);
        int length = toSendByteQueue.drainTo(list, SEND_BUFFER_SIZE);

        byte[] buffer = new byte[length];
        copyBytes(list, buffer);

        Packet sendPacket = new Packet(buffer, 0, buffer.length, false);
        sendPacket.setSequenceNumber(sequence);
        sendPacket.setAcknowledgementNumber(ackNum);
        sendPacket.setChecksum();
        sequence += buffer.length;

        log.info("Sending data packet: " + sendPacket.toString());
        // keep trying to send the data packet until it's ack'd
        while (true) {
            // send data packet
            socket
                    .T_sendto(sendPacket.getContents(), sendPacket.getContents().length,
                            otherAddress);

            // wait for ack
            Packet recvPacket = doReceive();
            doEstablishedHandling(recvPacket);
            if (state == JmyState.CLOSED) {
                return;
            }
            // if packet is correct ACK for the data, exit send state
            if (recvPacket != null && recvPacket.isAck() && recvPacket.getAckNumber() == sequence) {
                break;
            }
            log.fine("Timeout; resend data packet.");
        }

        log.info("Send successful.");
        setState(JmyState.IDLE);
    }

    protected void setState(JmyState value) {
        if (this.state != value) {
            log.fine("Changing state from " + state + " to " + value);
            this.state = value;
        }
    }

    /**
     * Does state transitions common to the established states; i.e. IDLE and DATA_SENT. Will check
     * and handle stale packets, fin packets, and lost ACK in 3-way handshake condition.
     * 
     * @param packet Packet that was received from the UDP layer.
     * @throws IOException
     */
    protected void doEstablishedHandling(Packet packet) throws IOException {
        assert (state == JmyState.IDLE || state == JmyState.DATA_SENT);

        if (packet != null) {
            // check packet type
            if (packet.isFin()) {
                ackNum = packet.getSequenceNumber() + 1;
                doFinReceived();
            } else if (packet.getSequenceNumber() < ackNum) {
                // Stale packet, just ack it with its expected sequence number
                if (packet.hasData()) {
                    sendControlMsg(ACK, otherAddress, packet.getSequenceNumber()
                            + packet.getData().length, packet.getAckNumber());
                } else {
                    sendControlMsg(ACK, otherAddress, packet.getSequenceNumber() + 1, packet
                            .getAckNumber());
                }
            } else if (packet.hasData()) {
                // get the data and add it to the received queue
                byte[] data = packet.getData();
                List<Byte> dataList = new ArrayList<Byte>(data.length);
                for (int i = 0; i < data.length; i++) {
                    dataList.add(data[i]);
                }
                receivedByteQueue.addAll(dataList);

                // send ACK for data packet
                ackNum = packet.getSequenceNumber() + data.length;
                sendControlMsg(ACK, otherAddress, ackNum);
                sequence++;
            } else {
                // log.warning("Unexpected packet; dropping this: \n" +
                // packet.toString());
            }
        } else {
            // we have not heard from the other for numberOfConsecutiveTimeouts
            // * SO_TIMEOUT miliseconds, close the connection.
            numberOfConsecutiveTimeouts++;
            if (numberOfConsecutiveTimeouts >= CLOSE_TIMEOUT_COUNT) {
                log.warning("Havent heard from the other guy in " + numberOfConsecutiveTimeouts
                        + ", closing connection");
                setState(JmyState.CLOSED);
            }
        }
    }

    @Override
    // precondition: accept or connect has been called
    public void run() {
        try {
            while (state != JmyState.CLOSED) {
                // idle state
                Packet packet = doReceive();
                doEstablishedHandling(packet);
                if (state == JmyState.CLOSED) {
                    break;
                }

                if (packet == null) {
                    // timeout, send keep-alive
//                    log.fine("Timeout in idle state, sending a timeout ack");
                    //sendControlMsg1(ACK, otherAddress, ackNum);
                }

                // send state
                if (!toSendByteQueue.isEmpty()) {
                    doSend();
                }

                if (shouldClose && state != JmyState.CLOSED && toSendByteQueue.isEmpty()) {
                    doActiveClose();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // receive with timeout with 1000
        // parse received, make sure packet is good
        // * right seq num
        // * if seq num < expecting seq num, re-ack
        // put good packet on receive queue
        // check send
        // if stuff to send
        // * send data, wait for ack time out, send again etc.

    }

    /**
     * Deals with the functionality moving to and in the fin-set state
     */
    protected void doActiveClose() throws IOException {
        assert shouldClose;

        sendControlMsg(FIN, otherAddress, ackNum); // propper ack number?

        setState(JmyState.FIN_SENT);

        Packet packet = null;
        int numberOfTimeouts = 0;
        while (numberOfTimeouts < CLOSE_TIMEOUT_COUNT) {
            packet = doReceive();

            if (packet == null) {
                numberOfTimeouts++;
                log.fine("" + state + " timeout number " + numberOfTimeouts + ", resending fin");
                sendControlMsg(FIN, otherAddress, ackNum);
            } else if (packet.isFin() || isValidAck(packet)) {
                ackNum = packet.getSequenceNumber() + 1;
                sequence++;
                break;
            }
        }

        if (numberOfTimeouts < CLOSE_TIMEOUT_COUNT && packet.isFin()) {
            sendControlMsg(ACK, otherAddress, ackNum);
        } else {
            log.info("" + state + " timed out, closing");
        }

        setState(JmyState.CLOSED);
    }

    /**
     * Deals with the functionality moving to and in the fin-received state
     */
    protected void doFinReceived() throws IOException {
        sendControlMsg(FIN | ACK, otherAddress, ackNum);
        sequence++;

        setState(JmyState.FIN_RECEIVED);

        Packet packet = null;
        int numberOfTimeouts = 0;
        while (numberOfTimeouts < CLOSE_TIMEOUT_COUNT) {
            packet = doReceive();

            if (packet == null) {
                numberOfTimeouts++;
                log.fine("" + state + " timeout number " + numberOfTimeouts + ", resending fin");
            } else if (packet.isFin()) {
                log.fine("Got another fin, replying with fin ack");
                sendControlMsg(FIN | ACK, otherAddress, ackNum, packet.getAckNumber());
            } else if (isValidAck(packet)) {
                // Unexpected stale packet (drop)
                log.warning("Got stale packet when expecting ack (closing)");
            } else if (packet.isAck()) {
                break;
            }
        }

        if (numberOfTimeouts >= CLOSE_TIMEOUT_COUNT) {
            log.info("" + state + " timed out, closing");
        }

        setState(JmyState.CLOSED);
    }

    /**
     * Copies the contents of src into dst, space permitted
     */
    protected void copyBytes(List<Byte> src, byte[] dst) {
        for (int i = 0; i < Math.min(src.size(), dst.length); i++) {
            dst[i] = src.get(i);
        }
    }

    /**
     * @return true if the packet is an ack and not outdated
     */
    protected boolean isValidAck(Packet packet) {
        return packet != null && packet.isAck() && packet.getSequenceNumber() < ackNum;
    }

}
