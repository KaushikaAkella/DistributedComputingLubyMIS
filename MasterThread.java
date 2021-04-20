import java.util.*;

//Master Thread
public class MasterThread implements Runnable{
    static SharedMemory sharedMemory = new SharedMemory();
    static FileReader fileReader = new FileReader();
    Map<Integer, Node> nodes = new HashMap<>();

    public void run(){
        initialiseSharedMemory();
        Set<Integer> candidateSet = new HashSet<>(fileReader.processIds);
        sharedMemory.restartExecution = 0;
        int phase = 1;

        //Print UID , Temp id (random)
//        for(Map.Entry<Integer, Node> map : SharedMemory.processThread.entrySet()){
//            System.out.println(map.getKey()+","+map.getValue().getTempId());
//        }


        //Candidate set consists of all nodes that need to be assessed in the next phase. Initially, all nodes.
        while(!candidateSet.isEmpty()){
                int moveToNextRound = 0;

                //Starting Round 1
                runProcesses();
                moveToNextRound = getProceedPermission();  //Master thread checks if all processes have completed execution. All completed processes will update status to 1.
                waitForRunningThreads();


                //Restart execution when thread failure occurs. This happends after waiting for the thread ( 3 seconds)
                if(sharedMemory.processStatus.contains(0)){
                    System.out.println("Restarting execution...");
                    sharedMemory.restartExecution = 1;
                    break;
                }

                //Setting status of threads in candidate set to 0 (Live) to proceed with next round
                Iterator candidateSetItr = candidateSet.iterator();
                while(candidateSetItr.hasNext()) {
                    sharedMemory.processStatus.set(sharedMemory.pIdMap.get((int) candidateSetItr.next()), 0);
                }

                //Starting Round 2  if Round 1 is done.
                if(moveToNextRound == 1){
                    sharedMemory.round++;
                    runProcesses();
                }

                moveToNextRound = getProceedPermission();  //Master thread checks if all processes have completed execution. All completed processes will update status to 1.
                waitForRunningThreads();

                if(sharedMemory.processStatus.contains(0)){
                    System.out.println("Restarting execution...");
                    sharedMemory.restartExecution = 1;
                    break;
                }

                //Setting status of threads in candidate set to 0 (Live) to proceed with next round
                candidateSetItr = candidateSet.iterator();
                while(candidateSetItr.hasNext()) {
                    sharedMemory.processStatus.set(sharedMemory.pIdMap.get((int) candidateSetItr.next()), 0);
                }

                //Starting Round 3  if Round 2 is done.
                if(moveToNextRound == 1){
                    sharedMemory.round++;
                    runProcesses();
                }

                phase++; // Moving to next phase

                //Updating candidate set
                sharedMemory.round = 1;
                candidateSet.removeAll(sharedMemory.winners);
                candidateSet.removeAll(sharedMemory.losers);
                System.out.println("At the end of phase : "+(phase-1)+" \n Winners are :"+sharedMemory.winners+" \n Losers are :"+sharedMemory.losers+" \n MIS is :"+sharedMemory.MIS+"\n \n");

                //Updating nodes
                Iterator itr = sharedMemory.winners.iterator();
                while(itr.hasNext()){
                    nodes.remove((int)itr.next());
                }
                itr = sharedMemory.losers.iterator();
                while(itr.hasNext()){
                    nodes.remove((int)itr.next());
                }

                //Set Temp Ids for next phase
                for(Map.Entry<Integer, Node> entry : nodes.entrySet()){
                    entry.getValue().setTempIdPhase();
                }
            }

        if(sharedMemory.restartExecution!=1){
            System.out.println("Total number of phases : "+(phase-1));
            System.out.println("MIS : "+sharedMemory.MIS);
        }

    }


    public void initialiseSharedMemory(){
        sharedMemory.MIS = new HashSet<>();
        sharedMemory.winners = new HashSet<>();
        sharedMemory.losers = new HashSet<>();
        sharedMemory.awake = new ArrayList<>();
        sharedMemory.processStatus = new ArrayList<>();
        sharedMemory.processThread = new HashMap<>();
        sharedMemory.pIdMap = new HashMap<>();

        for(int i = 0; i < sharedMemory.noOfProcesses;i++){
            sharedMemory.pIdMap.put(fileReader.processIds.get(i),i);
        }
        for(int i = 0; i < sharedMemory.noOfProcesses;i++){
            sharedMemory.awake.add(1);
        }
        for(int i = 0; i < sharedMemory.noOfProcesses; i++){
            sharedMemory.processStatus.add(0);
        }
        for(int i = 0; i < sharedMemory.noOfProcesses; i++){
            int uId = fileReader.processIds.get(i);
            List<Integer> neighbors = fileReader.graph.get(uId);
            Node node = new Node(sharedMemory,  uId, neighbors);
            sharedMemory.processThread.put(uId, node);
            this.nodes.put(uId, node);
        }
    }

    public void runProcesses(){
        List<Thread> threads = new ArrayList<>();
        for(Map.Entry<Integer, Node> entry : nodes.entrySet()){
            Thread thread = new Thread(entry.getValue());
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                if(thread.isAlive()){
                    thread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getProceedPermission(){
        if(sharedMemory.processStatus.contains(0)){
             return 0;
        }
        else{
            return 1;
        }
    }

    public void waitForRunningThreads(){
        if(sharedMemory.processStatus.contains(0)){
            try {
                System.out.println("Waiting for incomplete threads (3 seconds) "+sharedMemory.processStatus);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
