package MLFQScheduler;


import java.util.*;
public class MLFQSchedulerEngine {
    private int globalTime = 0;
    private int quantumQ0 = 4;
    private int quantumQ1 = 8;
    private int agingThreshold = 30;    // Starvation threshold



    // Priority Queues
    private Queue<Process> queue0 = new LinkedList<>(); // Implements Round Robin
    private Queue<Process> queue1 = new LinkedList<>(); // Also implements Round Robin
    private Queue<Process> queue2 = new LinkedList<>(); // Implements a FCFS

    // progress shii
    private List<Process> completedProcess = new ArrayList<>();
    private List<Process> ganttChart = new ArrayList<>();      // this is to track execution order


    // tracks current CPU state
    private Process currentProcess = null;
    private int currentProcessQuantumUsed = 0;


    public void run(List<Process> allProcesses){
        System.out.println("Running scheduler!!");
    }

}
