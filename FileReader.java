import java.util.*;
public class FileReader{
    public static void main(String args[]){
        File fileObj = new File("input.txt");
        Scanner scanObj = new Scanner(fileObj);
        List<String> inputData = new ArrayList<>();
        while(scanObj.hasNextLine()){
            inputData.append(scanObj.nextLine());
        }
        int noOfProcesses = Integer.ValueOf(inputData.get(0));
    }
}
