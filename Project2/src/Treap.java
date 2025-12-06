/**
 * Treap.java
 * 
 * A Treap (Tree + Heap) implementation supporting Union-Split-Find operations.
 * 
 * Properties maintained:
 *   1. BST Property: For any node, left subtree keys < node key < right subtree keys
 *   2. Heap Property: For any node, node priority >= children priorities
 * 
 * Key Operations:
 *   - split(key): Divide treap into two treaps (≤ key and > key)
 *   - merge(left, right): Combine two treaps (requires all keys in left < all keys in right)
 *   - find(key): Check if key exists in treap
 *   - insert(key): Add a new key
 *   - delete(key): Remove a key
 * 
 * Time Complexity: All operations are O(log n) expected time
 * 
 * @author [Your Names Here]
 */
public class Treap {
    
    private TreapNode root;
    
    /**
     * Default constructor - creates an empty Treap
     */
    public Treap() {
        this.root = null;
    }
    
    /**
     * Constructor with an existing root node
     * @param root The root node of the treap
     */
    public Treap(TreapNode root) {
        this.root = root;
    }
    
    // ==================== CORE OPERATIONS ====================
    
    /**
     * SPLIT Operation
     * 
     * Splits the treap into two treaps based on a key value.
     * 
     * @param key The split point
     * @return An array of two Treaps: [left, right]
     *         left contains all elements ≤ key
     *         right contains all elements > key
     * 
     * Time Complexity: O(log n) expected
     */
    public Treap[] split(int key) {
        TreapNode[] result = splitNode(this.root, key);
        return new Treap[] { new Treap(result[0]), new Treap(result[1]) };
    }
    
    /**
     * Internal recursive split operation on nodes
     * 
     * @param node Current node being processed
     * @param key Split point
     * @return Array of two nodes: [left subtree root, right subtree root]
     */
    private TreapNode[] splitNode(TreapNode node, int key) {
        if (node == null) {
            return new TreapNode[] { null, null };
        }
        
        if (node.key <= key) {
            // Current node goes to left treap
            // Recursively split right subtree
            TreapNode[] rightSplit = splitNode(node.right, key);
            node.right = rightSplit[0];
            node.updateSize();
            return new TreapNode[] { node, rightSplit[1] };
        } else {
            // Current node goes to right treap
            // Recursively split left subtree
            TreapNode[] leftSplit = splitNode(node.left, key);
            node.left = leftSplit[1];
            node.updateSize();
            return new TreapNode[] { leftSplit[0], node };
        }
    }
    
    /**
     * MERGE (UNION) Operation
     * 
     * Merges two treaps into one. 
     * PREREQUISITE: All keys in 'left' must be less than all keys in 'right'
     * 
     * @param left Treap with smaller keys
     * @param right Treap with larger keys
     * @return A new merged Treap
     * 
     * Time Complexity: O(log n) expected where n = size of larger treap
     */
    public static Treap merge(Treap left, Treap right) {
        TreapNode mergedRoot = mergeNodes(left.root, right.root);
        return new Treap(mergedRoot);
    }
    
    /**
     * Internal recursive merge operation on nodes
     * 
     * @param left Root of left treap (smaller keys)
     * @param right Root of right treap (larger keys)
     * @return Root of merged treap
     */
    private static TreapNode mergeNodes(TreapNode left, TreapNode right) {
        if (left == null) return right;
        if (right == null) return left;
        
        // The node with higher priority becomes the root (heap property)
        if (left.priority > right.priority) {
            // Left becomes root, merge left's right child with right treap
            left.right = mergeNodes(left.right, right);
            left.updateSize();
            return left;
        } else {
            // Right becomes root, merge left treap with right's left child
            right.left = mergeNodes(left, right.left);
            right.updateSize();
            return right;
        }
    }
    
    /**
     * FIND Operation
     * 
     * Searches for a key in the treap.
     * 
     * @param key The key to search for
     * @return true if key exists, false otherwise
     * 
     * Time Complexity: O(log n) expected
     */
    public boolean find(int key) {
        return findNode(this.root, key);
    }
    
    /**
     * Internal recursive find operation
     */
    private boolean findNode(TreapNode node, int key) {
        if (node == null) {
            return false;
        }
        
        if (key == node.key) {
            return true;
        } else if (key < node.key) {
            return findNode(node.left, key);
        } else {
            return findNode(node.right, key);
        }
    }
    
    // ==================== ADDITIONAL OPERATIONS ====================
    
    /**
     * INSERT Operation
     * 
     * Inserts a new key into the treap using split and merge.
     * Duplicates are not inserted (set semantics).
     * 
     * Algorithm:
     *   1. Check if key already exists (if so, return)
     *   2. Split treap at key-1 into (left, right)
     *   3. Create new node
     *   4. Merge: left + newNode + right
     * 
     * @param key The key to insert
     * @return true if inserted, false if key already existed
     * 
     * Time Complexity: O(log n) expected
     */
    public boolean insert(int key) {
        // Check for duplicate - sets don't allow duplicates
        if (find(key)) {
            return false;  // Key already exists
        }
        
        // Split at key-1 to get all elements < key on left
        TreapNode[] split1 = splitNode(this.root, key - 1);
        TreapNode left = split1[0];
        TreapNode right = split1[1];
        
        // Create new node
        TreapNode newNode = new TreapNode(key);
        
        // Merge: left + newNode + right
        TreapNode temp = mergeNodes(left, newNode);
        this.root = mergeNodes(temp, right);
        return true;
    }
    
    /**
     * DELETE Operation
     * 
     * Removes a key from the treap using split and merge.
     * 
     * Algorithm:
     *   1. Split at key-1: get (left, middle+right)
     *   2. Split middle+right at key: get (middle, right)
     *   3. Merge left + right (discarding middle)
     * 
     * @param key The key to delete
     * @return true if key was found and deleted, false otherwise
     * 
     * Time Complexity: O(log n) expected
     */
    public boolean delete(int key) {
        if (!find(key)) {
            return false;
        }
        
        // Split into: elements < key, elements = key, elements > key
        TreapNode[] split1 = splitNode(this.root, key - 1);
        TreapNode left = split1[0];
        TreapNode midAndRight = split1[1];
        
        TreapNode[] split2 = splitNode(midAndRight, key);
        // split2[0] contains the node with 'key' (which we discard)
        TreapNode right = split2[1];
        
        // Merge left and right, skipping the deleted element
        this.root = mergeNodes(left, right);
        return true;
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Gets the size of the treap
     * @return Number of elements in the treap
     */
    public int size() {
        return root == null ? 0 : root.size;
    }
    
    /**
     * Checks if the treap is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }
    
    /**
     * Gets the root node (for internal operations)
     */
    public TreapNode getRoot() {
        return this.root;
    }
    
    /**
     * Sets the root node (for internal operations)
     */
    public void setRoot(TreapNode root) {
        this.root = root;
    }
    
    /**
     * Gets the minimum key in the treap
     * @return The minimum key, or Integer.MIN_VALUE if empty
     */
    public int getMin() {
        if (root == null) return Integer.MIN_VALUE;
        TreapNode current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.key;
    }
    
    /**
     * Gets the maximum key in the treap
     * @return The maximum key, or Integer.MAX_VALUE if empty
     */
    public int getMax() {
        if (root == null) return Integer.MAX_VALUE;
        TreapNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }
    
    /**
     * In-order traversal to get sorted elements
     * @return Array of keys in sorted order
     */
    public int[] toSortedArray() {
        int[] result = new int[size()];
        int[] index = {0};
        inOrderTraversal(root, result, index);
        return result;
    }
    
    private void inOrderTraversal(TreapNode node, int[] result, int[] index) {
        if (node == null) return;
        inOrderTraversal(node.left, result, index);
        result[index[0]++] = node.key;
        inOrderTraversal(node.right, result, index);
    }
    
    /**
     * Prints the treap structure (for debugging)
     */
    public void printTree() {
        System.out.println("Treap (size=" + size() + "):");
        printTreeHelper(root, "", true);
    }
    
    private void printTreeHelper(TreapNode node, String prefix, boolean isLast) {
        if (node == null) return;
        
        System.out.println(prefix + (isLast ? "└── " : "├── ") + 
                           "key=" + node.key + ", pri=" + node.priority);
        
        String newPrefix = prefix + (isLast ? "    " : "│   ");
        
        if (node.left != null || node.right != null) {
            if (node.right != null) {
                printTreeHelper(node.right, newPrefix, node.left == null);
            }
            if (node.left != null) {
                printTreeHelper(node.left, newPrefix, true);
            }
        }
    }
    
    /**
     * Validates that the treap maintains both BST and Heap properties
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE) && 
               isValidHeap(root);
    }
    
    private boolean isValidBST(TreapNode node, long min, long max) {
        if (node == null) return true;
        if (node.key <= min || node.key >= max) return false;
        return isValidBST(node.left, min, node.key) && 
               isValidBST(node.right, node.key, max);
    }
    
    private boolean isValidHeap(TreapNode node) {
        if (node == null) return true;
        if (node.left != null && node.left.priority > node.priority) return false;
        if (node.right != null && node.right.priority > node.priority) return false;
        return isValidHeap(node.left) && isValidHeap(node.right);
    }
}