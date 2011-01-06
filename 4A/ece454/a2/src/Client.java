import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class Client {
    public static void main(String[] args) {
            Simple c;
            try {
            	int k, t1, s1, t2, s2;
            	
            	k = Integer.parseInt(args[0]);
            	t1 = Integer.parseInt(args[1]);
            	s1 = Integer.parseInt(args[2]);
            	t2 = Integer.parseInt(args[3]);
            	s2 = Integer.parseInt(args[4]);
            	
                c = (Simple) Naming.lookup("rmi://burstyn.ca:7776/Simple");
                
                int total = 0;
                
                for (int i = 0; i < 5; i++) {
                	long start = System.currentTimeMillis();
                	ServiceReply s = c.simpleService(t1, s1);
                	long mid = System.currentTimeMillis();
                	s.process(t2, s2, k);
                	long end = System.currentTimeMillis();
                	
                	total += (end - start);
                }
                
                System.out.print(k + ",");
                System.out.print(t1 + ",");
                System.out.print(s1 + ",");
                System.out.print(t2 + ",");
                System.out.print(s2 + ",");
                System.out.println(total / 5);
                
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
    }
}
