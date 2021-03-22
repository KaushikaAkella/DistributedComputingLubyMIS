import java.io.File;
import java.util.*;

public class ThreadHandler{
    static SharedMemory sharedMemory = new SharedMemory();
    static FileReader fileReader = new FileReader();

    public static void main(String args[]) throws InterruptedException {
        List<Node> nodes = new ArrayList<>();
        for(int i = 0; i < sharedMemory.noOfProcesses; i++){
            int uId = fileReader.processIds.get(i);
            List<Integer> neighbors = fileReader.graph.get(uId);
            Node node = new Node(sharedMemory,  uId, neighbors);
            sharedMemory.processThread.put(uId, node);
            nodes.add(node);
        }
        int phase = 0;
        Set<Integer> candidateSet = new HashSet<>(fileReader.processIds);
        while(!candidateSet.isEmpty()){
            for(int i =0; i < nodes.size(); i++) {
                Thread t = new Thread(nodes.get(i));
                t.join();
            }
            phase++;
            candidateSet.removeAll(sharedMemory.winners);
            candidateSet.removeAll(sharedMemory.losers);
        }
        System.out.println(sharedMemory.MIS);
    }

}
