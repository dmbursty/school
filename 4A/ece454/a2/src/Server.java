import java.rmi.Naming;


public class Server {
    public Server() {
        try {
            SimpleImpl impl = new SimpleImpl();
            Naming.rebind("rmi://localhost:7776/Simple", impl);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Server();
    }
}
