import java.util.List;
import java.util.Set;
import java.util.Map;

public class ThreadHandler{
    static SharedMemory sharedMemory = new SharedMemory();

    public static void main(String args[]){
        Thread t1 = new Thread(new Node(sharedMemory));
    }

}
