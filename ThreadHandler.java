import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class ThreadHandler{
    static SharedMemory sharedMemory = new SharedMemory();
    static FileReader fileReader = new FileReader();

    public static void main(String args[]){
        Thread[] t = new Thread[sharedMemory.noOfProcesses];
        for(int i = 0; i < sharedMemory.noOfProcesses; i++){
            int uId = fileReader.processIds.get(i);
            List<Integer> neighbors = fileReader.graph.get(uId);
            Node node = new Node(sharedMemory,  uId, neighbors);
            sharedMemory.processThread.put(uId, node);
            t[i] = new Thread(node);
        }
        int phase = 0;
        Set<Integer> candidateSet = new HashSet<>();
    }

}
