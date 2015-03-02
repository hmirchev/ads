package structures;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * The test suite for the <tt>Trie</tt> class
 */
public class TrieTest {

    private Trie<Integer> st;
    private String[] input = "she sells sea shells by the sea shore".split(" ");

    @Before
    public void setUp() {
        st = new Trie<Integer>();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(st.isEmpty());
    }

    @Test
    public void testOneElement() {
        st.put("one", 1);
        assertEquals(1, st.size());
    }

    @Test
    public void testInsertion() {
        for (String key : input) {
            st.put(key, 1);
        }
        //remove 1 for the duplicating "sea"
        assertEquals(input.length - 1, st.size());
    }

    @Test
    public void testContainment() {
        for (String key : input) {
            st.put(key, 1);
        }
        assertTrue(st.contains("sea"));
        assertFalse(st.contains("river"));
    }

    @Test
    public void testGetting() {
        int i = 0;
        for (String key : input) {
            st.put(key, i++);
        }
        assertEquals(0, (int) st.get("she"));
        assertEquals(6, (int) st.get("sea"));
    }

    @Test
    public void testDeletion() {
        for (String key : input) {
            st.put(key, 1);
        }
        assertEquals(7, st.size());
        st.delete("by");
        assertEquals(6, st.size());
        st.delete("she");
        assertEquals(5, st.size());
        st.delete("sea");
        assertEquals(4, st.size());
    }
}
