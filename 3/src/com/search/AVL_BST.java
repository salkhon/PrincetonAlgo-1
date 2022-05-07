package com.search;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class AVL_BST<K extends Comparable<K>, V> implements Iterable<K>, SymbolTable<K, V> {
    private Node root;

    public AVL_BST() {
    }

    public V get(K key) {
        V targetVal = null;
        Node current = this.root;
        while (current != null) {
            int cmp = key.compareTo(current.getKey());
            if (cmp < 0) {
                current = current.getLeft();
            } else if (cmp > 0) {
                current = current.getRight();
            } else {
                targetVal = current.getValue();
                break;
            }
        }
        return targetVal;
    }

    public void put(K key, V value) {
        this.root = putRecur(key, value, this.root);
    }

    private Node putRecur(K key, V value, Node current) {
        if (current == null) {
            return new Node(key, value);
        }

        int cmp = key.compareTo(current.getKey());
        if (cmp < 0) {
            current.setLeft(putRecur(key, value, current.getLeft()));
        } else if (cmp > 0) {
            current.setRight(putRecur(key, value, current.getRight()));
        } else {
            current.setValue(value);
        }

        current = restoreAVLBalance(current);

        return current;
    }

    private Node restoreAVLBalance(Node current) {
        if (heightOfSubtree(current.getLeft()) > heightOfSubtree(current.getRight()) + 1) {
            current = rotateRight(current);
        } else if (heightOfSubtree(current.getRight()) > heightOfSubtree(current.getLeft()) + 1) {
            current = rotateLeft(current);
        } else {
            updatePopulationAndHeight(current);
        }
        return current;
    }

    private Node rotateLeft(Node subtreeRoot) {
        Node rotatingNode = subtreeRoot.getRight();
        subtreeRoot.setRight(rotatingNode.getLeft());
        rotatingNode.setLeft(subtreeRoot);

        updatePopulationAndHeight(rotatingNode);
        updatePopulationAndHeight(subtreeRoot);

        return rotatingNode;
    }

    private Node rotateRight(Node subtreeRoot) {
        Node rotatingNode = subtreeRoot.getLeft();
        subtreeRoot.setLeft(rotatingNode.getRight());
        rotatingNode.setRight(subtreeRoot);

        updatePopulationAndHeight(rotatingNode);
        updatePopulationAndHeight(subtreeRoot);

        return rotatingNode;
    }

    private void updatePopulationAndHeight(Node node) {
        node.setSubtreePopulation(subtreePopulationOf(node.getLeft()) +
                subtreePopulationOf(node.getRight()) + 1);
        node.setHeightOfSubtree(Integer.max(heightOfSubtree(node.getLeft()), heightOfSubtree(node.getRight())) + 1);
    }

    public int size() {
        return subtreePopulationOf(this.root);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int subtreePopulationOf(Node node) {
        int population = 0;
        if (node != null) {
            population = node.getSubtreePopulation();
        }
        return population;
    }

    public int heightOfSubtree(Node node) {
        int height = 0;
        if (node != null) {
            height = node.getHeightOfSubtree();
        }
        return height;
    }

    public Node selectNode(int rank) {
        Node targetNode = null;
        if (size() > rank) {
            Node current = this.root;
            boolean isFound = false;
            int currentRank = rank;
            while (!isFound) {
                int leftLinkPopulation = subtreePopulationOf(current.getLeft());
                if (leftLinkPopulation < currentRank) {
                    currentRank -= leftLinkPopulation + 1;
                    current = current.getRight();
                } else if (leftLinkPopulation > currentRank) {
                    current = current.getLeft();
                } else {
                    targetNode = current;
                    isFound = true;
                }
            }
        }
        return targetNode;
    }

    public int rank(K key) {
        int rank = 0;
        Node current = this.root;
        while (current != null) {
            int cmp = key.compareTo(current.getKey());
            if (cmp < 0) {
                current = current.getLeft();
            } else if (cmp > 0) {
                rank += subtreePopulationOf(current.getLeft()) + 1;
                current = current.getRight();
            } else {
                rank += subtreePopulationOf(current.getLeft());
                break;
            }
        }
        return rank;
    }

    @NotNull
    @Override
    public Iterator<K> iterator() {
        Queue<K> keyQueue = new Queue<>();
        enqueueTree(keyQueue, this.root);
        return keyQueue.iterator();
    }

    public void enqueueTree(Queue<K> keyQueue, Node current) {
        if (current == null) {
            return;
        }
        enqueueTree(keyQueue, current.getLeft());
        keyQueue.enqueue(current.getKey());
        enqueueTree(keyQueue, current.getRight());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[ ");
        for (K key : this) {
            stringBuilder.append(key).append("-").append(get(key)).append(" ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private class Node implements Comparable<Node> {
        private final K key;
        private V value;
        private Node left, right;
        private int heightOfSubtree;
        private int subtreePopulation;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.heightOfSubtree = 1;
            this.subtreePopulation = 1; // itself
        }

        @Override
        public int compareTo(Node n) {
            return this.key.compareTo(n.key);
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public int getHeightOfSubtree() {
            return heightOfSubtree;
        }

        public void setHeightOfSubtree(int heightOfSubtree) {
            this.heightOfSubtree = heightOfSubtree;
        }

        public int getSubtreePopulation() {
            return subtreePopulation;
        }

        public void setSubtreePopulation(int subtreePopulation) {
            this.subtreePopulation = subtreePopulation;
        }

        @Override
        public String toString() {
            return this.key + "-" + this.value;
        }
    }

    // test client
    public static void main(String[] args) {
        AVL_BST<Integer, Character> alphabets = new AVL_BST<>();
        final int SIZE = 100, LIM = 200;

        for (int i = 0; i < SIZE; i++) {
            Integer key = StdRandom.uniform(LIM);
            Character value = MyBinarySearchTree.randomCharacter();
            System.out.println("Putting: " + key + "-" + value);
            alphabets.put(key, value);
            System.out.println(alphabets);
        }

        for (int i = 0; i < SIZE / 2; i++) {
            int random = StdRandom.uniform(LIM);
            System.out.println("Alphabet: key " + random + " values " + alphabets.get(random));
        }

        System.out.println(alphabets);
        System.out.println("size: " + alphabets.size());

        int rank = StdRandom.uniform(alphabets.size());
        Integer rankedInteger = alphabets.selectNode(rank).getKey();

        System.out.println("Node of rank " + rank + " : " + alphabets.selectNode(rank));
        System.out.println("Rank of key " + rankedInteger + " : " + alphabets.rank(rankedInteger));

    }
}
