import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ServiceReplyImpl extends UnicastRemoteObject implements ServiceReply {
    public char[] data;
    
    public ServiceReplyImpl(int s1) throws RemoteException {
        super();
        data = new char[s1];
    }
    
    @Override
    public ProcessReply process(int t2, int s2, int k) throws RemoteException {
        try {
            Thread.sleep(t2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ProcessReply(s2);
    }
}
