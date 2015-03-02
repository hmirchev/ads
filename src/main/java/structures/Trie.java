package structures;

import java.util.List;
import java.util.LinkedList;

/**
 * The <tt>Trie</tt> class represents an ordered tree data structure
 * used to store a dynamic set of key-value pairs where the keys are
 * strings.
 *
 * More info at: <a href="https://en.wikipedia.org/wiki/Trie">Trie</a>
 *
 * The implementation uses a 128-way trie (as standard ASCII includes
 * 128 characters) and it supports the following operations:
 *
 *  - put
 *  - get
 *  - contains
 *  - delete
 *  - size
 *  - isEmpty
 */
public class Trie<Value> {
    private static final int R = 128;

    private Node root;
    private int numberOfKeys;

    // R-way trie node
    private static class Node {
        private Object value;
        private Node[] next = new Node[R];
    }

    /**
     * Initialize an empty trie
     */
    public Trie() {
        this.root = null;
        this.numberOfKeys = 0;
    }

    /**
     * Returns the number of key-value pairs stored in the trie
     * @return the number of key-value pairs in the trie
     */
    public int size() { return this.numberOfKeys; }

    /**
     * Checks if the trie is empty
     * @return <tt>true</tt> if the trie is empty
     *  and <tt>false</tt> otherwise
     */
    public boolean isEmpty() { return size() == 0; }

    /**
     * Returns the value associated with the given key
     * @param key the given key
     * @return the value associated with the given key if the key is
     *  in the trie and <tt>null</tt> otherwise
     * @throws java.lang.NullPointerException if
     *  <tt>key</tt> is <tt>null</tt>
     */
    public Value get(String key) {
        Node nodeAtKey = this.get(this.root, key, 0);

        if (nodeAtKey == null) {
            return null;
        } else {
            return (Value) nodeAtKey.value;
        }
    }

    private Node get(Node currentNode, String key, int charCtr) {
        if (currentNode == null) {
            return null;
        }
        if (charCtr == key.length()) {
            //if we have reached the end of the key
            return currentNode;
        } else {
            //if not, we continue with the child node
            //containing the current char of the key
            return get(currentNode.next[key.charAt(charCtr)],
                        key,
                        charCtr + 1);
        }
    }

    /**
     * Checks if the trie contains the given key
     * @param key the given key
     * @return <tt>true</tt> if the trie contains the given key and
     *  <tt>false</tt> otherwise
     * @throws java.lang.NullPointerException if
     *  <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(String key) {
        return get(key) != null;
    }

    /**
     * Puts the key-value pair in the trie. If the key is already in
     * the trie we overwrite the old value with the new value. If the
     * given value is <tt>null</tt> we delete the key from the trie
     *
     * @param key the given key
     * @param value the given value
     * @throws java.lang.NullPointerException if
     *  <tt>key</tt> is <tt>null</tt>
     */
    public void put(String key, Value value) {
        if (value == null) {
            delete(key);
        } else {
            this.root = this.put(this.root, key, value, 0);
        }
    }

    private Node put(Node currentNode, String key, Value value, int charCtr) {
        if (currentNode == null) {
            currentNode = new Node();
        }
        if (charCtr == key.length()) {
            if (currentNode.value == null) {
                this.numberOfKeys++;
            }
            currentNode.value = value;

            return currentNode;
        } else {
            char currentChar = key.charAt(charCtr);
            currentNode.next[currentChar] = put(
                                        currentNode.next[currentChar],
                                        key, value, charCtr + 1);

            return currentNode;
        }
    }

    /**
     * Deletes the key from the trie if it is present
     * @param key the given key
     * @throws java.lang.NullPointerException if
     *  <tt>key</tt> is <tt>null</tt>
     */
    public void delete(String key) {
        this.root = this.delete(this.root, key, 0);
    }

    private Node delete(Node currentNode, String key, int charCtr) {
        if (currentNode == null) {
            return null;
        }
        if (charCtr == key.length()) {
            if (currentNode.value != null) {
                this.numberOfKeys--;
            }
            currentNode.value = null;
        } else {
            char currentChar = key.charAt(charCtr);
            currentNode.next[currentChar] = delete(
                                        currentNode.next[currentChar],
                                        key, charCtr + 1);
        }

        //we remove the subtrie at currentNode if it's completely empty
        if (currentNode.value != null) {
            return currentNode;
        }
        for (int i = 0; i < R; i++) {
            if (currentNode.next[i] != null) {
                return currentNode;
            }
        }
        return null;
    }

    /**
     * Returns all the keys in the trie as an <tt>Iterable</tt>
     *
     * Example:
     *  for(String key : trie.keys())
     *
     * @return all the keys in the trie as an <tt>Iterable</tt>
     */
    public Iterable<String> keys() {
        List<String> allTheKeys = new LinkedList<String>();
        Node currentNode = get(this.root, "", 0);
        collect(currentNode, new StringBuilder(""), allTheKeys);
        return allTheKeys;
    }

    private void collect(Node currentNode, StringBuilder currentString, List<String> keys) {
        if (currentNode == null) {
            return;
        }
        if (currentNode.value != null) {
            keys.add(currentString.toString());
        }
        for (char i = 0; i < R; i++) {
            currentString.append(i);
            collect(currentNode.next[i], currentString, keys);
            currentString.deleteCharAt(currentString.length() - 1);
        }
    }
}