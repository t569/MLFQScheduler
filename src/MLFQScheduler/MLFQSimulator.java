package MLFQScheduler;
import java.util.*;


public class MLFQSimulator {

    public static void main(String [] args){

        // Validation Test Case (Matches "Truth Table" from report)
        List<Process> processes = new ArrayList<>();

        // P1: Arrives 0, Burst 10 (High Priority start)
        processes.add(new Process("P1", 0, 10));
        // P2: Arrives 4, Burst 4
        processes.add(new Process("P2", 4, 4));
        // P3: Arrives 5, Burst 2
        processes.add(new Process("P3", 5, 2));

        // To test Aging, add a long running process and a late arrival
        // processes.add(new Process("P_Starved", 0, 100));

        MLFQSchedulerEngine scheduler = new MLFQSchedulerEngine();
        scheduler.run(processes);
    }

}
