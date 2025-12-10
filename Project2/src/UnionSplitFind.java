/**
 * Author Name: (1) Fahad Akon
 *              (2) Salma Ibrahim
 * Class:       COSC311
 * Assignment:  Prject 2
 * Date:        12/07/2025
 * Problem:     Union-Split-Find Data Structure with Performance Testing.
 * **/

public class UnionSplitFind {
    /** Number of elements */
    private final int n;

    /**
     * setId[i] is the identifier of the set that element i belongs to.
     * Many elements can share the same setId value.
     */
    private final int[] setId;

    /**
     * Next unique set id used by split(). This may grow beyond n,
     * but that is fine because it is only a label.
     */
    private int nextSetId;

    /**
     * Construct a structure with elements 0..n-1,
     * initially each element in its own set.
     */
    public UnionSplitFind(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        this.n = n;
        this.setId = new int[n];
        for (int i = 0; i < n; i++) {
            setId[i] = i; // each element is its own set
        }
        this.nextSetId = n; // new set ids start after the initial range
    }

    /** Return number of elements. */
    public int size() {
        return n;
    }

    /** Check that 0 <= x < n. */
    private void checkIndex(int x) {
        if (x < 0 || x >= n) {
            throw new IllegalArgumentException("Index out of bounds: " + x);
        }
    }

    /**
     * FIND operation:
     * Return the id of the set that x belongs to.
     * O(1).
     */
    public int find(int x) {
        checkIndex(x);
        return setId[x];
    }

    /**
     * CONNECTED operation:
     * Return true if a and b are in the same set.
     * O(1).
     */
    public boolean connected(int a, int b) {
        checkIndex(a);
        checkIndex(b);
        return setId[a] == setId[b];
    }

    /**
     * UNION operation:
     * Merge the set containing a with the set containing b.
     * Implementation: relabel all elements of b's set to a's set id.
     * O(n).
     */
    public void union(int a, int b) {
        checkIndex(a);
        checkIndex(b);

        int idA = setId[a];
        int idB = setId[b];

        if (idA == idB) {
            // already in same set
            return;
        }

        // Merge all elements currently in set idB into idA.
        for (int i = 0; i < n; i++) {
            if (setId[i] == idB) {
                setId[i] = idA;
            }
        }
    }

    /**
     * SPLIT operation:
     * Move a single element x into its own new set.
     * This creates a fresh, unique set id just for x.
     * O(1).
     */
    public void split(int x) {
        checkIndex(x);
        setId[x] = nextSetId++;
    }

    /**
     * Utility: print all current sets and their elements.
     */
    public void printSets() {
        System.out.println("Current sets:");

        for (int i = 0; i < n; i++) {
            int id = setId[i];

            // Only print this set once: when we encounter its first member.
            boolean firstOccurrence = true;
            for (int j = 0; j < i; j++) {
                if (setId[j] == id) {
                    firstOccurrence = false;
                    break;
                }
            }
            if (!firstOccurrence) {
                continue; // this set already printed
            }

            System.out.print("  { ");
            boolean firstElem = true;
            for (int j = 0; j < n; j++) {
                if (setId[j] == id) {
                    if (!firstElem) {
                        System.out.print(", ");
                    }
                    System.out.print(j);
                    firstElem = false;
                }
            }
            System.out.println(" }  (setId = " + id + ")");
        }
    }
}
