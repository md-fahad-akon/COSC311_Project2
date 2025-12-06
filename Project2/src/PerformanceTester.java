import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

/**
 * PerformanceTester.java
 * 
 * Comprehensive testing and benchmarking suite for the Union-Split-Find data structure.
 * 
 * Features:
 *   - Correctness testing
 *   - Performance benchmarking
 *   - Timing analysis across varying input sizes
 *   - CSV export for plotting
 * 
 * This class generates the data needed for the XY plot showing
 * how running time changes as input size grows.
 * 
 * @author [Your Names Here]
 */
public class PerformanceTester {
    
    private Random random;
    private static final int WARMUP_ITERATIONS = 100;
    private static final int TEST_ITERATIONS = 1000;
    
    public PerformanceTester() {
        this.random = new Random(42); // Fixed seed for reproducibility
    }
    
    public PerformanceTester(long seed) {
        this.random = new Random(seed);
    }
    
    // ==================== CORRECTNESS TESTS ====================
    
    /**
     * Runs all correctness tests
     * @return true if all tests pass
     */
    public boolean runCorrectnessTests() {
        System.out.println("=" .repeat(60));
        System.out.println("CORRECTNESS TESTS");
        System.out.println("=".repeat(60));
        
        boolean allPassed = true;
        
        allPassed &= testFind();
        allPassed &= testInsertAndDelete();
        allPassed &= testSplit();
        allPassed &= testUnion();
        allPassed &= testSplitThenUnion();
        allPassed &= testLargeDataset();
        
        System.out.println("\n" + "=".repeat(60));
        if (allPassed) {
            System.out.println("ALL CORRECTNESS TESTS PASSED ✓");
        } else {
            System.out.println("SOME TESTS FAILED ✗");
        }
        System.out.println("=".repeat(60));
        
        return allPassed;
    }
    
    private boolean testFind() {
        System.out.print("\nTest FIND operation... ");
        
        UnionSplitFind set = new UnionSplitFind("TestSet");
        int[] elements = {5, 3, 8, 1, 9, 2, 7};
        set.buildFromArray(elements);
        
        // Test finding existing elements
        for (int elem : elements) {
            if (!set.find(elem)) {
                System.out.println("FAILED - couldn't find " + elem);
                return false;
            }
        }
        
        // Test finding non-existing elements
        int[] notPresent = {0, 4, 6, 10, 100};
        for (int elem : notPresent) {
            if (set.find(elem)) {
                System.out.println("FAILED - falsely found " + elem);
                return false;
            }
        }
        
        System.out.println("PASSED ✓");
        return true;
    }
    
    private boolean testInsertAndDelete() {
        System.out.print("Test INSERT and DELETE... ");
        
        UnionSplitFind set = new UnionSplitFind("TestSet");
        
        // Insert elements
        for (int i = 1; i <= 10; i++) {
            set.add(i);
        }
        
        if (set.size() != 10) {
            System.out.println("FAILED - wrong size after insert");
            return false;
        }
        
        // Delete some elements
        set.remove(5);
        set.remove(3);
        set.remove(8);
        
        if (set.size() != 7) {
            System.out.println("FAILED - wrong size after delete");
            return false;
        }
        
        if (set.find(5) || set.find(3) || set.find(8)) {
            System.out.println("FAILED - deleted elements still found");
            return false;
        }
        
        // Verify remaining elements
        int[] expected = {1, 2, 4, 6, 7, 9, 10};
        int[] actual = set.toSortedArray();
        
        if (!Arrays.equals(expected, actual)) {
            System.out.println("FAILED - wrong elements remaining");
            return false;
        }
        
        System.out.println("PASSED ✓");
        return true;
    }
    
    private boolean testSplit() {
        System.out.print("Test SPLIT operation... ");
        
        UnionSplitFind set = new UnionSplitFind("OriginalSet");
        for (int i = 1; i <= 10; i++) {
            set.add(i);
        }
        
        // Split at 5
        UnionSplitFind[] parts = set.split(5);
        UnionSplitFind left = parts[0];
        UnionSplitFind right = parts[1];
        
        // Verify left part contains {1, 2, 3, 4, 5}
        int[] expectedLeft = {1, 2, 3, 4, 5};
        int[] actualLeft = left.toSortedArray();
        
        if (!Arrays.equals(expectedLeft, actualLeft)) {
            System.out.println("FAILED - wrong left split");
            System.out.println("Expected: " + Arrays.toString(expectedLeft));
            System.out.println("Actual: " + Arrays.toString(actualLeft));
            return false;
        }
        
        // Verify right part contains {6, 7, 8, 9, 10}
        int[] expectedRight = {6, 7, 8, 9, 10};
        int[] actualRight = right.toSortedArray();
        
        if (!Arrays.equals(expectedRight, actualRight)) {
            System.out.println("FAILED - wrong right split");
            System.out.println("Expected: " + Arrays.toString(expectedRight));
            System.out.println("Actual: " + Arrays.toString(actualRight));
            return false;
        }
        
        // Verify both parts have valid structure
        if (!left.isValid() || !right.isValid()) {
            System.out.println("FAILED - invalid structure after split");
            return false;
        }
        
        System.out.println("PASSED ✓");
        return true;
    }
    
    private boolean testUnion() {
        System.out.print("Test UNION operation... ");
        
        UnionSplitFind left = new UnionSplitFind("LeftSet");
        UnionSplitFind right = new UnionSplitFind("RightSet");
        
        // Left set: {1, 2, 3, 4, 5}
        for (int i = 1; i <= 5; i++) {
            left.add(i);
        }
        
        // Right set: {10, 11, 12, 13, 14}
        for (int i = 10; i <= 14; i++) {
            right.add(i);
        }
        
        // Union
        UnionSplitFind merged = left.union(right);
        
        // Verify merged set
        int[] expected = {1, 2, 3, 4, 5, 10, 11, 12, 13, 14};
        int[] actual = merged.toSortedArray();
        
        if (!Arrays.equals(expected, actual)) {
            System.out.println("FAILED - wrong merged result");
            return false;
        }
        
        if (!merged.isValid()) {
            System.out.println("FAILED - invalid structure after union");
            return false;
        }
        
        System.out.println("PASSED ✓");
        return true;
    }
    
    private boolean testSplitThenUnion() {
        System.out.print("Test SPLIT then UNION (roundtrip)... ");
        
        // Create original set
        int[] original = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        UnionSplitFind set = new UnionSplitFind("Original");
        set.buildFromArray(original);
        
        // Split at various points and reunite
        for (int splitPoint = 0; splitPoint <= 11; splitPoint++) {
            UnionSplitFind testSet = new UnionSplitFind("Test");
            testSet.buildFromArray(original);
            
            UnionSplitFind[] parts = testSet.split(splitPoint);
            UnionSplitFind reunited = UnionSplitFind.union(parts[0], parts[1]);
            
            int[] result = reunited.toSortedArray();
            if (!Arrays.equals(original, result)) {
                System.out.println("FAILED at split point " + splitPoint);
                return false;
            }
        }
        
        System.out.println("PASSED ✓");
        return true;
    }
    
    private boolean testLargeDataset() {
        System.out.print("Test with large dataset (10000 elements)... ");
        
        UnionSplitFind set = new UnionSplitFind("LargeSet");
        int n = 10000;
        
        // Insert random elements
        int[] elements = new int[n];
        for (int i = 0; i < n; i++) {
            elements[i] = random.nextInt(1000000);
            set.add(elements[i]);
        }
        
        // Verify all elements can be found
        for (int elem : elements) {
            if (!set.find(elem)) {
                System.out.println("FAILED - element not found");
                return false;
            }
        }
        
        // Test split and union
        UnionSplitFind[] parts = set.split(500000);
        UnionSplitFind reunited = UnionSplitFind.union(parts[0], parts[1]);
        
        if (!reunited.isValid()) {
            System.out.println("FAILED - invalid structure");
            return false;
        }
        
        System.out.println("PASSED ✓");
        return true;
    }
    
    // ==================== PERFORMANCE BENCHMARKS ====================
    
    /**
     * Runs comprehensive performance benchmarks
     */
    public void runPerformanceBenchmarks() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PERFORMANCE BENCHMARKS");
        System.out.println("=".repeat(60));
        
        // Input sizes to test
        int[] sizes = {100, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000};
        
        System.out.println("\nWarming up JVM...");
        warmup();
        
        System.out.println("\n--- FIND Operation Benchmark ---");
        benchmarkFind(sizes);
        
        System.out.println("\n--- INSERT Operation Benchmark ---");
        benchmarkInsert(sizes);
        
        System.out.println("\n--- DELETE Operation Benchmark ---");
        benchmarkDelete(sizes);
        
        System.out.println("\n--- SPLIT Operation Benchmark ---");
        benchmarkSplit(sizes);
        
        System.out.println("\n--- UNION Operation Benchmark ---");
        benchmarkUnion(sizes);
    }
    
    private void warmup() {
        UnionSplitFind warmupSet = new UnionSplitFind();
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            warmupSet.add(random.nextInt(10000));
        }
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            warmupSet.find(random.nextInt(10000));
        }
    }
    
    private void benchmarkFind(int[] sizes) {
        System.out.printf("%-12s %-15s %-15s%n", "Size", "Total (ms)", "Avg (μs)");
        System.out.println("-".repeat(45));
        
        for (int size : sizes) {
            UnionSplitFind set = new UnionSplitFind();
            for (int i = 0; i < size; i++) {
                set.add(i);
            }
            
            long startTime = System.nanoTime();
            for (int i = 0; i < TEST_ITERATIONS; i++) {
                set.find(random.nextInt(size));
            }
            long endTime = System.nanoTime();
            
            double totalMs = (endTime - startTime) / 1_000_000.0;
            double avgUs = (endTime - startTime) / 1000.0 / TEST_ITERATIONS;
            
            System.out.printf("%-12d %-15.3f %-15.3f%n", size, totalMs, avgUs);
        }
    }
    
    private void benchmarkInsert(int[] sizes) {
        System.out.printf("%-12s %-15s %-15s%n", "Size", "Total (ms)", "Avg (μs)");
        System.out.println("-".repeat(45));
        
        for (int size : sizes) {
            long totalTime = 0;
            
            for (int trial = 0; trial < 10; trial++) {
                UnionSplitFind set = new UnionSplitFind();
                
                long startTime = System.nanoTime();
                for (int i = 0; i < size; i++) {
                    set.add(random.nextInt(size * 10));
                }
                long endTime = System.nanoTime();
                
                totalTime += (endTime - startTime);
            }
            
            double avgTotalMs = totalTime / 10.0 / 1_000_000.0;
            double avgPerInsertUs = totalTime / 10.0 / size / 1000.0;
            
            System.out.printf("%-12d %-15.3f %-15.3f%n", size, avgTotalMs, avgPerInsertUs);
        }
    }
    
    private void benchmarkDelete(int[] sizes) {
        System.out.printf("%-12s %-15s %-15s%n", "Size", "Total (ms)", "Avg (μs)");
        System.out.println("-".repeat(45));
        
        for (int size : sizes) {
            // Build set first
            UnionSplitFind set = new UnionSplitFind();
            int[] elements = new int[size];
            for (int i = 0; i < size; i++) {
                elements[i] = i;
                set.add(i);
            }
            
            // Shuffle for random deletion order
            shuffleArray(elements);
            
            long startTime = System.nanoTime();
            for (int i = 0; i < Math.min(TEST_ITERATIONS, size); i++) {
                set.remove(elements[i]);
            }
            long endTime = System.nanoTime();
            
            int ops = Math.min(TEST_ITERATIONS, size);
            double totalMs = (endTime - startTime) / 1_000_000.0;
            double avgUs = (endTime - startTime) / 1000.0 / ops;
            
            System.out.printf("%-12d %-15.3f %-15.3f%n", size, totalMs, avgUs);
        }
    }
    
    private void benchmarkSplit(int[] sizes) {
        System.out.printf("%-12s %-15s%n", "Size", "Avg Split (μs)");
        System.out.println("-".repeat(30));
        
        for (int size : sizes) {
            long totalTime = 0;
            int trials = 100;
            
            for (int trial = 0; trial < trials; trial++) {
                // Build fresh set
                UnionSplitFind set = new UnionSplitFind();
                for (int i = 0; i < size; i++) {
                    set.add(i);
                }
                
                int splitPoint = size / 2;
                
                long startTime = System.nanoTime();
                set.split(splitPoint);
                long endTime = System.nanoTime();
                
                totalTime += (endTime - startTime);
            }
            
            double avgUs = totalTime / 1000.0 / trials;
            System.out.printf("%-12d %-15.3f%n", size, avgUs);
        }
    }
    
    private void benchmarkUnion(int[] sizes) {
        System.out.printf("%-12s %-15s%n", "Size", "Avg Union (μs)");
        System.out.println("-".repeat(30));
        
        for (int size : sizes) {
            long totalTime = 0;
            int trials = 100;
            
            for (int trial = 0; trial < trials; trial++) {
                // Build two sets
                UnionSplitFind left = new UnionSplitFind();
                UnionSplitFind right = new UnionSplitFind();
                
                for (int i = 0; i < size / 2; i++) {
                    left.add(i);
                }
                for (int i = size / 2; i < size; i++) {
                    right.add(i);
                }
                
                long startTime = System.nanoTime();
                UnionSplitFind.union(left, right);
                long endTime = System.nanoTime();
                
                totalTime += (endTime - startTime);
            }
            
            double avgUs = totalTime / 1000.0 / trials;
            System.out.printf("%-12d %-15.3f%n", size, avgUs);
        }
    }
    
    // ==================== CSV EXPORT FOR PLOTTING ====================
    
    /**
     * Generates CSV data for plotting running time vs input size
     * @param filename Output filename
     */
    public void exportBenchmarkData(String filename) {
        System.out.println("\nExporting benchmark data...");
        
        int[] sizes = {100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000};
        
        // Create the file object
        java.io.File file = new java.io.File(filename);
        
        // Create parent directory if needed
        java.io.File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Header
            writer.println("Size,Find_us,Insert_us,Delete_us,Split_us,Union_us");
            
            for (int size : sizes) {
                double findTime = measureFindTime(size);
                double insertTime = measureInsertTime(size);
                double deleteTime = measureDeleteTime(size);
                double splitTime = measureSplitTime(size);
                double unionTime = measureUnionTime(size);
                
                writer.printf("%d,%.3f,%.3f,%.3f,%.3f,%.3f%n",
                    size, findTime, insertTime, deleteTime, splitTime, unionTime);
                
                System.out.printf("Completed size %d%n", size);
            }
            
            System.out.println("Export complete: " + file.getAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("ERROR writing file: " + e.getMessage());
        }
    }
    
    private double measureFindTime(int size) {
        UnionSplitFind set = new UnionSplitFind();
        for (int i = 0; i < size; i++) {
            set.add(i);
        }
        
        long total = 0;
        int trials = 1000;
        for (int t = 0; t < trials; t++) {
            int key = random.nextInt(size);
            long start = System.nanoTime();
            set.find(key);
            total += System.nanoTime() - start;
        }
        return total / 1000.0 / trials;
    }
    
    private double measureInsertTime(int size) {
        long total = 0;
        int trials = 10;
        
        for (int t = 0; t < trials; t++) {
            UnionSplitFind set = new UnionSplitFind();
            for (int i = 0; i < size - 1; i++) {
                set.add(random.nextInt(size * 10));
            }
            
            long start = System.nanoTime();
            set.add(random.nextInt(size * 10));
            total += System.nanoTime() - start;
        }
        return total / 1000.0 / trials;
    }
    
    private double measureDeleteTime(int size) {
        long total = 0;
        int trials = 100;
        
        for (int t = 0; t < trials; t++) {
            UnionSplitFind set = new UnionSplitFind();
            for (int i = 0; i < size; i++) {
                set.add(i);
            }
            
            int key = random.nextInt(size);
            long start = System.nanoTime();
            set.remove(key);
            total += System.nanoTime() - start;
        }
        return total / 1000.0 / trials;
    }
    
    private double measureSplitTime(int size) {
        long total = 0;
        int trials = 100;
        
        for (int t = 0; t < trials; t++) {
            UnionSplitFind set = new UnionSplitFind();
            for (int i = 0; i < size; i++) {
                set.add(i);
            }
            
            int splitPoint = size / 2;
            long start = System.nanoTime();
            set.split(splitPoint);
            total += System.nanoTime() - start;
        }
        return total / 1000.0 / trials;
    }
    
    private double measureUnionTime(int size) {
        long total = 0;
        int trials = 100;
        
        for (int t = 0; t < trials; t++) {
            UnionSplitFind left = new UnionSplitFind();
            UnionSplitFind right = new UnionSplitFind();
            
            for (int i = 0; i < size / 2; i++) {
                left.add(i);
            }
            for (int i = size / 2; i < size; i++) {
                right.add(i);
            }
            
            long start = System.nanoTime();
            UnionSplitFind.union(left, right);
            total += System.nanoTime() - start;
        }
        return total / 1000.0 / trials;
    }
    
    // ==================== UTILITY ====================
    
    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
    
    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        PerformanceTester tester = new PerformanceTester();
        
        // Run correctness tests
        tester.runCorrectnessTests();
        
        // Run performance benchmarks
        tester.runPerformanceBenchmarks();
        
        // Export data for plotting - use current directory for simplicity
        String filename = "benchmark_data.csv";
        if (args.length > 0) {
            filename = args[0];  // Allow custom filename via command line
        }
        tester.exportBenchmarkData(filename);
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}