/**
 * Process class represents a process in the operating system scheduler.
 * This class implements the Comparable interface to enable natural ordering
 * based on arrival time, which is essential for scheduling algorithms.
 * The class maintains both static attributes (process characteristics that don't change)
 * and dynamic state variables (values that change during process execution and scheduling).
 */
public class Process implements Comparable<Process> {

    // ====================== STATIC ATTRIBUTES ======================
    /** Unique identifier for the process */
    private String pid;
    /** Time at which the process arrives in the system */
    private int arrivalTime;
    /** Total CPU time required to complete the process */
    private int burstTime;
    // ====================== DYNAMIC STATE VARIABLES ======================
    /** Remaining CPU time needed for process completion */
    private int remainingTime;
    /** Total time spent waiting in ready queues */
    private int waitingTime;
    /** Total time from arrival to completion (turnaround = finishTime - arrivalTime) */
    private int turnaroundTime;
    /** Time from arrival to first CPU execution (response time) */
    private int responseTime;
    /** Time at which the process completes execution */
    private int finishTime;
    /** Current priority level (1 = highest priority, 3 = lowest priority)
     *  Used in multi-level queue scheduling */
    private int priorityLevel;
    /** Time spent in the current priority queue
     *  Used for aging and priority promotion/demotion logic */
    private int timeInCurrentQueue;

    /** Flag indicating whether the process has started executing at least once */
    private boolean hasStarted;

    public Process(String pid, int arrivalTime, int burstTime) {
        // Initialize static attributes
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;

        // Initialize dynamic state variables
        this.remainingTime = burstTime;      // Initially, all burst time remains
        this.priorityLevel = 0;             // Default priority level (0 = not yet assigned)
        this.hasStarted = false;            // Process hasn't executed yet
        this.waitingTime = 0;               // No waiting time initially
        this.timeInCurrentQueue = 0;        // No time spent in queues yet

        // These will be calculated during execution
        this.turnaroundTime = 0;
        this.responseTime = 0;
        this.finishTime = 0;
    }

    /**
     * Compares this process with another process based on arrival time.
     * This enables natural ordering of processes in collections like PriorityQueue.
     *
     * @param other The process to compare with
     * @return Negative if this arrives first, positive if other arrives first, 0 if equal
     */
    @Override
    public int compareTo(Process other) {
        // Compare arrival times for natural ordering
        return Integer.compare(this.arrivalTime, other.arrivalTime);
    }

    // ====================== GETTERS AND SETTERS ======================
    // Essential for accessing and modifying process state from scheduler

    public String getPid() { return pid; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getRemainingTime() { return remainingTime; }
    public int getWaitingTime() { return waitingTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    public int getResponseTime() { return responseTime; }
    public int getFinishTime() { return finishTime; }
    public int getPriorityLevel() { return priorityLevel; }
    public int getTimeInCurrentQueue() { return timeInCurrentQueue; }
    public boolean hasStarted() { return hasStarted; }

    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }
    public void setResponseTime(int responseTime) { this.responseTime = responseTime; }
    public void setFinishTime(int finishTime) { this.finishTime = finishTime; }
    public void setPriorityLevel(int priorityLevel) { this.priorityLevel = priorityLevel; }
    public void setTimeInCurrentQueue(int timeInCurrentQueue) { this.timeInCurrentQueue = timeInCurrentQueue; }
    public void setHasStarted(boolean hasStarted) { this.hasStarted = hasStarted; }

    /**
     * Updates process state when it executes for a time quantum.
     *
     * @param timeQuantum Amount of CPU time allocated
     * @param currentTime Current system time
     * @return true if process completes, false otherwise
     */
    public boolean execute(int timeQuantum, int currentTime) {
        // Set response time if this is the first execution
        if (!hasStarted) {
            this.responseTime = currentTime - arrivalTime;
            this.hasStarted = true;
        }

        // Calculate actual execution time (don't exceed remaining time)
        int executionTime = Math.min(timeQuantum, remainingTime);

        // Update remaining time
        remainingTime -= executionTime;

        // Check if process has completed
        boolean isCompleted = (remainingTime == 0);

        if (isCompleted) {
            this.finishTime = currentTime + executionTime;
            this.turnaroundTime = finishTime - arrivalTime;
        }

        return isCompleted;
    }

    /**
     * Updates waiting time when process is in ready queue but not executing.
     *
     * @param timeIncrement Amount of time to add to waiting time
     */
    public void incrementWaitingTime(int timeIncrement) {
        this.waitingTime += timeIncrement;
        this.timeInCurrentQueue += timeIncrement;
    }

    /**
     * Resets time spent in current queue when priority changes.
     */
    public void resetTimeInCurrentQueue() {
        this.timeInCurrentQueue = 0;
    }

    /**
     * Returns a string representation of the process for debugging and logging.
     *
     * @return Formatted string containing key process information
     */
    @Override
    public String toString() {
        return String.format("Process[%s] Arrival:%d Burst:%d Remaining:%d Priority:%d",
                pid, arrivalTime, burstTime, remainingTime, priorityLevel);
    }
}