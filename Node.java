import java.util.*;

public class Node implements  Runnable {
    SharedMemory sharedMemory = new SharedMemory();
    FileReader fileReader = new FileReader();
    private int uId;
    private int tempId;
    private List<Integer> neighbors; //uidindices
    int round = 1;
    int status = -1;
    int count=0;
    public Node(SharedMemory sharedMemory, int uId, List<Integer> neighbors){
        this.sharedMemory = sharedMemory;
        this.uId=uId;
        this.neighbors=neighbors;
        this.tempId = setTempId();
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
       // System.out.println("Entering "+uId);
        //get id from all nbrs, compare and set status
        int currentIdx = sharedMemory.pIdMap.get(uId);
        int awake = sharedMemory.awake.get(currentIdx);

        //int round = 1;
            if(awake==1){
                //Round 1
                if(sharedMemory.round==1){
                  //  System.out.println("R1");
                    setTempId();
                    Iterator nbr_itr = neighbors.iterator();
                    while(nbr_itr.hasNext()){
                        int nbrUId = (int) nbr_itr.next();
                        Node nbrNode = sharedMemory.processThread.get(nbrUId);
                        int nbrTempId = nbrNode.getTempId();
                        if(tempId > nbrTempId){
                            count++;
                        }else if(tempId == nbrTempId){
                            sharedMemory.awake.add(currentIdx,0);
                            return;
                        }
                    }
                    if(count==neighbors.size()) {
                        synchronized (sharedMemory.MIS){
                            sharedMemory.MIS.add(uId);
                            sharedMemory.winners.add(uId);
                        }
                        status = 1;
                        //sthis.neighbors = new ArrayList<>();
                    }
                    System.out.println("UID "+uId+"/// TEMP ID "+tempId+" ///count "+count+" ///NBRS "+neighbors.size());
                }
                //Round 2
                else if(sharedMemory.round==2){
                    System.out.println("R2");


//                    if(count < neighbors.size()){
//                        status = 0;
//                        sharedMemory.losers.add(uId);
//                    }

//                    Iterator nbr_itr = neighbors.iterator();
//                    while(nbr_itr.hasNext()){
//                        int nbrUId = (int) nbr_itr.next();
//                        Node nbrNode = sharedMemory.processThread.get(nbrUId);
//                        int nbrStatus = nbrNode.getStatus();
//                        if(nbrStatus==1){
//                            status=0;
//                           // sharedMemory.losers.add(uId);
//                            this.neighbors = new ArrayList<>();
//                        }
//                    }

                    for(int i=0;i<this.neighbors.size();i++){
                        int nbrUId = neighbors.get(i);
                        Node nbrNode = sharedMemory.processThread.get(nbrUId);
                        int nbrStatus = nbrNode.getStatus();
                        if(nbrStatus==1){
                            status=0;
                            sharedMemory.losers.add(uId);
                            //this.neighbors = new ArrayList<>();
                        }
                    }

                    System.out.println("UID "+uId+"/// TEMP ID "+tempId+" ///status "+status+" ///MIS "+sharedMemory.MIS+" ///W "+sharedMemory.winners+" ///L"+sharedMemory.losers);
                } //Round 3
                else if (sharedMemory.round == 3) {
                    System.out.println("R3");
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
//                    if(status==-1 && neighbors.size()==0)
//                    {
//                        synchronized (sharedMemory.MIS){
//                            sharedMemory.MIS.add(uId);
//                            sharedMemory.winners.add(uId);
//                        }
//                    }
                    System.out.println("UID "+uId+"/// TEMP ID "+tempId+" ///neighbors "+neighbors+" ///loserNbrs "+loserNbrs);
                }
//                System.out.println("The status of the process is "+status);
//                System.out.println("neighbors are "+neighbors);
                round = (round+1);
       // }

        }
       // System.out.println("Exiting "+uId);
    }

}
