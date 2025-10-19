import java.util.*;

public class SRTF {
    public static List<String> ganttChart = new ArrayList<>();
    public static List<Integer> ganttTime = new ArrayList<>();

    public static void schedule(List<Process> processes) {
        ganttChart.clear();
        ganttTime.clear();
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
        boolean[] isCompleted = new boolean[n];
        // initialize per-process fields
        for (Process p : processes) {
            p.setRemainingTime(p.getBurstTime());
            p.setTotalRunTime(0);
            p.setStartTime(-1);
            p.setCompletionTime(0);
            p.setWaitingTime(0);
            p.setTurnaroundTime(0);
        }

        // Priority queue orders by remaining time, then arrival time, then burst time
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> {
            Process pa = processes.get(a);
            Process pb = processes.get(b);
            if (pa.getRemainingTime() != pb.getRemainingTime()) return Integer.compare(pa.getRemainingTime(), pb.getRemainingTime());
            if (pa.getArrivalTime() != pb.getArrivalTime()) return Integer.compare(pa.getArrivalTime(), pb.getArrivalTime());
            if (pa.getBurstTime() != pb.getBurstTime()) return Integer.compare(pa.getBurstTime(), pb.getBurstTime());
            return pa.getName().compareTo(pb.getName());
        });

        boolean[] added = new boolean[n];
        int lastIdx = -1;

        // start at earliest arrival if not zero
        int earliest = Integer.MAX_VALUE;
        for (Process p : processes) earliest = Math.min(earliest, p.getArrivalTime());
        currentTime = Math.max(0, earliest);

        while (completed < n) {
            // add all processes that have arrived by currentTime
            for (int i = 0; i < n; i++) {
                if (!added[i] && processes.get(i).getArrivalTime() <= currentTime) {
                    pq.add(i);
                    added[i] = true;
                }
            }

            if (pq.isEmpty()) {
                // jump to next arrival
                int next = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) if (!added[i]) next = Math.min(next, processes.get(i).getArrivalTime());
                if (next == Integer.MAX_VALUE) break; // nothing left
                currentTime = next;
                lastIdx = -1;
                continue;
            }

            int idx = pq.peek();
            Process p = processes.get(idx);

            if (p.getStartTime() == -1) p.setStartTime(currentTime);

            if (lastIdx != idx) {
                // close previous segment if any
                if (lastIdx != -1) {
                    ganttTime.add(currentTime);
                }
                // start new segment
                ganttChart.add(p.getName());
                ganttTime.add(currentTime);
                lastIdx = idx;
            }

            // execute one time unit
            pq.poll(); // remove to update remaining time
            p.setRemainingTime(p.getRemainingTime() - 1);
            p.setTotalRunTime(p.getTotalRunTime() + 1);
            currentTime++;

            // add any newly arrived processes at this new currentTime before re-adding current
            for (int i = 0; i < n; i++) {
                if (!added[i] && processes.get(i).getArrivalTime() <= currentTime) {
                    pq.add(i);
                    added[i] = true;
                }
            }

            if (p.getRemainingTime() == 0) {
                p.setCompletionTime(currentTime);
                p.setTurnaroundTime(p.getCompletionTime() - p.getArrivalTime());
                p.setWaitingTime(p.getTurnaroundTime() - p.getBurstTime());
                isCompleted[idx] = true;
                completed++;
                // close this segment
                ganttTime.add(currentTime);
                lastIdx = -1; // next loop will start new segment
            } else {
                // re-insert with updated remaining time
                pq.add(idx);
            }
        }

        // final boundary for Gantt chart
        ganttTime.add(currentTime);
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