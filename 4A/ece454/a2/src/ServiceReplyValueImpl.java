import java.io.Serializable;
import java.rmi.Remote;


public class ServiceReplyValueImpl implements Serializable, ServiceReply, Remote {
   public char[] data;
    
    public ServiceReplyValueImpl(int s1) {
        super();
        data = new char[s1];
    }
    
    @Override
    public ProcessReply process(int t2, int s2, int k) {
        try {
            Thread.sleep(t2 * k);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ProcessReply(s2);
    }

}
