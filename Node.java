import java.util.*;

public class Node implements Runnable{
    SharedMemory sharedMemory = new SharedMemory();
    FileReader fileReader = new FileReader();
    private int uId;
    private int tempId;
    private List<Integer> neighbors; //uidindices
    int round = 1;
    int status = -1;

    public Node(SharedMemory sharedMemory, int uId, List<Integer> neighbors){
        this.sharedMemory = sharedMemory;
        this.uId=uId;
        this.neighbors=neighbors;
    }


    //Method to get Temp Id
    public int getTempId(){
        return tempId;
    }

    //status = -1:unknown , 0:loser , 1:winner
    public int getStatus(){
        return status;
    }

    public int setTempId(){
        int maxId = (int) Math.pow(fileReader.noOfProcesses, 4);
        return (int)(Math.random()*maxId + 1);
    }

    public void run() {
        //get id from all nbrs, compare and set status
        int currentIdx = sharedMemory.pIdMap.get(uId);
        int awake = sharedMemory.awake.get(currentIdx);
        int count=0;
        int round = 1;
        while(round <= 3){
            if(awake==1){
                //Round 1
                if(round==1){
                    setTempId();
                    Iterator nbr_itr = neighbors.iterator();
                    while(nbr_itr.hasNext()){
                        int nbrUId = (int) nbr_itr.next();
                        Node nbrNode = sharedMemory.processThread.get(nbrUId);
                        int nbrTempId = nbrNode.getTempId();
                        if(tempId > nbrTempId){
                            count++;
                        }else if(tempId == nbrTempId){
                            break;
                        }
                    }
                }
                //Round 2
                else if(round==2){
                    if(count==neighbors.size()) {
                        synchronized (sharedMemory.MIS){
                            sharedMemory.MIS.add(uId);
                            sharedMemory.winners.add(uId);
                        }
                        status = 1;
                        this.neighbors = new ArrayList<>();
                    }

                    if(count < neighbors.size()){
                        status = 0;
                        sharedMemory.losers.add(uId);
                    }

                    if(status==-1 && neighbors.size()==0)
                    {
                        synchronized (sharedMemory.MIS){
                            sharedMemory.MIS.add(uId);
                            sharedMemory.winners.add(uId);
                        }
                    }
                    //Round 3
                } else if (round == 3) {
                    if(status==1 || status==0){
                        sharedMemory.awake.add(currentIdx,0);
                    }

                    Iterator nbr_itr = neighbors.iterator();
                    Set<Integer> loserNbrs = new HashSet<>();;
                    while(nbr_itr.hasNext()){
                        int nbrUId = (int) nbr_itr.next();
                        Node nbrNode = sharedMemory.processThread.get(nbrUId);
                        int nbrStatus = nbrNode.getStatus();
                        if(nbrStatus==0){
                            loserNbrs.add(nbrUId);
                        }
                    }
                    neighbors.removeAll(loserNbrs);
                }

                round = (round+1)%3;
        }


        }
    }

}
