import java.util.Iterator;
import java.util.List;

public class Main {
    static MasterThread masterThread = new MasterThread();
    static SharedMemory sharedMemory = new SharedMemory();
    static FileReader fileReader = new FileReader();

    public static void misVerification(){
        int misFalse = 0;
        Iterator itr = sharedMemory.MIS.iterator();
        while(itr.hasNext()){
            int misVertex = (int) itr.next();
            List<Integer> misVertexNeighbors = fileReader.graph.get(misVertex);
            for(int i = 0; i < misVertexNeighbors.size(); i++){
                if(sharedMemory.MIS.contains(misVertexNeighbors.get(i)) && misVertex != misVertexNeighbors.get(i)){
                    misFalse = 1;
                }
            }
        }
        if(misFalse == 1){
            System.out.println("Generated MIS is incorrect");
        }
        else{
            System.out.println("Generated MIS is correct!");
        }
    }

    public static void main(String args[]) throws InterruptedException {
        Thread thr = new Thread(masterThread);
        thr.start();
        thr.join();

        misVerification();
    }
}
