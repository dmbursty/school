import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TestServer {
    protected static S_StreamSocket socket;
    
    public static void analysisServer() throws Exception {
        byte[] buf = new byte[256000];
        byte[] bigbuf = new byte[256000];
        while (true) {
            socket.S_accept();
            
            int len = 0;
            int recvLen = 0;
            while (recvLen < 250*1024) {
                len = socket.S_receive(buf, buf.length);
                System.out.println("Got " + len);
                for (int i = 0; i < len; i++) {
                    bigbuf[recvLen + i] = buf[i];
                }
                recvLen += len;
            }
            System.out.println("==== Received " + recvLen + " at " + System.currentTimeMillis() + "====");
            
            int badbytes = 0;
            for (int i = 0; i < recvLen; i++) {
                if (bigbuf[i] != (byte) (i % Byte.MAX_VALUE)) {
                    if (badbytes == 0) {
                        System.out.println("first mismatch at byte " + i);
                    }
                    badbytes++;
                }
            }
            System.out.println(badbytes + " badbytes");
            
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
            //socket.S_close();
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
        analysisServer();

        System.out.println("done main");
    }
}
