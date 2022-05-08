package lesson4;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        SortedMap<Character, Node> children = new TreeMap<>();
    }

    private final Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     * <p>
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    public class TrieIterator implements Iterator<String> {



        private class ChangeableNode extends Node {
            public final Node source;
            public ChangeableNode(Node source) {
                this.children = (TreeMap<Character, Node>) ((TreeMap<Character, Node>) source.children).clone();
                this.source = source;
            }
        }

        private final Stack<ChangeableNode> nodesStack = new Stack<>();
        private final StringBuilder currentWord = new StringBuilder();
        private boolean readyToRemove = false;

        private int counter = 0;

        // Временные затраты: O(1)
        // Затраты по памяти: O(1)
        @Override
        public void remove() {
            if(!readyToRemove) throw new IllegalStateException();
            nodesStack.peek().source.children.remove((char) 0);
            readyToRemove = false;
            counter--;
            size--;
        }

        public TrieIterator() {
            nodesStack.push(new ChangeableNode(root));
        }

        // Временные затраты: O(1)
        // Затраты по памяти: O(1)
        @Override
        public boolean hasNext() {
            return counter < size;
        }


        private String findNextWord() {
            ChangeableNode current = nodesStack.peek();
            while (!current.children.containsKey((char) 0) && current.children.keySet().size() != 0) {
                Character nextChar = current.children.firstKey();
                ChangeableNode nextNode = new ChangeableNode(current.children.get(nextChar));
                nodesStack.push(nextNode);
                currentWord.append(nextChar);
                current.children.remove(nextChar);
                current = nextNode;
            }
            if (current.children.containsKey((char) 0)) {
                current.children.remove((char) 0);
                return currentWord.toString();
            } else {
                //здесь мы спускаемся в стеке до узла, имеющего не посещенных потомков
                while (current.children.isEmpty()) {
                    assert !nodesStack.isEmpty();
                    nodesStack.pop();
                    currentWord.deleteCharAt(currentWord.length() - 1);
                    current = nodesStack.peek();
                }
                return findNextWord();
            }
        }

        // Временные затраты: в самом худшем случае O(MAX_WORD_SIZE)
        // Затраты по памяти: в самом худшем случае O(MAX_WORD_SIZE)
        //MAX_WORD_SIZE -- длина самого длинного слова, когда-либо добавлявшегося в этот Trie
        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            counter++;
            readyToRemove = true;
            return findNextWord();
        }
    }

}