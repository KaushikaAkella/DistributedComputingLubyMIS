import java.util.List;
import java.util.Map;
import java.util.Set;

public class Node implements Runnable{
    SharedMemory sharedMemory = new SharedMemory();
    FileReader fileReader = new FileReader();
    private int uId;
    private int tempId;
    private Set<Integer> neighbors; //uidindices
    int round = 1;

    public Node(SharedMemory sharedMemory, int uId, int tempId, Set<Integer> neighbors){
        this.sharedMemory = sharedMemory;
        this.uId=uId;
        this.tempId=tempId;
        this.neighbors=neighbors;
    }


    //Method to get Temp Id
    public int getTempId(){
        return tempId;
    }
    public int setTempId(){
        int maxId = (int) Math.pow(fileReader.noOfProcesses, 4);
        return (int)(Math.random()*maxId + 1);
    }

    public void run() {
        //Send id to all nbrs , get id from all nbrs
        //compare and set status
        //If sts is winner ->

    }

    //Start the thread


}
