import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("================================================================================================================");
        System.out.println("---------------------------------------------Choose Algorithm:--------------------------------------------------");
        System.out.println("================================================================================================================");
        System.out.println("1. SJF");
        System.out.println("2. Non-Preemptive Priority");
        System.out.println("3. Round Robin");
        System.out.println("4. SRTF");
        System.out.println("5. Optimal Page Replacement");
        System.out.println("6. LRU Page Replacement");
        System.out.println("================================================================================================================");
        System.out.print("Enter choice (1-6): ");
        try {
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("================================================================================================================");
                    runSJF(sc);
                    System.out.println("================================================================================================================");
                    break;
                case 2:
                    System.out.println("================================================================================================================");
                    runNonPreemptivePriority(sc);
                    System.out.println("================================================================================================================");
                    break;
                case 3:
                    System.out.println("================================================================================================================");
                    runRoundRobin(sc);
                    System.out.println("================================================================================================================");
                    break;
                case 4:
                    System.out.println("================================================================================================================");
                    runSRTF(sc);
                    System.out.println("================================================================================================================");
                    break;
                case 5:
                    System.out.println("================================================================================================================");
                    runOptimal(sc);
                    System.out.println("================================================================================================================");
                    break;
                case 6:
                    System.out.println("================================================================================================================");
                    runLRU(sc);
                    System.out.println("================================================================================================================");
                    break;
                default:
                    System.out.println("================================================================================================================");
                    System.out.println("Invalid choice");
                    System.out.println("================================================================================================================");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
        }
        sc.close();
    }

    private static void runSJF(Scanner sc) {
        try {
            System.out.print("Enter number of processes: ");
            int n = sc.nextInt();
            System.out.println("================================================================================================================");
            System.out.println("----------------------------------------------------------------------------------------------------------------");
            List<Process> processes = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                System.out.println("For Process P" + (i+1) + ":");
                System.out.print("Arrival Time: ");
                int at = sc.nextInt();
                System.out.print("CPU Cycle (Burst Time): ");
                int bt = sc.nextInt();
                int pr = 0; // Not used in SJF
                processes.add(new Process("P" + (i+1), at, bt, pr));
                System.out.println("----------------------------------------------------------------------------------------------------------------");
            }
            SJF.schedule(processes);
            System.out.println("================================================================================================================");
            SJF.printTable(processes);
            System.out.println("================================================================================================================");
            SJF.printGanttChart();
            System.out.println("================================================================================================================");
            SJF.calculateAverageTimes(processes);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid integers.");
        }
    }

    private static void runNonPreemptivePriority(Scanner sc) {
        try {
            System.out.print("Enter number of processes: ");
            int n = sc.nextInt();
            System.out.println("================================================================================================================");
            System.out.println("----------------------------------------------------------------------------------------------------------------");
            List<Process> processes = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                System.out.println("For Process P" + (i+1) + ":");
                System.out.print("Arrival Time: ");
                int at = sc.nextInt();
                System.out.print("CPU Cycle (Burst Time): ");
                int bt = sc.nextInt();
                System.out.print("Priority: ");
                int pr = sc.nextInt();
                processes.add(new Process("P" + (i+1), at, bt, pr));
                System.out.println("----------------------------------------------------------------------------------------------------------------");
            }
            System.out.println("================================================================================================================");
            NonPreemptivePriority.schedule(processes);
            System.out.println("================================================================================================================");
            NonPreemptivePriority.printTable(processes);
            System.out.println("================================================================================================================");
            NonPreemptivePriority.printGanttChart();
            System.out.println("================================================================================================================");
            NonPreemptivePriority.calculateAverageTimes(processes);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid integers.");
        }
    }

    private static void runRoundRobin(Scanner sc) {
        try {
            System.out.print("Enter number of processes: ");
            int n = sc.nextInt();
            System.out.println("================================================================================================================");
            System.out.println("----------------------------------------------------------------------------------------------------------------");
            List<Process> processes = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                System.out.println("For Process P" + (i+1) + ":");
                System.out.print("Arrival Time: ");
                int at = sc.nextInt();
                System.out.print("CPU Cycle (Burst Time): ");
                int bt = sc.nextInt();
                int pr = 0; // Not used in Round Robin
                processes.add(new Process("P" + (i+1), at, bt, pr));
                System.out.println("----------------------------------------------------------------------------------------------------------------");
            }
            System.out.print("Enter time quantum: ");
            int quantum = sc.nextInt();
            System.out.println("================================================================================================================");
            RoundRobin.schedule(processes, quantum);
            System.out.println("================================================================================================================");
            RoundRobin.printTable(processes);
            System.out.println("================================================================================================================");
            RoundRobin.printGanttChart();
            System.out.println("================================================================================================================");
            RoundRobin.calculateAverageTimes(processes);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid integers.");
        }
    }

    private static void runSRTF(Scanner sc) {
        try {
            System.out.print("Enter number of processes: ");
            int n = sc.nextInt();
            System.out.println("================================================================================================================");
            System.out.println("----------------------------------------------------------------------------------------------------------------");
            List<Process> processes = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                System.out.println("For Process P" + (i+1) + ":");
                System.out.print("Arrival Time: ");
                int at = sc.nextInt();
                System.out.print("CPU Cycle (Burst Time): ");
                int bt = sc.nextInt();
                int pr = 0; // Not used in SRTF
                processes.add(new Process("P" + (i+1), at, bt, pr));
                System.out.println("----------------------------------------------------------------------------------------------------------------");
            }
            System.out.println("================================================================================================================");
            SRTF.schedule(processes);
            System.out.println("================================================================================================================");
            SRTF.printTable(processes);
            System.out.println("================================================================================================================");
            SRTF.printGanttChart();
            System.out.println("================================================================================================================");
            SRTF.calculateAverageTimes(processes);
            System.out.println("================================================================================================================");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid integers.");
        }
    }

    private static void runOptimal(Scanner sc) {
        try {
            System.out.print("Enter number of frames (memory size): ");
            int capacity = sc.nextInt();
            System.out.print("Enter number of requested pages: ");
            int n = sc.nextInt();
            String[] pages = new String[n];
            for (int i = 0; i < n; i++) {
                System.out.print("Enter page " + (i+1) + " (single character): ");
                String s = sc.next();
                pages[i] = s.trim().toUpperCase();
                System.out.println("----------------------------------------------------------------------------------------------------------------");
            }
            System.out.println("================================================================================================================");
            Optimal.simulate(pages, capacity);
            System.out.println("================================================================================================================");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid integers.");
        }
    }

    private static void runLRU(Scanner sc) {
        try {
            System.out.print("Enter number of frames (memory size): ");
            int capacity = sc.nextInt();
            System.out.print("Enter number of requested pages: ");
            int n = sc.nextInt();
            String[] pages = new String[n];
            for (int i = 0; i < n; i++) {
                System.out.print("Enter page " + (i+1) + " (single character): ");
                String s = sc.next();
                pages[i] = s.trim().toUpperCase();
                System.out.println("----------------------------------------------------------------------------------------------------------------");
            }
            System.out.println("================================================================================================================");
            LRU.simulate(pages, capacity);
            System.out.println("================================================================================================================");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid integers.");
        }
    }
}