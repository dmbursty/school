import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class SimpleImpl extends UnicastRemoteObject implements Simple {
	
	boolean byVal = true;

    public SimpleImpl() throws RemoteException {
        super();
    }

    @Override
    public ServiceReply simpleService(int t1, int s1) throws RemoteException {
        try {
            Thread.sleep(t1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (byVal) {
        	return new ServiceReplyValueImpl(s1);
        } else {
        	return new ServiceReplyImpl(s1);
        }
    }
}
