package structures;

import java.util.ArrayList;
import java.util.List;

/**
 * The <tt>FibonacciHeap</tt> class represents a heap data structure
 * consisting of a collection of trees. A Fibonacci heap guarantees
 * good amortized running times for its operations. Particularly,
 * insert, findMin and decreaseKey all run in O(1) time, while
 * deleteMin takes O(log N). This makes it a favourite data structure
 * for algorithms that rely heavily on decreaseKey (e.g. Dijkstra's
 * algorithm)
 *
 * More info at:
 *  <a href="https://en.wikipedia.org/wiki/Fibonacci_heap">Fib Heap</a>
 *
 * The implementation supports the following operations:
 *
 *  - insert
 *  - findMin
 *  - decreaseKey
 *  - deleteMin
 *  - delete
 *  - merge
 *  - size
 *  - isEmpty
 */
public class FibonacciHeap<T> {
    private Node<T> minElem;
    private int numberOfElements;

    public static final class Node<T> {
        private int degree = 0;
        private boolean isMarked = false;
        private T elem;
        private double priority;

        private Node<T> parent;
        private Node<T> child;
        private Node<T> left;
        private Node<T> right;

        /**
         * Initialize a new node that holds the given element with
         * the given priority
         *
         * @param elem the element to associate with this heap node
         * @param priority the priority of this element
         */
        public Node(T elem, double priority) {
            this.left = this;
            this.right = this;
            this.elem = elem;
            this.priority = priority;
        }

        public T getElem() {
            return elem;
        }

        public double getPriority() {
            return priority;
        }
    }

    /**
     * Initialize an empty Fibonacci heap
     */
    public FibonacciHeap() {
        this.minElem = null;
        this.numberOfElements = 0;
    }

    /**
     * Returns the number of elements in the Fibonacci heap
     * @return the number of elements in the Fibonacci heap
     */
    public int size() { return this.numberOfElements; }

    /**
     * Checks if the Fibonacci heap is empty
     * @return <tt>true</tt> if the Fibonacci heap is empty or
     *  <tt>false</tt> otherwise
     */
    public boolean isEmpty() { return size() == 0; }

    /**
     * Returns the minimum element in the Fibonacci heap
     * @return the minimum element in the Fibonacci heap
     * @throws java.util.NoSuchElementException
     *  if fibonacci heap is empty
     */
    public Node<T> findMin() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException(
                                    "The Fibonacci heap is empty");
        }
        return this.minElem;
    }

    /**
     * Inserts the specified element into the Fibonacci heap with
     * the given priority
     *
     * @param elem the given element
     * @param priority the given priority
     * @return a node associated with the element in the Fibonacci heap
     */
    public Node<T> insert(T elem, double priority) {
        Node<T> newNode = new Node<T>(elem, priority);
        this.minElem = mergeLists(this.minElem, newNode);
        this.numberOfElements++;

        return newNode;
    }

    private static <T> Node<T> mergeLists(Node<T> first, Node<T> second) {
        //we assume both first and second are Fibonacci heaps
        //pointing to their respective minimum elements
        if (first == null && second == null) {
            return null;
        } else if (first != null && second == null) {
            return first;
        } else if (first == null && second != null) {
            return second;
        } else {
            Node<T> originalFirstRight = first.right;
            first.right = second.right;
            first.right.left = first;
            second.right = originalFirstRight;
            second.right.left = second;

            return first.priority < second.priority? first : second;
        }
    }

    /**
     * Merges two given Fibonacci heaps into one. This function is
     * destructive regarding the initial two heaps.
     *
     * @param first the first given Fibonacci heap to merge
     * @param second the second given Fibonacci heap to merge
     * @return a new merged Fibonacci heap
     */
    public static <T> FibonacciHeap<T> merge(FibonacciHeap<T> first,
                                    FibonacciHeap<T> second) {
        FibonacciHeap<T> newFibHeap = new FibonacciHeap<T>();

        newFibHeap.minElem = mergeLists(first.minElem, second.minElem);
        newFibHeap.numberOfElements = first.numberOfElements +
                                    second.numberOfElements;

        first.numberOfElements = second.numberOfElements = 0;
        first.minElem = null;
        second.minElem = null;

        return newFibHeap;
    }

    /**
     * Decreases the priority of the specified node. We assume the
     * given node is in the Fibonacci heap.
     *
     * @param node node whose priority will be decreased
     * @param newPriority the new priority
     * @throws java.lang.IllegalArgumentException if the new priority
     *  is bigger than the old one
     */
    public void decreaseKey(Node<T> node, double newPriority) {
        if (newPriority > node.priority) {
            throw new java.lang.IllegalArgumentException(
                        "The new priority should be smaller");
        }
        node.priority = newPriority;

        if (node.parent != null &&
                node.priority <= node.parent.priority) {
            //heap property is violated
            cutNode(node);
        }
        if (node.priority <= this.minElem.priority) {
            this.minElem = node;
        }
    }

    private void cutNode(Node<T> node) {
        node.isMarked = false;

        //base case: if there are no parents left
        if (node.parent == null) {
            return;
        }

        //if the node has any siblings we cut them from the node...
        if (node.right != node) {
            node.right.left = node.left;
            node.left.right = node.right;
        }

        //...and choose one arbitrarily to fill the child's role
        if (node.parent.child == node) {
            if (node.right != node) {
                node.parent.child = node.right;
            } else {
                node.parent.child = null;
            }
        }

        node.parent.degree--;
        //we put this node on the top root list...
        node.left = node.right = node;
        this.minElem = mergeLists(this.minElem, node);

        //...and mark the parent (if it is already marked, we cut it)
        if (node.parent.isMarked) {
            cutNode(node.parent);
        } else {
            node.parent.isMarked = true;
        }
        node.parent = null;
    }

    /**
     * Removes and returns the minimum element of the Fibonacci heap
     * @return the minimum element in the Fibonacci heap
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public Node<T> deleteMin() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException(
                                    "The heap is empty");
        }
        this.numberOfElements--;
        Node<T> minElement = this.minElem;

        if (this.minElem.right == this.minElem) {
            //only element in fib heap
            this.minElem = null;
        } else {
            //we cut the siblings of the node out...
            this.minElem.left.right = this.minElem.right;
            this.minElem.right.left = this.minElem.left;
            this.minElem = this.minElem.right;
        }

        //...and we cut the children of the node
        if (minElement.child != null) {
            Node<T> currentNode = minElement.child;
            do {
                currentNode.parent = null;
                currentNode = currentNode.right;
            } while (currentNode != minElement.child);
        }

        this.minElem = mergeLists(this.minElem, minElement.child);
        if (this.minElem == null) {
            return minElement;
        }
        consolidate();  //to find the new minimum element we have
                        //to merge trees of the same degree

        return minElement;
    }

    private void consolidate() {
        List<Node<T>> treeTable = new ArrayList<Node<T>>();
        List<Node<T>> toVisit = new ArrayList<Node<T>>();
        for (Node<T> currentNode = this.minElem;
             toVisit.isEmpty() || toVisit.get(0) != currentNode;
             currentNode = currentNode.right) {
            toVisit.add(currentNode);
        }

        for (Node<T> currentNode : toVisit) {
            while(true) {
                //bit of a hack to ensure that the treeTable can hold
                //an element with that many children
                while (currentNode.degree >= treeTable.size()) {
                    treeTable.add(null);
                }
                if (treeTable.get(currentNode.degree) == null) {
                    treeTable.set(currentNode.degree, currentNode);
                    break;
                }

                Node<T> other = treeTable.get(currentNode.degree);
                treeTable.set(currentNode.degree, null);

                Node<T> min = (other.priority < currentNode.priority)?
                        other : currentNode;
                Node<T> max = (other.priority < currentNode.priority)?
                        currentNode : other;

                max.right.left = max.left;
                max.left.right = max.right;
                max.right = max.left = max;

                min.child = mergeLists(min.child, max);
                max.parent = min;
                max.isMarked = false;
                min.degree++;
                currentNode = min;
            }
            if (currentNode.priority <= this.minElem.priority) {
                this.minElem = currentNode;
            }
        }
    }
    /**
     * Deletes a node from the Fibonacci heap. We assume the
     * given node is in the Fibonacci heap
     *
     * @param node the node to be deleted
     */
    public void delete(Node<T> node) {
        decreaseKey(node, Double.NEGATIVE_INFINITY);
        deleteMin();
    }
}