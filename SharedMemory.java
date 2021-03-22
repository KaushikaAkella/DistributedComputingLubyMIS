import java.util.*;

public class SharedMemory {
    public static FileReader fileReader = new FileReader();
    public static int noOfProcesses = fileReader.noOfProcesses;
//    public static List<String> processState;
    public static Set<Integer> MIS = new HashSet<>();
    public static List<Integer> awake = new ArrayList<>();
//    public static List<Integer> pIds;
    public static List<Integer> winners = new ArrayList<>();
    public static List<Integer> losers = new ArrayList<>();
    public static Map<Integer,Integer> pIdMap = new HashMap<>(); // {0:0, 1:1, 2:2} - pid, pidindex
//    public static List<Integer> tempIds;
    public static Map<Integer, Node> processThread = new HashMap<>();
}
