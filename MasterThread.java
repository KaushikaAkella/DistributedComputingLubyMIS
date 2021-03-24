import com.sun.source.tree.LiteralTree;

import java.io.File;
import java.util.*;

public class MasterThread implements Runnable{
    static SharedMemory sharedMemory = new SharedMemory();
    static FileReader fileReader = new FileReader();


    public void run(){
        System.out.println("Starting thread handler");

        Map<Integer, Node> nodes = new HashMap<>();
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
            nodes.put(uId, node);
        }

        int phase = 0;

        for(Map.Entry<Integer, Node> map : SharedMemory.processThread.entrySet()){
            System.out.println(map.getKey()+","+map.getValue().getTempId());
        }

        Set<Integer> candidateSet = new HashSet<>(fileReader.processIds);
        for(int i = 0; i < sharedMemory.noOfProcesses; i++){
            sharedMemory.processStatus.add(0);
        }
        while(!candidateSet.isEmpty()){
            int moveToNextRound = 0;
            System.out.println("Round 1");
            List<Thread> threads = new ArrayList<>();

            for(Map.Entry<Integer, Node> entry : nodes.entrySet()){
                Thread thread = new Thread(entry.getValue());
                threads.add(thread);
                thread.start();
            }
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(sharedMemory.processStatus.contains(0)){
                moveToNextRound = 0;
            }
            else{
                moveToNextRound = 1;
            }
            if(moveToNextRound == 1){
                System.out.println("Round 2");
                sharedMemory.round++;

                for(Map.Entry<Integer, Node> entry : nodes.entrySet()){
                    Thread thread = new Thread(entry.getValue());
                    threads.add(thread);
                    thread.start();
                }
                for (Thread thread : threads) {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(sharedMemory.processStatus.contains(0)){
                moveToNextRound = 0;
            }
            else{
                moveToNextRound = 1;
            }
            Iterator candidateSetItr = candidateSet.iterator();
            while(candidateSetItr.hasNext()) {
                sharedMemory.processStatus.set(sharedMemory.pIdMap.get((int) candidateSetItr.next()), 0);
            }
            if(moveToNextRound == 1){
                System.out.println("Round 3");
                sharedMemory.round++;
                for(Map.Entry<Integer, Node> entry : nodes.entrySet()){
                    Thread thread = new Thread(entry.getValue());
                    threads.add(thread);
                    thread.start();
                }
                for (Thread thread : threads) {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            phase++;

            sharedMemory.round = 1;
            System.out.println("phase "+phase);
            System.out.println(sharedMemory.winners +"///"+sharedMemory.losers+"///"+candidateSet);
            candidateSet.removeAll(sharedMemory.winners);
            candidateSet.removeAll(sharedMemory.losers);
            Iterator itr = sharedMemory.winners.iterator();
            while(itr.hasNext()){
                nodes.remove((int)itr.next());
            }
            itr = sharedMemory.losers.iterator();
            while(itr.hasNext()){
                nodes.remove((int)itr.next());
            }
            for(Map.Entry<Integer, Node> entry : nodes.entrySet()){
                entry.getValue().setTempIdPhase();
            }
        }


//        Set<Integer> candidateSet = new HashSet<>(fileReader.processIds);

//        while(!candidateSet.isEmpty()){
//            for(int i =0; i < nodes.size(); i++) {
//                Thread t = new Thread(nodes.get(i));
//                t.start();
//            }
//            System.out.println("Round 2");
//            sharedMemory.round++;
//            for(int i =0; i < nodes.size(); i++) {
//                Thread t1 = new Thread(nodes.get(i));
//                t1.start();
//                t1.join();
//            }
//            System.out.println("Round 3");
//            sharedMemory.round++;
//            for(int i =0; i < nodes.size(); i++) {
//                Thread t2 = new Thread(nodes.get(i));
//                t2.start();
//                t.join();
//            }
//            phase++;
//            System.out.println(sharedMemory.winners +"///"+sharedMemory.losers+"///"+candidateSet);
//            candidateSet.removeAll(sharedMemory.winners);
//            candidateSet.removeAll(sharedMemory.losers);
//            for(int i =0; i <  fileReader.processIds.size(); i++){
//                if(!candidateSet.contains(fileReader.processIds.get(i))){
//                    nodes.remove(sharedMemory.pIdMap.get(fileReader.processIds.get(i)));
//                }
//            }
/*
            List<Integer> winnersLosers = new ArrayList<>();
            winnersLosers.addAll(sharedMemory.winners);
            winnersLosers.addAll(sharedMemory.losers);
            List<Integer> winnerLosersIdx = new ArrayList<>();
            for(int i =0; i < winnersLosers.size(); i++){
                winnerLosersIdx.add(pIdMap.get());
            }
            nodes.remove();
*/
        //}
        System.out.println("hereeeeeee"+candidateSet.size());
        System.out.println(sharedMemory.MIS);
    }
//    public static void main(String args[]) throws InterruptedException {
//
//    }

}
