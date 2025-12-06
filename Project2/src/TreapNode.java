/**
 * TreapNode.java
 * 
 * Represents a single node in a Treap data structure.
 * Each node contains:
 *   - key: the actual data value (maintains BST property)
 *   - priority: random value (maintains Heap property)
 *   - left, right: child pointers
 *   - size: subtree size (useful for order statistics)
 * 
 * @author [Your Names Here]
 */
public class TreapNode {
    
    int key;           // The value stored (BST property: left < key < right)
    int priority;      // Random priority (Heap property: parent priority > children)
    TreapNode left;    // Left child
    TreapNode right;   // Right child
    int size;          // Size of subtree rooted at this node
    
    /**
     * Constructor for creating a new TreapNode
     * @param key The value to store in this node
     */
    public TreapNode(int key) {
        this.key = key;
        this.priority = (int) (Math.random() * Integer.MAX_VALUE); // Random priority
        this.left = null;
        this.right = null;
        this.size = 1; // A single node has size 1
    }
    
    /**
     * Constructor with explicit priority (useful for testing)
     * @param key The value to store
     * @param priority The heap priority
     */
    public TreapNode(int key, int priority) {
        this.key = key;
        this.priority = priority;
        this.left = null;
        this.right = null;
        this.size = 1;
    }
    
    /**
     * Updates the size of this node based on children sizes
     * Should be called after any structural modification
     */
    public void updateSize() {
        this.size = 1;
        if (left != null) {
            this.size += left.size;
        }
        if (right != null) {
            this.size += right.size;
        }
    }
    
    @Override
    public String toString() {
        return "TreapNode{key=" + key + ", priority=" + priority + ", size=" + size + "}";
    }
}