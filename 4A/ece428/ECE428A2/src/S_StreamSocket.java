
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import ece428.socket.T_DatagramSocket;

class S_StreamSocket {
    /* Data members */
    protected int recieveTimeout = 1000;
    protected WorkerThread worker = null;
    protected T_DatagramSocket socket;

    private static Logger log = Logger.getLogger(S_StreamSocket.class.getName());

    /* Constructor. Binds socket to addr */
    public S_StreamSocket(InetSocketAddress addr) throws SocketException {
        // Silence logger 
        Logger topLogger = Logger.getLogger("");

        Handler consoleHandler = null;
        for (Handler handler : topLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                consoleHandler = handler;
                break;
            }
        }

        if (consoleHandler == null) {
            consoleHandler = new ConsoleHandler();
            topLogger.addHandler(consoleHandler);
        }
        consoleHandler.setLevel(Level.INFO);

        socket = new T_DatagramSocket(addr);
        socket.T_setSoTimeout(WorkerThread.SO_TIMEOUT);
    }

    /* Receive timeout in milliseconds */
    public void S_setSoTimeout(int timeout) throws SocketException {
        this.recieveTimeout = timeout;
    }

    /* Details of local socket (IP & port) */
    public InetSocketAddress S_getLocalSocketAddress() {
        return socket.T_getLocalSocketAddress();
    }

    /* Used by client to connect to server */
    public void S_connect(InetSocketAddress serverAddr) throws IOException {
        log.finest("Connecting to server...");
        resetWorker();
        worker.connect(serverAddr);
        worker.start(); // start the network monitoring thread
    }

    /* Used by server to accept a new connection */
    /* Returns the IP & port of the client */
    public InetSocketAddress S_accept() throws IOException {
        log.finest("Listening for connections...");
        resetWorker();
        InetSocketAddress addr = worker.accept();
        worker.start(); // start the network monitoring thread
        return addr;
    }

    /* Used to send data. len can be arbitrarily large or small */
    public void S_send(byte[] buf, int len) {
        if (buf == null || buf.length < len) {
            throw new IllegalArgumentException();
        }
        if (len == 0) {
            return;
        }

        worker.send(buf, len);
    }

    /*
     * Used to receive data. Max chunk of data received is len. The actual
     * number of bytes received is returned
     */
    public int S_receive(byte[] buf, int len) throws IOException, InterruptedException {
        if (buf == null || buf.length < len) {
            throw new IllegalArgumentException();
        }
        if (len == 0) {
            return 0;
        }

        return worker.receive(buf, len, recieveTimeout);
    }

    /* To close the connection */
    public void S_close() throws InterruptedException /* throws ... */
    {
        worker.setShouldClose(); // tell the thread to close
        worker.join(); // wait for it to actually close
        worker = null; 
    }

    protected void resetWorker() throws SocketException {
        worker = new WorkerThread(socket);
    }

}
