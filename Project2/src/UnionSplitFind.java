/**
 * UnionSplitFind.java
 * 
 * A high-level API for Union-Split-Find operations on ordered sets.
 * This class wraps the Treap implementation and provides a clean interface
 * for the three core operations:
 * 
 *   - UNION: Merge two ordered sets into one
 *   - SPLIT: Divide a set into two parts at a given value
 *   - FIND:  Check membership in the set
 * 
 * This data structure is useful for:
 *   - Dynamic set manipulation
 *   - Range queries on ordered data
 *   - Interval management
 *   - Database index operations
 * 
 * @author [Your Names Here]
 */
public class UnionSplitFind {
    
    private Treap treap;
    private String name;  // Optional name for the set (useful for demos)
    
    /**
     * Creates an empty Union-Split-Find structure
     */
    public UnionSplitFind() {
        this.treap = new Treap();
        this.name = "UnnamedSet";
    }
    
    /**
     * Creates a named Union-Split-Find structure
     * @param name Name identifier for this set
     */
    public UnionSplitFind(String name) {
        this.treap = new Treap();
        this.name = name;
    }
    
    /**
     * Internal constructor with existing treap
     */
    private UnionSplitFind(Treap treap, String name) {
        this.treap = treap;
        this.name = name;
    }
    
    // ==================== CORE OPERATIONS ====================
    
    /**
     * FIND Operation
     * 
     * Determines if an element exists in the set.
     * 
     * @param element The element to search for
     * @return true if element is in the set, false otherwise
     * 
     * Time Complexity: O(log n) expected
     * 
     * Example:
     *   set = {1, 3, 5, 7, 9}
     *   find(5) → true
     *   find(4) → false
     */
    public boolean find(int element) {
        return treap.find(element);
    }
    
    /**
     * SPLIT Operation
     * 
     * Divides this set into two sets based on a split value.
     * After splitting, this set becomes empty.
     * 
     * @param splitValue The value to split at
     * @return Array of two UnionSplitFind structures:
     *         [0] contains elements ≤ splitValue
     *         [1] contains elements > splitValue
     * 
     * Time Complexity: O(log n) expected
     * 
     * Example:
     *   set = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
     *   split(5) → [{1,2,3,4,5}, {6,7,8,9,10}]
     */
    public UnionSplitFind[] split(int splitValue) {
        Treap[] splitTreaps = treap.split(splitValue);
        
        UnionSplitFind left = new UnionSplitFind(splitTreaps[0], name + "_L");
        UnionSplitFind right = new UnionSplitFind(splitTreaps[1], name + "_R");
        
        // Clear this set after splitting
        this.treap = new Treap();
        
        return new UnionSplitFind[] { left, right };
    }
    
    /**
     * UNION Operation
     * 
     * Merges two ordered sets into one.
     * PREREQUISITE: All elements in 'this' must be less than all elements in 'other'
     * 
     * @param other The set to merge with (must have all larger elements)
     * @return A new merged UnionSplitFind structure
     * 
     * Time Complexity: O(log n) expected
     * 
     * Example:
     *   set1 = {1, 2, 3}
     *   set2 = {5, 6, 7}
     *   union(set1, set2) → {1, 2, 3, 5, 6, 7}
     */
    public UnionSplitFind union(UnionSplitFind other) {
        // Validate precondition
        if (!this.isEmpty() && !other.isEmpty()) {
            if (this.getMax() >= other.getMin()) {
                throw new IllegalArgumentException(
                    "Union precondition violated: all elements in first set must be < all elements in second set. " +
                    "Max of first set: " + this.getMax() + ", Min of second set: " + other.getMin()
                );
            }
        }
        
        Treap merged = Treap.merge(this.treap, other.treap);
        return new UnionSplitFind(merged, "(" + this.name + "∪" + other.name + ")");
    }
    
    /**
     * Static UNION method for convenience
     */
    public static UnionSplitFind union(UnionSplitFind left, UnionSplitFind right) {
        return left.union(right);
    }
    
    // ==================== ADDITIONAL OPERATIONS ====================
    
    /**
     * Adds an element to the set
     * @param element Element to add
     */
    public void add(int element) {
        treap.insert(element);
    }
    
    /**
     * Removes an element from the set
     * @param element Element to remove
     * @return true if element was found and removed
     */
    public boolean remove(int element) {
        return treap.delete(element);
    }
    
    /**
     * Builds a set from an array of elements
     * @param elements Array of elements to add
     */
    public void buildFromArray(int[] elements) {
        for (int element : elements) {
            treap.insert(element);
        }
    }
    
    /**
     * Gets the number of elements in the set
     */
    public int size() {
        return treap.size();
    }
    
    /**
     * Checks if the set is empty
     */
    public boolean isEmpty() {
        return treap.isEmpty();
    }
    
    /**
     * Gets the minimum element
     */
    public int getMin() {
        return treap.getMin();
    }
    
    /**
     * Gets the maximum element
     */
    public int getMax() {
        return treap.getMax();
    }
    
    /**
     * Returns all elements in sorted order
     */
    public int[] toSortedArray() {
        return treap.toSortedArray();
    }
    
    /**
     * Gets the name of this set
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of this set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Validates the internal structure
     */
    public boolean isValid() {
        return treap.isValid();
    }
    
    /**
     * Prints the internal tree structure
     */
    public void printStructure() {
        System.out.println("=== " + name + " ===");
        treap.printTree();
    }
    
    @Override
    public String toString() {
        int[] sorted = toSortedArray();
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" = {");
        for (int i = 0; i < sorted.length; i++) {
            sb.append(sorted[i]);
            if (i < sorted.length - 1) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * Creates a string representation with size info
     */
    public String toDetailedString() {
        return toString() + " [size=" + size() + "]";
    }
}