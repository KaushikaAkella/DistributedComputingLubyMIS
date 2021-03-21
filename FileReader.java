import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class FileReader{
    public static void main(String args[]){
        try{
            File fileObj = new File("input.txt");
            Scanner scanObj = new Scanner(fileObj);
            List<String> inputData = new ArrayList<>();
            while(scanObj.hasNextLine()){
                inputData.add(scanObj.nextLine());
            }
            int noOfProcesses = Integer.valueOf(inputData.get(0));
            List<Integer> processIds = Arrays.asList(inputData.get(1).split(" ")).stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList());
            Map<Integer, List<Integer>> graph = new HashMap<>();
            for(int i =0; i < noOfProcesses; i++){
                graph.put(processIds.get(i), Arrays.asList(inputData.get(i+2).split(" ")).stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList()));
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not found exception");
        }
    }
}
