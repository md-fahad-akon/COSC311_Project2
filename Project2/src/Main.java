/**
 * Author Name: (1) Fahad Akon
 *              (2) Salma Ibrahim
 * Class:       COSC311
 * Assignment:  Prject 2
 * Date:        12/07/2025
 * Problem:     Union-Split-Find Data Structure with Performance Testing.
 * **/


import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Operations and theoretical running times in this implementation:
 *   - FIND (find(x))          : O(1)
 *   - CONNECTED (connected)   : O(1)
 *   - UNION (union(a, b))     : O(n)
 *   - SPLIT (split(x))        : O(1)
 */
public class Main {

    public static void main(String[] args) {
        runDemo();

        runTimingExperiment();
    }

    /**
     * Demonstrate all Union-Split-Find operations.
     */
    public static void runDemo() {
        System.out.println("============================================================");
        System.out.println("       UNION-SPLIT-FIND DATA STRUCTURE DEMONSTRATION");
        System.out.println("============================================================\n");

        // Create structure with 10 elements (0 to 9)
        UnionSplitFind ds = new UnionSplitFind(10);

        System.out.println("Created structure with 10 elements (0-9).");
        System.out.println("Initial state (each element in its own set):");
        ds.printSets();

        // FIND Operation
        System.out.println("\n------------------------------------------------------------");
        System.out.println("FIND OPERATION - Get set ID of an element");
        System.out.println("------------------------------------------------------------");
        System.out.println("  find(0) = " + ds.find(0));
        System.out.println("  find(5) = " + ds.find(5));
        System.out.println("  find(9) = " + ds.find(9));

        // UNION Operation
        System.out.println("\n------------------------------------------------------------");
        System.out.println("UNION OPERATION - Merge sets containing two elements");
        System.out.println("------------------------------------------------------------");
        System.out.println("Performing: union(0, 1), union(1, 2), union(3, 4), union(5, 6)");
        ds.union(0, 1);  // merge {0} and {1}
        ds.union(1, 2);  // merge {0,1} and {2}
        ds.union(3, 4);  // merge {3} and {4}
        ds.union(5, 6);  // merge {5} and {6}

        System.out.println("\nAfter unions:");
        ds.printSets();

        System.out.println("\nChecking connectivity:");
        System.out.println("  connected(0, 2) = " + ds.connected(0, 2) + " (same set)");
        System.out.println("  connected(3, 4) = " + ds.connected(3, 4) + " (same set)");
        System.out.println("  connected(0, 5) = " + ds.connected(0, 5) + " (different sets)");

        System.out.println("\nPerforming: union(2, 3) - merging {0,1,2} with {3,4}");
        ds.union(2, 3);

        System.out.println("\nAfter union(2, 3):");
        ds.printSets();

        // SPLIT Operation
        System.out.println("\n------------------------------------------------------------");
        System.out.println("SPLIT OPERATION - Move element to its own new set");
        System.out.println("------------------------------------------------------------");
        System.out.println("Performing: split(3) (move element 3 to its own set)");

        ds.split(3);

        System.out.println("\nAfter split(3):");
        ds.printSets();

        System.out.println("\nPerforming: split(5) (move element 5 to its own set)");
        ds.split(5);

        System.out.println("\nAfter split(5):");
        ds.printSets();

        System.out.println("\nDemo complete.\n");
    }

    /**
     * Timing experiment – measures running time vs input size.
     * Exports data to CSV for XY plot: benchmark_data.csv
     *
     * CSV columns:
     *   Size, Find_ms, Union_ms, Split_ms, Total_ms
     */
    public static void runTimingExperiment() {
        System.out.println("\n\n============================================================");
        System.out.println("              PERFORMANCE TIMING EXPERIMENT");
        System.out.println("============================================================\n");

        int[] sizes = {1_000, 2_000, 5_000, 10_000, 20_000, 50_000, 100_000};
        int operations = 10_000; // number of operations for each type
        Random rand = new Random(42);

        System.out.printf("%-10s %-15s %-15s %-15s %-15s%n",
                "Size", "Find (ms)", "Union (ms)", "Split (ms)", "Total (ms)");
        System.out.println("------------------------------------------------------------");

        try (PrintWriter writer = new PrintWriter(new FileWriter("benchmark_data.csv"))) {
            writer.println("Size,Find_ms,Union_ms,Split_ms,Total_ms");

            for (int n : sizes) {

                // ----------------- Benchmark FIND -----------------
                UnionSplitFind dsFind = new UnionSplitFind(n);
                long start = System.nanoTime();
                for (int i = 0; i < operations; i++) {
                    dsFind.find(rand.nextInt(n));
                }
                double findTime = (System.nanoTime() - start) / 1_000_000.0;

                // ----------------- Benchmark UNION ----------------
                UnionSplitFind dsUnion = new UnionSplitFind(n);
                start = System.nanoTime();
                for (int i = 0; i < operations; i++) {
                    int a = rand.nextInt(n);
                    int b = rand.nextInt(n);
                    dsUnion.union(a, b);
                }
                double unionTime = (System.nanoTime() - start) / 1_000_000.0;

                // ----------------- Benchmark SPLIT ----------------
                UnionSplitFind dsSplit = new UnionSplitFind(n);
                start = System.nanoTime();
                for (int i = 0; i < operations; i++) {
                    int x = rand.nextInt(n);
                    dsSplit.split(x);
                }
                double splitTime = (System.nanoTime() - start) / 1_000_000.0;

                double totalTime = findTime + unionTime + splitTime;

                // Print results
                System.out.printf("%-10d %-15.3f %-15.3f %-15.3f %-15.3f%n",
                        n, findTime, unionTime, splitTime, totalTime);

                // Write to CSV
                writer.printf("%d,%.3f,%.3f,%.3f,%.3f%n",
                        n, findTime, unionTime, splitTime, totalTime);
            }

            System.out.println("\nData exported to: benchmark_data.csv");

        } catch (Exception e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }
}
