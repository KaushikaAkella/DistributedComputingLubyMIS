import java.util.*;

public class Node implements  Runnable {
    SharedMemory sharedMemory = new SharedMemory();
    FileReader fileReader = new FileReader();
    private final int uId;
    private int tempId;
    private final List<Integer> neighbors; //uidindices
    int round = 1;
    int status = -1;
    int count=0;
    public Node(SharedMemory sharedMemory, int uId, List<Integer> neighbors){
        this.sharedMemory = sharedMemory;
        this.uId=uId;
        this.neighbors=neighbors;
        this.tempId = setTempId();
    }

    public void setTempIdPhase(){
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
        int maxId = (int) Math.pow(FileReader.noOfProcesses, 4);
        return (int)(Math.random()*maxId + 1);
    }

    public void run() {

            //get id from all nbrs, compare and set status
            int currentIdx = sharedMemory.pIdMap.get(uId);
            int awake = sharedMemory.awake.get(currentIdx);

            if(awake==1){
                //Round 1
                    if(sharedMemory.round ==1){
                        count = 0;

                        //Check if my Id > neighbors Id
                        Iterator nbr_itr = neighbors.iterator();
                        while(nbr_itr.hasNext()){
                            int nbrUId = (int) nbr_itr.next();
                            Node nbrNode = SharedMemory.processThread.get(nbrUId);
                            int nbrTempId = nbrNode.getTempId();
                            if(tempId > nbrTempId){
                                count++;
                            }else if(tempId == nbrTempId){
                                synchronized (sharedMemory.awake){
                                    SharedMemory.awake.add(currentIdx,0);
                                }
                                return;
                            }
                        }

                        //If Node has no neighbors and status = unknown, it is added to MIS
                        if(status==-1 && neighbors.size() == 0){
                            synchronized (sharedMemory.MIS){
                                sharedMemory.MIS.add(uId);
                            }
                            synchronized (sharedMemory.winners){
                                sharedMemory.winners.add(uId);
                            }
                            status = 1;
                        }

                        //If my Id > all neighbors , I am a winner. Added to MIS.
                        if(count==neighbors.size()) {
                            synchronized (sharedMemory.MIS){
                                sharedMemory.MIS.add(uId);
                            }
                            synchronized (sharedMemory.winners){
                                sharedMemory.winners.add(uId);
                            }
                            status = 1;
                        }

                        //Set my process status to 1. Completed work for round 1.
                        synchronized (sharedMemory.processStatus){
                            sharedMemory.processStatus.set(sharedMemory.pIdMap.get(uId), 1);
                        }
                    }

                    //Round 2
                    else if(sharedMemory.round ==2){

                        //If any of my neighbors is winner, set my status to loser
                        for(int i=0;i<this.neighbors.size();i++){
                            int nbrUId = neighbors.get(i);
                            Node nbrNode = sharedMemory.processThread.get(nbrUId);
                            int nbrStatus = nbrNode.getStatus();
                            if(nbrStatus==1){
                                status=0;
                                synchronized (sharedMemory.losers){
                                    sharedMemory.losers.add(uId);
                                }
                            }
                        }

//                      System.out.println("UID "+uId+"/// TEMP ID "+tempId+" ///status "+status+" ///MIS "+ SharedMemory.MIS +" ///W "+ SharedMemory.winners +" ///L"+ SharedMemory.losers);

                        //Set my process status to 1. Completed work for round 1.
                        synchronized (sharedMemory.processStatus){
                            synchronized (sharedMemory.pIdMap){
                                sharedMemory.processStatus.set(sharedMemory.pIdMap.get(uId), 1);
                            }
                        }

                        //If Node has no neighbors and status = unknown, it is added to MIS
                        if(status==-1 && neighbors.size() == 0){
                            synchronized (sharedMemory.MIS){
                                sharedMemory.MIS.add(uId);
                            }
                            synchronized (sharedMemory.winners){
                                sharedMemory.winners.add(uId);
                            }
                            status = 1;
                        }
                    }

                    //Round 3
                    else if (SharedMemory.round == 3) {
                        //Set awake = 0 when status = winner or loser
                        if(status==1 || status==0){
                            synchronized (sharedMemory.awake){
                                sharedMemory.awake.add(currentIdx,0);
                            }
                        }

                        //Remove losers from my neighbors
                        Iterator nbr_itr = neighbors.iterator();
                        Set<Integer> loserNbrs = new HashSet<>();
                        while(nbr_itr.hasNext()){
                            int nbrUId = (int) nbr_itr.next();
                            Node nbrNode = sharedMemory.processThread.get(nbrUId);
                            int nbrStatus = nbrNode.getStatus();
                            if(nbrStatus==0){
                                loserNbrs.add(nbrUId);
                            }
                        }
                        neighbors.removeAll(loserNbrs);

                        //Remove winners from my neighbors
                        nbr_itr = neighbors.iterator();
                        Set<Integer> winnerNbrs = new HashSet<>();
                        while(nbr_itr.hasNext()){
                            int nbrUId = (int) nbr_itr.next();
                            Node nbrNode = sharedMemory.processThread.get(nbrUId);
                            int nbrStatus = nbrNode.getStatus();
                            if(nbrStatus==1){
                                winnerNbrs.add(nbrUId);
                            }
                        }
                        neighbors.removeAll(winnerNbrs);

//                        System.out.println("UID "+uId+"/// TEMP ID "+tempId+" ///neighbors "+neighbors+" ///loserNbrs "+loserNbrs +"/// winnernbrs "+winnerNbrs);

                        //If Node has no neighbors and status = unknown, it is added to MIS
                        if(status==-1 && neighbors.size() == 0){
                            synchronized (sharedMemory.MIS){
                                sharedMemory.MIS.add(uId);
                            }
                            synchronized (sharedMemory.winners){
                                sharedMemory.winners.add(uId);
                            }
                            status = 1;
                        }

                        //Set my process status to 1. Completed work for round 1.
                        synchronized (sharedMemory.processStatus){
                            synchronized (sharedMemory.pIdMap){
                                sharedMemory.processStatus.set(sharedMemory.pIdMap.get(uId), 1);
                            }
                        }
                }

            }

    }

}
