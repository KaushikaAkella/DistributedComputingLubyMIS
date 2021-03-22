import java.io.File;
import java.util.*;

public class ThreadHandler{
    static SharedMemory sharedMemory = new SharedMemory();
    static FileReader fileReader = new FileReader();

    public static void main(String args[]) throws InterruptedException {
        System.out.println("Starting thread handler");
        List<Node> nodes = new ArrayList<>();
        for(int i = 0; i < sharedMemory.noOfProcesses;i++){
            sharedMemory.pIdMap.put(fileReader.processIds.get(i),i);
        }
        for(int i = 0; i < sharedMemory.noOfProcesses;i++){
            sharedMemory.awake.add(1);
        }
        for(int i = 0; i < sharedMemory.noOfProcesses; i++){
            int uId = fileReader.processIds.get(i);
            List<Integer> neighbors = fileReader.graph.get(uId);
            Node node = new Node(sharedMemory,  uId, neighbors);
            sharedMemory.processThread.put(uId, node);
            nodes.add(node);
        }
        int phase = 0;
        System.out.println(phase);
        Set<Integer> candidateSet = new HashSet<>(fileReader.processIds);
        while(!candidateSet.isEmpty()){
            for(int i =0; i < nodes.size(); i++) {
                Thread t = new Thread(nodes.get(i));
                t.start();
            }
            phase++;
            candidateSet.removeAll(sharedMemory.winners);
            candidateSet.removeAll(sharedMemory.losers);
            for(int i =0; i <  fileReader.processIds.size(); i++){
                if(!candidateSet.contains(fileReader.processIds.get(i))){
                    nodes.remove(sharedMemory.pIdMap.get(fileReader.processIds.get(i)));
                }
            }
//            List<Integer> winnersLosers = new ArrayList<>();
//            winnersLosers.addAll(sharedMemory.winners);
//            winnersLosers.addAll(sharedMemory.losers);
//            List<Integer> winnerLosersIdx = new ArrayList<>();
//            for(int i =0; i < winnersLosers.size(); i++){
//                winnerLosersIdx.add(pIdMap.get());
//            }
//            nodes.remove();
        }
        System.out.println(sharedMemory.MIS);
    }

}
