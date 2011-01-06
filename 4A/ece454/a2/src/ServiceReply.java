import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ServiceReply extends Remote {
    public ProcessReply process(int t2, int s2, int k) throws RemoteException;
}
