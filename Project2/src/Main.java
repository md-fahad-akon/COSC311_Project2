/**
 * Main.java
 * 
 * Demo and entry point for the Union-Split-Find Data Structure project.
 * 
 * This class demonstrates:
 *   - Creating ordered sets
 *   - FIND operation
 *   - SPLIT operation
 *   - UNION operation
 *   - Performance characteristics
 * 
 * @author [Your Names Here]
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║       UNION-SPLIT-FIND DATA STRUCTURE DEMONSTRATION          ║");
        System.out.println("║                   Using Treap Implementation                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        
        // Run demo based on command line argument
        if (args.length > 0) {
            switch (args[0]) {
                case "demo" -> runInteractiveDemo();
                case "test" -> runTests();
                case "benchmark" -> runBenchmarks();
                case "all" -> {
                    runInteractiveDemo();
                    runTests();
                    runBenchmarks();
                }
                default -> printUsage();
            }
        } else {
            // Default: run the interactive demo
            runInteractiveDemo();
        }
    }
    
    private static void printUsage() {
        System.out.println("\nUsage: java Main [option]");
        System.out.println("Options:");
        System.out.println("  demo      - Run interactive demonstration");
        System.out.println("  test      - Run correctness tests");
        System.out.println("  benchmark - Run performance benchmarks");
        System.out.println("  all       - Run everything");
        System.out.println("\nDefault (no args): runs demo");
    }
    
    private static void runInteractiveDemo() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("PART 1: BASIC OPERATIONS DEMO");
        System.out.println("═".repeat(60));
        
        // Create a new set
        System.out.println("\nCreating a new ordered set and adding elements 1-10...\n");
        UnionSplitFind mySet = new UnionSplitFind("MySet");
        
        for (int i = 1; i <= 10; i++) {
            mySet.add(i);
            System.out.println("  Added " + i + " → " + mySet);
        }
        
        // Demonstrate FIND
        System.out.println("\n" + "─".repeat(60));
        System.out.println("▶ FIND Operation Demo\n");
        
        int[] searchKeys = {5, 7, 11, 0};
        for (int key : searchKeys) {
            boolean found = mySet.find(key);
            System.out.println("  find(" + key + ") = " + found + 
                (found ? " ✓ Element exists" : " ✗ Element not found"));
        }
        
        // Demonstrate SPLIT
        System.out.println("\n" + "─".repeat(60));
        System.out.println("▶ SPLIT Operation Demo\n");
        
        System.out.println("  Before split: " + mySet);
        System.out.println("  Splitting at key = 5...\n");
        
        UnionSplitFind[] splitResult = mySet.split(5);
        UnionSplitFind leftPart = splitResult[0];
        UnionSplitFind rightPart = splitResult[1];
        
        leftPart.setName("LeftPart");
        rightPart.setName("RightPart");
        
        System.out.println("  Left part (≤ 5):  " + leftPart);
        System.out.println("  Right part (> 5): " + rightPart);
        
        // Demonstrate UNION
        System.out.println("\n" + "─".repeat(60));
        System.out.println("▶ UNION Operation Demo\n");
        
        System.out.println("  Reuniting the split parts...\n");
        
        UnionSplitFind reunited = UnionSplitFind.union(leftPart, rightPart);
        reunited.setName("ReunitedSet");
        
        System.out.println("  Result: " + reunited);
        System.out.println("  Structure is valid: " + reunited.isValid());
        
        // More complex demo
        System.out.println("\n" + "═".repeat(60));
        System.out.println("PART 2: ADVANCED OPERATIONS DEMO");
        System.out.println("═".repeat(60));
        
        // Create two separate sets
        System.out.println("\n▶ Creating two disjoint sets for union...\n");
        
        UnionSplitFind setA = new UnionSplitFind("SetA");
        UnionSplitFind setB = new UnionSplitFind("SetB");
        
        int[] elementsA = {2, 4, 6, 8, 10};
        int[] elementsB = {21, 23, 25, 27, 29};
        
        for (int e : elementsA) setA.add(e);
        for (int e : elementsB) setB.add(e);
        
        System.out.println("  " + setA);
        System.out.println("  " + setB);
        
        System.out.println("\n▶ Performing UNION (SetA ∪ SetB)...\n");
        
        UnionSplitFind unionResult = setA.union(setB);
        System.out.println("  Result: " + unionResult);
        
        // Multiple splits
        System.out.println("\n" + "─".repeat(60));
        System.out.println("▶ Multiple SPLIT Operations\n");
        
        UnionSplitFind numbers = new UnionSplitFind("Numbers");
        for (int i = 1; i <= 20; i++) {
            numbers.add(i);
        }
        
        System.out.println("  Original: " + numbers);
        System.out.println("\n  Splitting into thirds...\n");
        
        UnionSplitFind[] firstSplit = numbers.split(7);
        UnionSplitFind third1 = firstSplit[0];
        third1.setName("First Third");
        
        UnionSplitFind[] secondSplit = firstSplit[1].split(14);
        UnionSplitFind third2 = secondSplit[0];
        UnionSplitFind third3 = secondSplit[1];
        third2.setName("Second Third");
        third3.setName("Third Third");
        
        System.out.println("  " + third1);
        System.out.println("  " + third2);
        System.out.println("  " + third3);
        
        // Show internal structure
        System.out.println("\n" + "═".repeat(60));
        System.out.println("PART 3: INTERNAL STRUCTURE VISUALIZATION");
        System.out.println("═".repeat(60));
        
        UnionSplitFind smallSet = new UnionSplitFind("SmallDemo");
        int[] demoElements = {5, 3, 8, 1, 4, 7, 9};
        smallSet.buildFromArray(demoElements);
        
        System.out.println("\n▶ Tree structure of set with elements " + 
                          java.util.Arrays.toString(demoElements) + ":\n");
        smallSet.printStructure();
        
        System.out.println("\n▶ In-order traversal (sorted): " + 
                          java.util.Arrays.toString(smallSet.toSortedArray()));
        
        // Summary
        System.out.println("\n" + "═".repeat(60));
        System.out.println("SUMMARY");
        System.out.println("═".repeat(60));
        System.out.println("\nThe Union-Split-Find data structure supports:");
        System.out.println("  • FIND:  O(log n) - Check if element exists");
        System.out.println("  • SPLIT: O(log n) - Divide set at a given value");
        System.out.println("  • UNION: O(log n) - Merge two ordered sets");
        System.out.println("  • INSERT/DELETE: O(log n) - Add/remove elements");
        System.out.println("\nImplemented using a Treap (randomized BST)");
        System.out.println("which maintains balance through random priorities.");
        
        System.out.println("\n" + "═".repeat(60));
        System.out.println("END OF DEMO");
        System.out.println("═".repeat(60));
    }
    
    private static void runTests() {
        System.out.println("\n▶ Running correctness tests...\n");
        PerformanceTester tester = new PerformanceTester();
        tester.runCorrectnessTests();
    }
    
    private static void runBenchmarks() {
        System.out.println("\n▶ Running performance benchmarks...\n");
        PerformanceTester tester = new PerformanceTester();
        tester.runPerformanceBenchmarks();
        tester.exportBenchmarkData("results/benchmark_data.csv");
    }
}