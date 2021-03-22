import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//global variables
// MIS_set = {}
// awake = [T, T, T, T, T]
// processStatus = [winner, loser, undecided]

public class MasterThread extends Thread {
    List<Integer> processStatus = new ArrayList<>();
    public static Map<Integer,Integer> pIdMap; // {0:0, 1:1, 2:2} - pid, pidindex
    int phase = 0;
}
