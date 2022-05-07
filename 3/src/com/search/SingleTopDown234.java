package com.search;

import edu.princeton.cs.algs4.StdRandom;

public class SingleTopDown234<K extends Comparable<K>, V> implements SymbolTable<K, V> {
    private Node root;
    private int size;

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    public SingleTopDown234() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        System.out.println("Putting: " + key + "-" + value);
        Node current = this.root;
        Node prev = null;
        int depth = 0;
        while (current != null) {
            current = remove4NodeAndRestoreBalance(current, prev);
            int cmp = key.compareTo(current.getKey());
            prev = current;
            if (cmp < 0) {
                current = current.getLeft();
                depth++;
            } else if (cmp > 0) {
                current = current.getRight();
                depth++;
            } else {
                current.setValue(value);
                break;
            }
        }

        if (current == null) {
            setChild(prev, new Node(key, value, RED, depth));
            this.size++;
        }
    }

    @Override
    public V get(K key) {
        Node current = this.root;
        V target = null;
        while (current != null) {
            int cmp = key.compareTo(current.getKey());
            if (cmp > 0) {
                current = current.getRight();
            } else if (cmp < 0) {
                current = current.getLeft();
            } else {
                target = current.getValue();
                break;
            }
        }
        return target;
    }

    private Node remove4NodeAndRestoreBalance(Node node, Node parent) {
        if (isRed(node.getLeft()) && isRed(node.getLeft().getLeft())) {
            node = rotateRight(node);
        } else if (isRed(node.getLeft()) && isRed(node.getLeft().getRight())) {
            node.setLeft(rotateLeft(node.getLeft()));
            node = rotateRight(node);
        } else if (isRed(node.getRight()) && !isRed(node.getLeft())) {
            node = rotateLeft(node);
        }
        setChild(parent, node); // changing structure - careful

        if (isRed(node.getLeft()) && isRed(node.getRight())) {
            flipColor(node);
            this.root.setColor(BLACK);
        }
        return node; // node might have changed in rotation
    }

    private void setChild(Node parent, Node child) {
        if (parent != null) {
            int cmp = child.compareTo(parent);
            if (cmp < 0) {
                parent.setLeft(child);
            } else if (cmp > 0) {
                parent.setRight(child);
            }
        } else {
            this.root = child;
            this.root.setColor(BLACK);
        }
    }

    private void flipColor(Node node) {
        node.setColor(RED);
        node.getLeft().setColor(BLACK);
        node.getRight().setColor(BLACK);
    }

    private Node rotateLeft(Node node) {
        Node rot = node.getRight();
        node.setRight(rot.getLeft());
        rot.setLeft(node);
        rot.setColor(node.isRed());
        node.setColor(RED);
        return rot;
    }

    private Node rotateRight(Node node) {
        Node rot = node.getLeft();
        node.setLeft(rot.getRight());
        rot.setRight(node);
        rot.setColor(node.isRed());
        node.setColor(RED);
        return rot;
    }

    private boolean isRed(Node node) {
        boolean isRed = false;
        if (node != null) {
            isRed = node.isRed();
        }
        return isRed;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private class Node implements Comparable<Node> {
        private final K key;
        private V value;
        private Node left, right;
        private boolean color;
        private int subtreePopulation;
        private int depth;

        public Node(K key, V value, boolean color, int depth) {
            this.key = key;
            this.value = value;
            this.depth = depth;
            this.color = color;
            this.subtreePopulation = 1; // itself
        }

        @Override
        public int compareTo(Node n) {
            return this.key.compareTo(n.key);
        }

        public boolean isRed() {
            return color;
        }

        public void setColor(boolean color) {
            this.color = color;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public int getSubtreePopulation() {
            return subtreePopulation;
        }

        public void setSubtreePopulation(int subtreePopulation) {
            this.subtreePopulation = subtreePopulation;
        }

        @Override
        public String toString() {
            return "(" + this.key + "-" + this.value + ")";
        }
    }

    // test client
    public static void main(String[] args) {
        SingleTopDown234<Integer, Character> alphabets = new SingleTopDown234<>();
        final int SIZE = 50, LIM = 100;

        for (int i = 0; i < SIZE; i++) {
            alphabets.put(StdRandom.uniform(LIM), MyBinarySearchTree.randomCharacter());
        }

        for (int i = 0; i < SIZE / 2; i++) {
            int random = StdRandom.uniform(LIM);
            System.out.println("Alphabet: key " + random + " values " + alphabets.get(random));
        }
    }
}
