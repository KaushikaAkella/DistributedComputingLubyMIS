import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedMemory {
    public static FileReader fileReader = new FileReader();
    public static int noOfProcesses = fileReader.noOfProcesses;
    public static List<String> processState;
    public static Set<Integer> MIS;
    public static List<Integer> awake;
    public static List<Integer> pIds;
    public static Map<Integer,Integer> pIdMap; // {0:0, 1:1, 2:2} - pid, pidindex
    public static List<Integer> tempIds;
}
