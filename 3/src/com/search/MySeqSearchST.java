package com.search;

import edu.princeton.cs.algs4.StdRandom;
import org.jetbrains.annotations.NotNull;

public class MySeqSearchST<K extends Comparable<K>, V> implements SymbolTable<K, V> {
    private Node first;
    private int size;

    public MySeqSearchST() {
        this.size = 0;
    }

    public void put(@NotNull K key, @NotNull V val) {
        this.first = new Node(key, val, this.first);
        this.size++;
    }

    public V get(K key) {
        V value = null;
        if (!isEmpty()) {
            Node targetNode = findNodeWithKeyAndMoveToFront(key);
            if (targetNode != null) {
                value = targetNode.getValue();
            }
        }
        return value;
    }

    public void delete(K key) {
        Node targetNode = findNodeWithKeyAndMoveToFront(key);
        if (targetNode != null) {
            this.first = this.first.getNext();
            this.size--;
        }
    }

    private Node findNodeWithKeyAndMoveToFront(K key) {
        Node node = null;
        Node current = this.first;
        if (this.first.getKey().compareTo(key) == 0) {
            node = this.first;
        } else {
            while (current.getNext() != null) {
                if (current.getNext().getKey().compareTo(key) == 0) {
                    node = current.getNext();

                    current.setNext(current.getNext().getNext());
                    node.setNext(this.first);
                    this.first = node;
                    break;
                }

                current = current.getNext();
            }
        }
        return node;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    private class Node {
        final K key;
        V value;
        Node next;

        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node getNext() {
            return next;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[ ");
        Node current = this.first;
        while (current != null) {
            stringBuilder.append(current.getKey()).append("-").append(current.getValue());
            if (current.getNext() != null) {
                stringBuilder.append(", ");
            }
            current = current.getNext();
        }
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }

    // test client
    public static void main(String[] args) {
        MySeqSearchST<Integer, Character> alphabets = new MySeqSearchST<>();

        alphabets.put(4, 'D');
        alphabets.put(6, 'F');
        alphabets.put(7, 'G');
        alphabets.put(10, 'J');
        alphabets.put(12, 'l');
        alphabets.put(24, 'X');
        alphabets.put(5, 'E');
        alphabets.put(12, 'L');
        alphabets.put(22, 'V');
        alphabets.put(18, 'R');
        alphabets.put(17, 'Q');
        alphabets.put(11, 'K');
        alphabets.put(2, 'B');
        alphabets.put(1, 'A');

        for (int i = 0; i < 20; i++) {
            int random = StdRandom.uniform(27);
            System.out.println("Alphabet: key " + random + " values " + alphabets.get(random));
        }

        System.out.println("Before Deletion: " + alphabets);
        alphabets.delete(22); alphabets.delete(1);
        System.out.println("After deletion: " + alphabets);
    }
}
