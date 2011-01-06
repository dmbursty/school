import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TestServer {
    protected static S_StreamSocket socket;
    private static Logger log = Logger.getLogger(TestServer.class.getName());
    
    public static void analysisServer() throws Exception {
        byte[] buf = new byte[256000];
        while (true) {
            socket.S_accept();
            
            int len = 0;
            int recvLen = 0;
            while (recvLen < 1*1024) {
                len = socket.S_receive(buf, buf.length);
                recvLen += len;
            }
            System.out.println("==== Received " + recvLen + " at " + System.currentTimeMillis() + "====");
            
            socket.S_close();
            break;
        }
    }

    public static void greetingServer() throws Exception {
        byte[] buf = new byte[100];
        while (true) {
            socket.S_accept();
            
            String res = "";
            int recvLen = 0;
            int len = 0;
            while (recvLen < 26) {
                len = socket.S_receive(buf, 100);
                recvLen += len;
                res += new String(buf, 0, len);
            }
            System.out.println(res);

            String data = "Hello, client. I'm fine thank you.";
            socket.S_send(data.getBytes(), data.getBytes().length);
            socket.S_close();
            break;
        }
    }    
    
    public static void reuseSocket() throws Exception {
        greetingServer();
        greetingServer();
    }
    
    /**
     * @param args
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws Exception {
        Logger.getLogger("").setLevel(Level.FINE);
        InetSocketAddress addr = new InetSocketAddress("localhost", 13337);
        socket = new S_StreamSocket(addr);
        greetingServer();

        System.out.println("done main");
    }
}
