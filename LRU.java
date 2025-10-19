import java.util.*;

public class LRU {
    public static int simulate(String[] pages, int capacity) {
        // displayFrames preserves the visual order and is NOT updated on hits
        LinkedHashSet<String> displayFrames = new LinkedHashSet<>();
        // recency tracks usage for eviction (true LRU): most recent at the end
        LinkedList<String> recency = new LinkedList<>();
        int pageFaults = 0;

        System.out.printf("%-6s %-6s %-20s %-6s%n", "Step", "Page", "Frames", "Fault");
        for (int i = 0; i < pages.length; i++) {
            boolean fault = false;
            String page = pages[i];

            boolean isHit = recency.contains(page);
            if (!isHit) {
                // page fault
                if (displayFrames.size() == capacity) {
                    // evict least recently used according to recency list
                    String evict = recency.removeFirst();
                    displayFrames.remove(evict);
                }
                // add new page to both structures
                displayFrames.add(page);
                recency.addLast(page);
                pageFaults++;
                fault = true;
            } else {
                // hit: update recency (move to most recent) but do NOT change displayFrames order
                recency.remove(page);
                recency.addLast(page);
            }

            StringBuilder fl = new StringBuilder();
            for (String f : displayFrames) {
                if (fl.length() > 0) fl.append(' ');
                fl.append(f);
            }
            System.out.printf("%-6d %-6s %-20s %-6s%n", (i+1), page, fl.toString(), (fault ? "Yes" : "No"));
        }

        double faultRate = (double) pageFaults / pages.length;
        double successRate = 1.0 - faultRate;
        System.out.println();
        System.out.printf("Total Page Faults: %d out of %d%n", pageFaults, pages.length);
        System.out.printf("Fault Rate: %.2f%%  Success Rate: %.2f%%%n", faultRate * 100.0, successRate * 100.0);
        return pageFaults;
    }

    public static void main(String[] args) {
        // This will be called from Main
    }
}