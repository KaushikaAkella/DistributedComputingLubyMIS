import java.util.*;
import java.io.*;

public class FileReader{
    public static int noOfProcesses;
    public static List<Integer> processIds = new ArrayList<>();
    public static Map<Integer, List<Integer>> graph = new HashMap<>();
    public FileReader(){
        try{
            File fileObj = new File("input.txt");
            Scanner scanObj = new Scanner(fileObj);
            List<String> inputData = new ArrayList<>();
            while(scanObj.hasNextLine()){
                inputData.add(scanObj.nextLine());
            }
            noOfProcesses = Integer.valueOf(inputData.get(0));
            List<String> inputProcessIds = Arrays.asList(inputData.get(1).split(" "));
            for(int i = 0; i < inputProcessIds.size(); i++){
                processIds.add(Integer.valueOf(inputProcessIds.get(i)));
            }
            graph = new HashMap<>();
            for(int i =0; i < noOfProcesses; i++){
                List<String> neighborString = Arrays.asList(inputData.get(i+2).split(" "));
                List<Integer> neighbors = new ArrayList<>();
                for(int j = 0; j < neighborString.size(); j++){
                    neighbors.add(Integer.valueOf(neighborString.get(j)));
                }
                List<Integer> neighborIds = new ArrayList<>();
                for(int j = 0; j < neighbors.size(); j++){
                    if(neighbors.get(j) == 1){
                        neighborIds.add(processIds.get(j));
                    }
                }
                graph.put(processIds.get(i), neighborIds);
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not found exception");
        }
    }
}
