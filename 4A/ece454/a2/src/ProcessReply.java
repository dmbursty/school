import java.io.Serializable;



public class ProcessReply implements Serializable {
    private char[] data;
    
    public ProcessReply(int s2) {
        data = new char[s2];
    }
}
