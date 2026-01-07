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

            // move from the arrival list to queue0 ( highest priority)

            Iterator<Process> arrivalIterator = arrivalQueue.iterator();
            while (arrivalIterator.hasNext())
            {
                Process p = arrivalIterator.next();
                if(p.getArrivalTime() == globalTime)
                {
                    queue0.add(p);
                    System.out.printf("%4d | %s | Q0 | Arrived\n", globalTime, p.getPid());
                    arrivalIterator.remove();
                }
            }

            // 2. Check Aging (Promote Starving Processes)
            // if any process has aged move to Q0
            checkAging(queue1);
            checkAging(queue2);


            // 3. Select Process (Dispatcher)
            Process nextProcess = null;
            if(! queue0.isEmpty()) nextProcess = queue0.peek();
            else if (! queue1.isEmpty()) nextProcess = queue1.peek();
            else if (!queue2.isEmpty()) nextProcess = queue2.peek();


            // 4. Context Switch / Preemption Logic
            if (currentProcess != null && currentProcess != nextProcess)
            {
                if (currentProcess.getRemainingTime() > 0){
                    // the process was preempted put it back in the appropriate queue
                    int priorityLevel = currentProcess.getPriorityLevel();

                    switch(priorityLevel){
                        case 0:
                            queue0.add(currentProcess);
                            break;
                        case 1:
                            queue1.add(currentProcess);
                            break;
                        case 2:
                            queue2.add(currentProcess);
                            break;
                    }
                }

                // Reset the quantum counter
                currentProcessQuantumUsed = 0;
            }
            // 5. Execution Step
            if(nextProcess != null)
            {
                if(nextProcess.getPriorityLevel() == 0) queue0.poll();
                else if(nextProcess.getPriorityLevel() == 1) queue1.poll();
                else queue2.poll();

                currentProcess = nextProcess;

                // Record time of execution
                if(!currentProcess.hasStarted())
                {
                    currentProcess.setStartTime(globalTime);
                    currentProcess.setResponseTime(globalTime - currentProcess.getArrivalTime());
                    currentProcess.setHasStarted(true);
                }

                // execute the process for 1 tick
                currentProcess.decrementRemainingTime();
                currentProcessQuantumUsed++;

                updateWaitingTimes();
            }
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

    private void checkAging(Queue<Process> queue)
    {
        Iterator<Process> it = queue.iterator();
        while(it.hasNext()){
            Process p = it.next();
            // to check if p is greater than aging threshold
            if(p.getTimeInCurrentQueue()>agingThreshold){
                // it should be removed
                it.remove();
                //then the priority should be rest
                p.setTimeInCurrentQueue(0);
                // set priority level to
                p.setPriorityLevel(0);
                //add the process to queue 0
                queue0.add(p);

                System.out.printf("%4d | %s | %s | Aged -> promoted to Q0\n", globalTime, p.getPid(), (queue == queue1? "Q1" : "Q2"));
            }
        }

    }

    private void updateWaitingTimes()
    {
        // for all processes in all the queues increment their time in current queue
        // for all processes in all queues increment waiting time
        for(Process p : queue0){
            p.setTimeInCurrentQueue(p.getTimeInCurrentQueue()+1);
            p.setWaitingTime(p.getWaitingTime()+1);
        }
        for(Process p: queue1){
            p.setTimeInCurrentQueue(p.getTimeInCurrentQueue()+1);
            p.setWaitingTime(p.getWaitingTime()+1);
        }
        for(Process p : queue2){
            p.setTimeInCurrentQueue(p.getTimeInCurrentQueue()+1);
            p.setWaitingTime(p.getWaitingTime()+1);
        }

    }
}
