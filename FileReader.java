import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class FileReader{
    public static int noOfProcesses;
    public static List<Integer> processIds;
    public static Map<Integer, List<Integer>> graph;
    public FileReader(){
        try{
            File fileObj = new File("input.txt");
            Scanner scanObj = new Scanner(fileObj);
            List<String> inputData = new ArrayList<>();
            while(scanObj.hasNextLine()){
                inputData.add(scanObj.nextLine());
            }
            noOfProcesses = Integer.valueOf(inputData.get(0));
            processIds = Arrays.asList(inputData.get(1).split(" ")).stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList());
            graph = new HashMap<>();
            for(int i =0; i < noOfProcesses; i++){
                List<Integer> neighbors = Arrays.asList(inputData.get(i+2).split(" ")).stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList());
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
