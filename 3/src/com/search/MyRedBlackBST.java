package com.search;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Scanner;

public class MyRedBlackBST<K extends Comparable<K>, V> implements Iterable<K>, SymbolTable<K, V> {
    private Node root;

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    public MyRedBlackBST() {
    }

    public int size() {
        return subtreePopulationOf(this.root);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    // can't implement iterative, because call stack path is needed
    public void put(K key, V value) {
        this.root = put(key, value, this.root);
        this.root.setColor(BLACK);
    }

    private Node put(K key, V value, Node current) {
        if (current == null) {
            return new Node(key, value, RED);
        }
        int cmp = key.compareTo(current.getKey());
        if (cmp < 0) {
            current.setLeft(put(key, value, current.getLeft()));
        } else if (cmp > 0) {
            current.setRight(put(key, value, current.getRight()));
        } else {
            current.setValue(value);
            return current;
        }

        return restoreRedBlackTreeBalance(current); // population update code is always called inside
    }

    private Node restoreRedBlackTreeBalance(Node current) {
        if (current != null) {
            boolean noRotation = true;
            if (isRed(current.getRight())) {
                // put() can't cause right red child's left child to be red, because right red link passed up or added, can't have red left child because it was flipped or added to 2 node
                // necessary for delete(key)
//                if (isRed(current.getRight().getLeft())) {
//                    current.setRight(rotateRight(current.getRight()));
//                    current.getRight().getRight().setColor(BLACK);
//                }
                current = rotateLeft(current);
                noRotation = false;
            }
            if (isRed(current.getLeft()) && isRed(current.getLeft().getLeft())) {
                // this operation might require to flip color on the same node rotated right - so it is part of a composite operation on that node - rotate right, flip color
                current = rotateRight(current);
//                if (isRed(current.getRight().getRight())) {
//                    current.setRight(rotateLeft(current.getRight())); // making left leaning
//                }
                noRotation = false;
            }

            if (noRotation) {
                current.setSubtreePopulation(subtreePopulationOf(current.getLeft()) + subtreePopulationOf(current.getRight()) + 1);
            }


            // flip can occur without rotating any node
            if (isRed(current.getLeft()) && isRed(current.getRight())) {
                flipColor(current);
            }
        }
        return current;
    }

    public V get(K key) {
        V val = null;
        if (!isEmpty()) {
            Node node = search(key);
            if (node != null) {
                val = node.getValue();
            }
        }
        return val;
    }

    private Node search(K key) {
        Node current = this.root;
        Node target = null;
        int cmp;
        while (current != null) {
            cmp = key.compareTo(current.getKey());
            if (cmp < 0) {
                current = current.getLeft();
            } else if (cmp > 0) {
                current = current.getRight();
            } else {
                target = current;
                break;
            }
        }
        return target;
    }

    public Node getMinNode() {
        return getMinNode(this.root);
    }

    private Node getMinNode(Node subtreeRoot) {
        Node current = subtreeRoot;
        if (current != null) {
            while (current.getLeft() != null) {
                current = current.getLeft();
            }
        }
        return current;
    }

    public Node getMaxNode() {
        return getMaxNode(this.root);
    }

    private Node getMaxNode(Node subtreeRoot) {
        Node current = subtreeRoot;
        if (current != null) {
            while (current.getRight() != null) {
                current = current.getRight();
            }
        }

        return current;
    }

    public Node ceil(K key) {
        Node ceil = null;
        if (!isEmpty()) {
            Node current = this.root;
            int cmp;
            while (current != null) {
                cmp = key.compareTo(current.getKey());
                if (cmp < 0) {
                    // probable ceil, if no better ceil
                    ceil = current;
                    current = current.getLeft();
                } else if (cmp > 0) {
                    current = current.getRight();
                } else {
                    ceil = current;
                    break;
                }
            }
        }
        return ceil;
    }

    public Node floor(K key) {
        Node floor = null;
        if (!isEmpty()) {
            Node current = this.root;
            int cmp;
            while (current != null) {
                cmp = key.compareTo(current.getKey());
                if (cmp < 0) {
                    current = current.getLeft();
                } else if (cmp > 0) {
                    // probable floor, if no better floor
                    floor = current;
                    current = current.getRight();
                } else {
                    floor = current;
                    break;
                }
            }
        }
        return floor;
    }

    public K select(int r) {
        K targetKey = null;
        if (size() > r) {
            Node current = this.root;
            int currentRank = r;
            boolean targetFound = false;

            while (!targetFound) {
                int leftLinkPopulation = subtreePopulationOf(current.getLeft());
                if (leftLinkPopulation > currentRank) {
                    current = current.getLeft();
                } else if (leftLinkPopulation < currentRank) {
                    currentRank -= leftLinkPopulation + 1;
                    current = current.getRight();
                } else {
                    targetKey = current.getKey();
                    targetFound = true;
                }
            }
        }
        return targetKey;
    }

    public int rank(K key) {
        int rank = 0;
        if (!isEmpty()) {
            Node current = this.root;

            while (current != null) {
                int cmp = key.compareTo(current.getKey());
                if (cmp > 0) {
                    rank += subtreePopulationOf(current.getLeft()) + 1;
                    current = current.getRight();
                } else if (cmp < 0) {
                    current = current.getLeft();
                } else {
                    rank += subtreePopulationOf(current.getLeft());
                    break;
                }
            }
        }
        return rank;
    }

    private void flipColor(Node parent) {
        parent.setColor(!parent.isRed());
        if (parent.getLeft() != null) {
            parent.getLeft().setColor(!parent.getLeft().isRed());
        }
        if (parent.getRight() != null) {
            parent.getRight().setColor(!parent.getRight().isRed());
        }
    }

    // sometimes node end up linking right node with red link - breaking the left leaning red black tree condition
    // notice that depth only depends on black links
    // on rotation only red links move, which are 3-node glues of corresponding 2-3 tree
    // thus rotation preserved balance (no depth change)
    private Node rotateLeft(Node parent) throws IllegalStateException {
        Node rotatingNode = parent.getRight();
        parent.setRight(rotatingNode.getLeft());
        rotatingNode.setLeft(parent);

        boolean rotatingNodeColor = rotatingNode.isRed();
        rotatingNode.setColor(parent.isRed());
        parent.setColor(rotatingNodeColor);
        // to make rotation a self contained unit
        parent.setSubtreePopulation(subtreePopulationOf(parent.getLeft()) +
                subtreePopulationOf(parent.getRight()) + 1);
        rotatingNode.setSubtreePopulation(subtreePopulationOf(rotatingNode.getLeft()) +
                subtreePopulationOf(rotatingNode.getRight()) + 1);

        return rotatingNode;
    }

    // sometimes nodes need to be rotated right, perhaps in an unstable condition (4 node)
    private Node rotateRight(Node parent) throws IllegalStateException {
        Node rotatingNode = parent.getLeft();
        parent.setLeft(rotatingNode.getRight());
        rotatingNode.setRight(parent);

        boolean rotatingNodeColor = rotatingNode.isRed();
        rotatingNode.setColor(parent.isRed());
        parent.setColor(rotatingNodeColor);

        parent.setSubtreePopulation(subtreePopulationOf(parent.getLeft()) +
                subtreePopulationOf(parent.getRight()) + 1);
        rotatingNode.setSubtreePopulation(subtreePopulationOf(rotatingNode.getLeft()) +
                subtreePopulationOf(rotatingNode.getRight()) + 1);


        return rotatingNode;
    }

    private boolean isRed(Node node) {
        boolean isRed = false;
        if (node != null) {
            isRed = node.isRed();
        }
        return isRed;
    }

    private int subtreePopulationOf(Node node) {
        int population = 0;
        if (node != null) {
            population = node.getSubtreePopulation();
        }
        return population;
    }

    @NotNull
    @Override
    public Iterator<K> iterator() {
        Queue<K> kQueue = new Queue<>();
        enqueueTree(this.root, kQueue);
        return kQueue.iterator();
    }

    private void enqueueTree(Node current, Queue<K> queue) {
        if (current == null) {
            return;
        }
        enqueueTree(current.getLeft(), queue);
        queue.enqueue(current.getKey());
        enqueueTree(current.getRight(), queue);
    }

    // red-black BST has to preserve perfect black balance when deleting
    // min keys have to have left link null - and there cannot be a right red link in a left leaning red black BST
    // so to preserve black balance, left link of leaf null => right link of leaf is also null (if right link was red, black balance would have preserved indicating a right leaning 2 node)
    public void delMin() {
        if (!isEmpty()) {
            // just for the sake of keeping the invariant that the current node is not a 2 node, otherwise, no function
            this.root.setColor(RED);
            // if root and it's children are two nodes, they have to flip colors to delMin, inspires the idea of flipping
            this.root = delMin(this.root);
            if (!isEmpty()) {
                this.root.setColor(BLACK);
            }
        }
    }

    private Node delMin(Node current) {
        if (current.getLeft() == null) {
            return null;
        }

        if (isTwoNode(current.getLeft())) {
            current = moveRedLeft(current);
        }

        current.setLeft(delMin(current.getLeft()));

        return restoreRedBlackTreeBalance(current);
    }

    // every operation performed by the delete operation has to preserve perfect black balance
    // moves red link from parent to two of its children
    private Node moveRedLeft(Node node) {
        boolean isRightChild2NodeBeforeFlippingColors = isTwoNode(node.getRight());
        // node.right cannot be red in LLRBBST, cannot be null because node.left is not, so has to be black
        flipColor(node); // node.left and node.right is now red
        if (!isRightChild2NodeBeforeFlippingColors) {
            node.setRight(rotateRight(node.getRight()));
            node = rotateLeft(node);
        }
        return node;
    }

    public void delMax() {
        if (!isEmpty()) {
            this.root.setColor(RED);
            this.root = delMax(this.root);
            if (!isEmpty()) {
                this.root.setColor(BLACK);
            }
        }
    }

    private Node delMax(Node current) {
        // making left sibling adjacent (traversing down)
        if (isRed(current.getLeft())) {
            current = rotateRight(current);
        } else if (current.getRight() == null) {
            return null;
        }

        if (isTwoNode(current.getRight())) {
            current = moveRedRight(current);
        }

        current.setRight(delMax(current.getRight()));

        return restoreRedBlackTreeBalance(current);
    }

    private Node moveRedRight(Node current) {
        // left sibling is adjacent, so just check left sibling is non two node
        boolean isLeftSibling2NodeBeforeColorFlip = isTwoNode(current.getLeft());
        flipColor(current);
        if (!isLeftSibling2NodeBeforeColorFlip) {
            current = rotateRight(current);
        }
        return current;
    }
//    // my developed working delMax
//    private Node delMax(Node current) {
//        if (current.getRight() == null) {
//            if (current.getLeft() != null) {
//                current.getLeft().setColor(BLACK);
//            }
//            return current.getLeft();
//        }
//
//        // invariant: current node is not a 2 node - right link cannot be red
//        if (isTwoNode(current.getRight())) {
//            current = moveRedRight(current);
//        }
//
//        current.setRight(delMax(current.getRight()));
//
//        return restoreRedBlackTreeBalance(current);
//    }
//
//    // my developed working moveRedRight
//    private Node moveRedRight(Node current) {
//        if (isTwoNode(current.getLeft())) {
//            flipColor(current);
//        } else {
//            current = rotateRight(current);
//            if (isRed(current.getLeft())) {
//                current.getLeft().setColor(BLACK);
//                current.getRight().getRight().setColor(RED);
//                current.setRight(rotateLeft(current.getRight()));
//            }
//        }
//
//        return current;
//    }

    public void delete(K key) {
        // check of existence is necessary
        if (get(key) != null) {
            this.root.setColor(RED);
            this.root = delete(key, this.root);
            if (!isEmpty()) {
                this.root.setColor(BLACK);
            }
        }
    }

    // invariant: current node can't be 2 node - has to be maintained
    private Node delete(K key, Node current) {
        if (key.compareTo(current.getKey()) < 0) {
            if (isTwoNode(current.getLeft())) {
                current = moveRedLeft(current);
            }

            current.setLeft(delete(key, current.getLeft()));
        } else {
            // making sibling left adjacent for comparison
            if (isRed(current.getLeft())) {
                current = rotateRight(current);
            } else if (key.compareTo(current.getKey()) == 0 && current.getRight() == null) {
                // has no right link, therefore has no black left link, left red links were right rotated on the previous if block
                return null;
            }

            if (isTwoNode(current.getRight())) {
                current = moveRedRight(current);
            }

            if (key.compareTo(current.getKey()) == 0) {
                Node successor = getMinNode(current.getRight());
                current.setRight(delMin(current.getRight()));
                successor.setLeft(current.getLeft());
                successor.setRight(current.getRight());
                successor.setColor(current.isRed());
                successor.setSubtreePopulation(current.getSubtreePopulation());
                current = successor;
            } else {
                current.setRight(delete(key, current.getRight()));
            }
        }

        return restoreRedBlackTreeBalance(current);
    }

//    // my developed delete()
//    private Node delete(K key, Node current) {
//        if (current == null) {
//            return null;
//        }
//
//        int cmp = key.compareTo(current.getKey());
//        if (cmp < 0) {
//            if (isTwoNode(current.getLeft())) {
//                moveRedLeft(current);
//            }
//            current.setLeft(delete(key, current.getLeft()));
//        } else if (cmp > 0) {
//            if (isTwoNode(current.getRight())) {
//                moveRedRight(current);
//            }
//            current.setRight(delete(key, current.getRight()));
//        } else {
//            Node next = getMinNode(current.getRight());
//            boolean currentColor = isRed(current);
//            if (next != null) {
//                current.setRight(delMin(current.getRight()));
//                next.setLeft(current.getLeft());
//                next.setRight(current.getRight());
//            } else {
//                next = getMinNode();
//            }
//
//            if (next != null) {
//                next.setColor(currentColor);
//            }
//
//            current = next;
//        }
//
//        return restoreRedBlackTreeBalance(current);
//    }

    private boolean isTwoNode(Node node) {
        return !isRed(node) && !isRed(node.getLeft());
    }

    public boolean isPerfectBlackBalance() {
        return isPerfectBlackBalance(this.root);
    }

    private boolean isPerfectBlackBalance(Node current) {
        return blackDepth(current) >= 0;
    }

    public int blackDepth(Node current) {
        if (current == null) {
            return 0;
        }

        int blackDepthLeft = blackDepth(current.getLeft());
        int currentBlackDepth;
        if (blackDepthLeft == -1 || (blackDepthLeft != blackDepth(current.getRight()))) {
            currentBlackDepth = -1; // inconsistent black depth
        } else {
            currentBlackDepth = isRed(current) ? blackDepthLeft : 1 + blackDepthLeft;
        }

        return currentBlackDepth;
    }

    public boolean isValid23LeftLeaningRedBlackTree() {
        return isValid23LeftLeaningRedBlackTree(this.root);
    }

    private boolean isValid23LeftLeaningRedBlackTree(Node current) {
        if (current == null) {
            return true;
        }
        return  !isRed(current.getRight()) &&
                (!isRed(current) || (isRed(current) && !isRed(current.getLeft()))) &&
                isValid23LeftLeaningRedBlackTree(current.getLeft()) &&
                isValid23LeftLeaningRedBlackTree(current.getRight());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[ ");
        for (K key : this) {
            stringBuilder.append(key).append(" - ").append(get(key)).append(", ");
        }
        int lastComma = stringBuilder.lastIndexOf(",");
        if (lastComma >= 0) {
            stringBuilder.replace(lastComma, lastComma + 2, " ]");
        }
        return stringBuilder.toString();
    }

    public int rangeCount(K lo, K hi) {
        int count = 0;
        if (get(hi) != null) {
            count++;
        }
        count += rank(hi) - rank(lo);
        return count;
    }

    private class Node implements Comparable<Node> {
        private final K key;
        private V value;
        private Node left, right;
        private boolean color;
        private int subtreePopulation;

        public Node(K key, V value, boolean color) {
            this.key = key;
            this.value = value;
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
            return "(" + this.key + ", " + this.value + ")";
        }
    }

    // test client
    public static void main(String[] args) {
        MyRedBlackBST<Integer, Character> alphabets = new MyRedBlackBST<>();
        final int SIZE = 100, LIM = 200;

        for (int i = 0; i < SIZE; i++) {
            alphabets.put(StdRandom.uniform(LIM), MyBinarySearchTree.randomCharacter());
        }

        for (int i = 0; i < SIZE / 2; i++) {
            int random = StdRandom.uniform(LIM);
            System.out.println("Alphabet: key " + random + " values " + alphabets.get(random));
        }

        System.out.println(alphabets);
        System.out.println("Perfectly balanced RB BST? : " + alphabets.isPerfectBlackBalance());
        System.out.println("Valid Left Leaning Red Black Tree? : " + alphabets.isValid23LeftLeaningRedBlackTree());

        System.out.println("size: " + alphabets.size());
        int rank = StdRandom.uniform(alphabets.size());
        Integer rankedInteger = alphabets.select(rank);
        System.out.println(rank + " ranked key: " + rankedInteger);
        System.out.println("Rank " + rank + " : " + rankedInteger + "-" + alphabets.get(rankedInteger));
        System.out.println("Rank of key: " + rankedInteger + " is " + alphabets.rank(rankedInteger));


        System.out.println(alphabets);
        System.out.println("Perfectly balanced RB BST? : " + alphabets.isPerfectBlackBalance());
        System.out.println("Valid Left Leaning Red Black Tree? : " + alphabets.isValid23LeftLeaningRedBlackTree());


        System.out.println("Deleting min");
        alphabets.delMin();
        System.out.println(alphabets);
        System.out.println("Perfectly balanced RB BST? : " + alphabets.isPerfectBlackBalance());
        System.out.println("Valid Left Leaning Red Black Tree? : " + alphabets.isValid23LeftLeaningRedBlackTree());


        System.out.println("Deleting min");
        alphabets.delMin();
        System.out.println(alphabets);
        System.out.println("Perfectly balanced RB BST? : " + alphabets.isPerfectBlackBalance());
        System.out.println("Valid Left Leaning Red Black Tree? : " + alphabets.isValid23LeftLeaningRedBlackTree());


        System.out.println("Deleting min");
        alphabets.delMin();
        System.out.println(alphabets);
        System.out.println("Perfectly balanced RB BST? : " + alphabets.isPerfectBlackBalance());
        System.out.println("Valid Left Leaning Red Black Tree? : " + alphabets.isValid23LeftLeaningRedBlackTree());

        System.out.println("Deleting MAX");
        alphabets.delMax();
        System.out.println(alphabets);
        System.out.println("Perfectly balanced RB BST? : " + alphabets.isPerfectBlackBalance());
        System.out.println("Valid Left Leaning Red Black Tree? : " + alphabets.isValid23LeftLeaningRedBlackTree());


        System.out.println("Deleting MAX");
        alphabets.delMax();
        System.out.println(alphabets);
        System.out.println("Perfectly balanced RB BST? : " + alphabets.isPerfectBlackBalance());
        System.out.println("Valid Left Leaning Red Black Tree? : " + alphabets.isValid23LeftLeaningRedBlackTree());


        System.out.println("Deleting MAX");
        alphabets.delMax();
        System.out.println(alphabets);
        System.out.println("Perfectly balanced RB BST? : " + alphabets.isPerfectBlackBalance());
        System.out.println("Valid Left Leaning Red Black Tree? : " + alphabets.isValid23LeftLeaningRedBlackTree());

        Scanner scanner = new Scanner(System.in);
        int delKey = 0;
        while (delKey >= 0) {
            System.out.print("Enter key to delete: (negative to stop) ");
            delKey = scanner.nextInt();
            if (delKey >= 0) {
                System.out.println("Deleting key: " + delKey);
                alphabets.delete(delKey);
                System.out.println(alphabets);
                System.out.println("Perfectly balanced RB BST? : " + alphabets.isPerfectBlackBalance());
                System.out.println("Valid Left Leaning Red Black Tree? : " + alphabets.isValid23LeftLeaningRedBlackTree());
            }
        }

        System.out.println("Enter range to count: ");
        int lo = scanner.nextInt();
        int hi = scanner.nextInt();
        System.out.println("Range count in [" + lo + ", " + hi + "] is : " + alphabets.rangeCount(lo, hi));
    }
}

/*
// removing color memory
class Node<K extends Comparable<K>, V> {
    private K[] keys;
    private V[] values;
    private Node<K, V>[] links;

    @SuppressWarnings("unchecked")
    public Node() {
        this.keys = (K[]) new Comparable[2];
        this.values = (V[]) new Object[2];
        this.links = (Node<K, V>[]) new Node[3];
    }

    @SuppressWarnings("unchecked")
    public Node(K[] keys, V[] values) {
        this.keys = keys;
        this.values = values;
        this.links = new Node[3];
    }

    public Node(K loKey, K hiKey, V loVal, V hiVal) {
        this();
        this.keys[0] = loKey; this.keys[1] = hiKey;
        this.values[0] = loVal; this.values[1] = hiVal;

    }

    private int linksCount() {
        int count = 0;
        for (Node<K, V> node : this.links) {
            if (node != null) {
                count++;
            }
        }
        return count;
    }

    public K getLoKey() {
        return this.keys[0];
    }

    public K getHiKey() {
        return this.keys[1];
    }

    public V getLoVal() {
        return this.values[0];
    }

    public V getHiVal() {
        return this.values[1];
    }

    public void setKeys(K[] keys) {
        this.keys = keys;
    }

    public V[] getValues() {
        return values;
    }

    public void setValues(V[] values) {
        this.values = values;
    }

    public boolean isTwoNode() {
        return linksCount() == 2;
    }

    public boolean isThreeNode() {
        return linksCount() == 3;
    }

    public Node<K, V> getLeft() {
        return this.links[0];
    }

    public Node<K, V> getRight() {
        return this.links[2];
    }

    public Node<K, V> getMiddle() {
        return this.links[1];
    }
}
*/
