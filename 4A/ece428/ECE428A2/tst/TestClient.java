import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TestClient {
    protected static S_StreamSocket socket;
    
    public static void analysisClient() throws Exception {
        byte[] data = new byte[256000];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i % Byte.MAX_VALUE);
        }
        
        socket.S_connect(new InetSocketAddress("localhost", 13337));
        long now = System.currentTimeMillis();
        System.out.println("==== Data sent on " + now + "====");
        socket.S_send(data, 250*1024);
        now = System.currentTimeMillis();
        
        //socket.S_close();        
    }

    public static void greetingClient() throws Exception {
        String test = "Hello server, how are you?";
        socket.S_connect(new InetSocketAddress("localhost", 13337));        
        socket.S_send(test.getBytes(), test.getBytes().length);
        
        byte[] buf = new byte[100];
        String res = "";
        int recvLen = 0;
        int len = 0;
        while (recvLen < 34) {
            len = socket.S_receive(buf, 100);
            recvLen += len;
            res += new String(buf, 0, len);
        }
        System.out.println(res);        
        
        socket.S_close();
    }

    public static void reuseSocket() throws Exception {
        greetingClient();
        greetingClient();
    }
    
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws Exception {
	    Logger.getLogger("").setLevel(Level.FINE);
        InetSocketAddress addr = new InetSocketAddress("localhost", 0);
        socket = new S_StreamSocket(addr);
        analysisClient();

        System.out.println("done main");
	}

}
