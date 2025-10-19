import java.util.*;

public class RoundRobin {
    public static List<String> ganttChart = new ArrayList<>();
    public static List<Integer> ganttTime = new ArrayList<>();

    public static void schedule(List<Process> processes, int timeQuantum) {
        ganttChart.clear();
        ganttTime.clear();
        // Do not mutate the original list order. Work on an arrival-sorted copy for enqueueing
        List<Process> arrivalOrder = new ArrayList<>(processes);
        arrivalOrder.sort(Comparator.comparingInt(Process::getArrivalTime));

        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        int n = processes.size();
        int completed = 0;

        // initialize fields
        for (Process p : processes) {
            p.setRemainingTime(p.getBurstTime());
            p.setTotalRunTime(0);
            p.setStartTime(-1);
            p.setCompletionTime(0);
            p.setWaitingTime(0);
            p.setTurnaroundTime(0);
        }

        int idx = 0; // index into arrivalOrder

        // start at earliest arrival
        int earliest = Integer.MAX_VALUE;
        for (Process p : arrivalOrder) earliest = Math.min(earliest, p.getArrivalTime());
        currentTime = Math.max(0, earliest);

        while (completed < n) {
            // enqueue all that have arrived
            while (idx < n && arrivalOrder.get(idx).getArrivalTime() <= currentTime) {
                queue.add(arrivalOrder.get(idx));
                idx++;
            }

            if (queue.isEmpty()) {
                // jump to next arrival if any
                if (idx < n) {
                    currentTime = arrivalOrder.get(idx).getArrivalTime();
                    continue;
                } else {
                    break;
                }
            }

            Process p = queue.poll();
            if (p.getStartTime() == -1) p.setStartTime(currentTime);

            int execTime = Math.min(timeQuantum, p.getRemainingTime());
            // Record Gantt segment start
            ganttChart.add(p.getName());
            ganttTime.add(currentTime);

            // execute
            p.setRemainingTime(p.getRemainingTime() - execTime);
            p.setTotalRunTime(p.getTotalRunTime() + execTime);
            currentTime += execTime;

            // add any newly arrived processes during execution
            while (idx < n && arrivalOrder.get(idx).getArrivalTime() <= currentTime) {
                queue.add(arrivalOrder.get(idx));
                idx++;
            }

            // Record Gantt segment end
            ganttTime.add(currentTime);

            if (p.getRemainingTime() == 0) {
                p.setCompletionTime(currentTime);
                p.setTurnaroundTime(p.getCompletionTime() - p.getArrivalTime());
                p.setWaitingTime(p.getTurnaroundTime() - p.getBurstTime());
                completed++;
            } else {
                // requeue
                queue.add(p);
            }
        }
    }

    public static void calculateAverageTimes(List<Process> processes) {
        double totalWaiting = 0, totalTurnaround = 0;
        for (Process p : processes) {
            totalWaiting += p.getWaitingTime();
            totalTurnaround += p.getTurnaroundTime();
        }
        System.out.printf("Average Waiting Time: %.2f%n", (totalWaiting / processes.size()));
        System.out.printf("Average Turnaround Time: %.2f%n", (totalTurnaround / processes.size()));
    }

    public static void printTable(List<Process> processes) {
        System.out.printf("%-8s %-8s %-10s %-8s %-8s %-8s %-12s%n", "Process", "Arrival", "CPU Cycle", "End Time", "Run Time", "TAT", "Waiting Time");
        // Print in P1..Pn order by parsing the numeric suffix of the process name
        List<Process> ordered = new ArrayList<>(processes);
        ordered.sort(Comparator.comparingInt(p -> {
            try { return Integer.parseInt(p.getName().replaceAll("[^0-9]", "")); }
            catch (Exception e) { return Integer.MAX_VALUE; }
        }));
        for (Process p : ordered) {
            System.out.printf("%-8s %-8d %-10d %-8d %-8d %-8.2f %-12.2f%n", p.getName(), p.getArrivalTime(), p.getBurstTime(), p.getCompletionTime(), p.getTotalRunTime(), (double)p.getTurnaroundTime(), (double)p.getWaitingTime());
        }
    }

    public static void printGanttChart() {
        System.out.println("Gantt Chart:");
        if (ganttChart.isEmpty() || ganttTime.isEmpty()) {
            System.out.println("(no chart)");
            return;
        }
        List<String> labels = new ArrayList<>();
        List<Integer> widths = new ArrayList<>();
        int segments = ganttChart.size();
        for (int i = 0; i < segments; i++) {
            String label = ganttChart.get(i);
            int start = ganttTime.get(2 * i);
            int end = ganttTime.get(2 * i + 1);
            int width = Math.max(3, end - start);
            labels.add(label);
            widths.add(width);
        }

        StringBuilder line1 = new StringBuilder();
        for (int i = 0; i < labels.size(); i++) {
            int w = widths.get(i) * 2;
            String label = " " + labels.get(i) + " ";
            int padding = Math.max(0, w - label.length());
            int left = padding / 2;
            int right = padding - left;
            line1.append("|");
            for (int j = 0; j < left; j++) line1.append(' ');
            line1.append(label);
            for (int j = 0; j < right; j++) line1.append(' ');
        }
        line1.append("|");

        StringBuilder line2 = new StringBuilder();
        for (int i = 0; i < labels.size(); i++) {
            int w = widths.get(i) * 2;
            line2.append("+");
            for (int j = 0; j < w; j++) line2.append('-');
        }
        line2.append("+");

        // Build boundary times: start0, end0, end1, ..., endN-1
        List<Integer> boundaries = new ArrayList<>();
        for (int i = 0; i < segments; i++) boundaries.add(ganttTime.get(2 * i));
        boundaries.add(ganttTime.get(2 * segments - 1));

        StringBuilder times = new StringBuilder();
        for (int i = 0; i < boundaries.size(); i++) {
            int t = boundaries.get(i);
            String ts = String.valueOf(t);
            int pos = 0;
            for (int k = 0; k < i; k++) pos += widths.get(k) * 2 + 1;
            while (times.length() < pos) times.append(' ');
            times.append(ts);
        }

        System.out.println(line1.toString());
        System.out.println(line2.toString());
        System.out.println(times.toString());
    }
}