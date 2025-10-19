import java.util.*;

public class Optimal {
    public static int simulate(String[] pages, int capacity) {
        Set<String> frames = new HashSet<>();
        List<String> frameList = new ArrayList<>();
        int pageFaults = 0;

        // Header with aligned columns
        System.out.printf("%-6s %-6s %-20s %-6s%n", "Step", "Page", "Frames", "Fault");
        for (int i = 0; i < pages.length; i++) {
            boolean fault = false;
            String page = pages[i];
            if (!frames.contains(page)) {
                if (frames.size() < capacity) {
                    frames.add(page);
                    frameList.add(page);
                } else {
                    int farthest = -1;
                    int replaceIndex = -1;
                    for (int j = 0; j < frameList.size(); j++) {
                        int nextUse = Integer.MAX_VALUE;
                        for (int k = i + 1; k < pages.length; k++) {
                            if (frameList.get(j).equals(pages[k])) {
                                nextUse = k;
                                break;
                            }
                        }
                        if (nextUse > farthest) {
                            farthest = nextUse;
                            replaceIndex = j;
                        }
                    }
                    // replace at replaceIndex
                    frames.remove(frameList.get(replaceIndex));
                    frameList.set(replaceIndex, page);
                    frames.add(page);
                }
                pageFaults++;
                fault = true;
            }

            // Build frames display preserving order
            StringBuilder fl = new StringBuilder();
            for (String f : frameList) {
                if (fl.length() > 0) fl.append(' ');
                fl.append(f);
            }
            System.out.printf("%-6d %-6s %-20s %-6s%n", (i+1), page, fl.toString(), (fault ? "Yes" : "No"));
        }

        // Print rates
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