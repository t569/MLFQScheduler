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


        // Sort by arrival time
        Collections.sort(allProcesses);
        List<Process> arrivalQueue = new ArrayList<>(allProcesses);

        System.out.println("Starting sim....");
        System.out.println("Time | PID | Queue | Action ");


        // Simulation loop
        while(completedProcess.size() < allProcesses.size())
        {
            // 1. Handle Arrivals
            // 2. Check Aging (Promote Starving Processes)
            // 3. Select Process (Dispatcher)
            // 4. Context Switch / Preemption Logic
            // 5. Execution Step
            // 6. Post-Execution Logic (Completion or Demotion)
        }
    }

    private void printMetrics()
    {
        System.out.println("\n Simulation Completed");
        System.out.println("---------------------------------------------------------------");
        System.out.printf("%-5s %-10s %-10s %-10s %-10s %-10s\n", "PID","Arr Time", "Burst", "Finish", "Wait", "Turn around");
        System.out.println("---------------------------------------------------------------");

        completedProcess.sort(Comparator.comparing(p -> p.getPid()));

        double totalWT = 0, totalTAT = 0;
        for(Process p: completedProcess)
        {
            System.out.printf("%-5s %-10d %-10d %-10d %-10d %-10d\n", p.getPid(),
                                                                        p.getArrivalTime(),
                                                                        p.getBurstTime(),
                                                                        p.getFinishTime(),
                                                                        p.getWaitingTime(),
                                                                        p.getTurnaroundTime());
            totalWT += p.getWaitingTime();
            totalTAT += p.getTurnaroundTime();

            System.out.println("---------------------------------------------------------------------------------");
            System.out.printf("Average Waiting Time: %.2f\n", totalWT / completedProcess.size());
            System.out.printf("Average Turnaround Time: %.2f\n", totalTAT / completedProcess.size());
        }
    }

}
