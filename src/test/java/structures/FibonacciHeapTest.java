package structures;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.PriorityQueue;

/**
 * The test suite for the <tt>FibonacciHeap</tt> class
 */
public class FibonacciHeapTest {

    private FibonacciHeap<String> fibHeap;
    private PriorityQueue<Integer> pQueue;

    @Before
    public void setUp() {
        fibHeap = new FibonacciHeap<String>();
        pQueue = new PriorityQueue<Integer>();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(fibHeap.isEmpty());
    }

    @Test
    public void testOneElement() {
        fibHeap.insert("one", 1);
        assertEquals(1, fibHeap.size());
    }

    @Test
    public void testInsertion() {
        for (int i = 0; i < 10000; i++) {
            fibHeap.insert(i + ": ", i);
        }
        assertEquals(10000, fibHeap.size());
    }

    @Test
    public void testFindMin() {
        for (int i = 0; i < 10000; i++) {
            pQueue.add(i);
            fibHeap.insert(i + ": ", i);
        }
        assertEquals(pQueue.peek(), fibHeap.findMin().getPriority(), 0.01);
    }

    @Test
    public void testDeleteMin() {
        for (int i = 0; i < 10000; i++) {
            pQueue.add(i);
            fibHeap.insert(i + ": ", i);
        }
        for (int i = 0; i < 300; i++) {
            assertEquals(pQueue.poll(), fibHeap.deleteMin().getPriority(), 0.01);
        }
    }
}
