package bearmaps.proj2c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//@src - lab TA solution 3/22 9 AM lab
public class MyTrieSet implements TrieSet61B {

    private Node root;

    public MyTrieSet() {
        clear();
    }

    public void clear() {
        root = new Node();
    }

    public boolean contains(String key) {
        if (key == null) {
            return false;
        }

        Node n = root;
        for (int i = 0; i< key.length(); i++) {
            char c = key.charAt(i);
            if (!n.links.containsKey(c)) {
                return false;
            }
            n = n.links.get(c);
        }
        return n.isKey;
    }


    @Override
    public void add(String key) {
        if (key == null) {
            return;
        }
        Node n = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!n.links.containsKey(c)) {
                n.links.put(c, new Node());
            }
            n = n.links.get(c);
        }
        n.isKey = true;
    }

    private void add(Node n, String key) {
        if (key.isEmpty()) {
            n.isKey = true;
            return;
        }
        char firstChar = key.charAt(0);
        String rest = key.substring(1);

        Node nextNode = n.links.get(firstChar);

        if (nextNode == null) {
            nextNode = new Node();
            n.links.put(firstChar, nextNode);
        }
        add(nextNode, rest);
    }

    public List<String> keysWithPrefix(String prefix) {
        List<String> results = new ArrayList<>();

        Node currNode = root;
        for (int i = 0; i < prefix.length(); i++) {
            char currChar = prefix.charAt(i);
            currNode = currNode.links.get(currChar);
            if (currNode == null) {
                return results;
            }
        }
        Node endOfPrefix = currNode;

        collectionHelper(endOfPrefix, results, prefix);
        return results;
    }

    private void collectionHelper(Node n, List<String> r, String prefix) {
        if (n.isKey) {
            r.add(prefix);
        }
        for (Character c : n.links.keySet()) {
            Node child = n.links.get(c);
            collectionHelper(child, r, prefix + c);
        }
    }

    public String longestPrefixOf(String key) {
        if (key == null) {
            return "";
        }
        String prefix = "";
        Node currNode = root;
        for (int i = 0; i < key.length(); i++) {
            char currChar = key.charAt(i);
            currNode = currNode.links.get(currChar);
            if (currNode == null) {
                break;
            }
            prefix = prefix + currChar;
        }
        return prefix;
    }

    private class Node {
        HashMap<Character, Node> links;
        boolean isKey;

        public Node() {
            isKey = false;
            links = new HashMap<>();
        }
    }
}
