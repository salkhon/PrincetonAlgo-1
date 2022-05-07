package com.search;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class MyBinarySearchTree<K extends Comparable<K>, V> implements Iterable<K>, SymbolTable<K, V> {
    private Node root;

    public MyBinarySearchTree() {
    }

    public void put(K key, V value) {
        this.root = put(key, value, this.root, 1); // if this.root is null and this is the first key
    }

    // recursive
    private Node put(K key, V value, Node current, int depth) {
        if (current == null) {
            // targetKey does not exist in BST, need to put new
            return new Node(key, value, depth);
        }
        if (MyBinSearchSymbolTable.less(key, current.getKey())) {
            current.setLeft(put(key, value, current.getLeft(), depth + 1));
        } else if (MyBinSearchSymbolTable.less(current.getKey(), key)) {
            current.setRight(put(key, value, current.getRight(), depth + 1));
        } else {
            current.setValue(value);
        }
        // to enable this size changing, recursive put() is necessary, could have been done iteratively by simulating call stack
        current.setSubtreePopulation(1 + size(current.getLeft()) + size(current.getRight())); // if new node with size one is added, the change is propagated down the call stack
        // 1 because, if both children are null, still the subtree population is conventionally 1 to count the parent
        current.setMaxHeightInSubtree(1 + Math.max(height(current.getLeft()), height(current.getRight())));
        return current;
    }

    public void putIter(K key, V value) {
        Stack<Node> pathStack = new Stack<>();
        Node current = this.root;
        int cmp;
        while (current != null) {
            pathStack.push(current);
            cmp = key.compareTo(current.getKey());
            if (cmp < 0) {
                current = current.getLeft();
            } else if (cmp > 0) {
                current = current.getRight();
            } else {
                current.setValue(value);
                break;
            }
        }
        if (current == null) {
            current = new Node(key, value, pathStack.size());
            Node parent;
            while (!pathStack.isEmpty()) {
                parent = pathStack.pop();
                cmp = current.compareTo(parent);
                if (cmp < 0) {
                    parent.setLeft(current);
                } else if (cmp > 0) {
                    parent.setRight(current);
                }
                parent.setSubtreePopulation(1 + size(parent.getLeft()) + size(parent.getRight()));
                parent.setMaxHeightInSubtree(1 + Math.max(height(parent.getLeft()), height(parent.getRight())));

                current = parent;
            }
            this.root = current;
        }
    }

    public V get(K key) {
        return get(key, this.root);
    }

    private V get(K key, Node current) {
        if (current == null) {
            return null;
        }
        int cmp = key.compareTo(current.getKey());
        V val;
        if (cmp < 0) {
            val = get(key, current.getLeft());
        } else if (cmp > 0) {
            val = get(key, current.getRight());
        } else {
            val = current.getValue(); // type arguments ought to be immutable
        }
        return val;
    }


    public V getIter(K key) {
        V val = null;
        Node current = this.root;
        while (current != null) {
            int cmp = key.compareTo(current.getKey());
            if (cmp < 0) {
                current = current.getLeft();
            } else if (cmp > 0) {
                current = current.getRight();
            } else {
                val = current.getValue();
            }
        }
        return val;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public K minKey() {
        Node current = this.root;
        if (current != null) {
            while (current.getLeft() != null) {
                current = current.getLeft();
            }
            return current.getKey();
        }
        return null;
    }

    public K maxKey() {
        Node current = this.root;
        if (current != null) {
            while (current.getRight() != null) {
                current = current.getRight();
            }
            return current.getKey();
        }
        return null;
    }

    // largest key smaller than target key
    public K floor(K key) {
        Node floorNode = floorNode(key);
        K floorKey = null;
        if (floorNode != null) {
            floorKey = floorNode.getKey();
        }
        return floorKey;
    }

    // put() also could have been done iteratively, but the size() increment based on new Node insertion requires call stack
    // floor() also implemented recursively might be necessary for some implementation
    private Node floorNode(K key) {
        Node floor = null;
        Node current = this.root;
        while (current != null) {
            if (MyBinSearchSymbolTable.less(key, current.getKey())) {
                current = current.getLeft();
            } else if (MyBinSearchSymbolTable.less(current.getKey(), key)) {
                //if (floor == null || MyBinSearchSymbolTable.less(floor, current.getKey())) {
                floor = current;
                current = current.getRight();
            } else {
                floor = current;
                break;
            }
        }
        return floor;
    }

    // smallest key larger than target key
    public K ceil(K key) {
        Node ceilNode = ceilNode(key);
        K ceilKey = null;
        if (ceilNode != null) {
            ceilKey = ceilNode.getKey();
        }
        return ceilKey;
    }

    private Node ceilNode(K key) {
        Node ceil = null;
        Node current = this.root;
        while (current != null) {
            if (MyBinSearchSymbolTable.less(current.getKey(), key)) {
                current = current.getRight();
            } else if (MyBinSearchSymbolTable.less(key, current.getKey())) {
                //if (ceil == null || MyBinSearchSymbolTable.less(current.getKey(), ceil)) { // redundant because current is guaranteed to be closer than any parent
                ceil = current;
                current = current.getLeft();
            } else {
                ceil = current;
                break;
            }
        }
        return ceil;
    }

    // simply returning the size() of floor key won't work because the tree orientation depends on insertion sequence
    // and the floors ancestors might be inside the rank, but won't be counted in floor's subtreePopulation
    public int rank(K key) {
        return rank(this.root, key);
    }

    // if current key is less than targetKey, that key and all the left link nodes are included in the rank and the right node needs to be analyzed further recursively
    // if the current key is greater, then it's left link nodes need to be analyzed further - recursively, the right link ones are not in the rank
    // if the current key is equal to the target key, then all the left link nodes are in the rank, but none of the right ones are
    private int rank(Node current, K key) {
        if (current == null) {
            return 0;
        }
        if (MyBinSearchSymbolTable.less(key, current.getKey())) {
            return rank(current.getRight(), key);
        } else if (MyBinSearchSymbolTable.less(current.getKey(), key)) {
            return 1 + size(current.getLeft()) + rank(current.getRight(), key);
        } else {
            return size(current.getLeft()); // not counting the equal key itself, so no 1 + ...
        }
    }

    public K select(int rank) {
        K targetKey = null;
        if (rank < size()) {
            targetKey = select(rank, this.root).getKey();
        }
        return targetKey;
    }

    private Node select(int rank, Node current) {
        Node target;
        if (rank < size(current.getLeft())) {
            target = select(rank, current.getLeft()); // we don't follow a link if we're not sure the rank exists in it
            // if the number of lesser items than the current key exceeds the required rank, then the rank target must exist in that left link
        } else if (rank > size(current.getLeft())) {
            target = select(rank - (size(current.getLeft()) + 1), current.getRight()); // the smaller keys are included in rank, so search rank is lowered
        } else {
            target = current; // we followed this link because we know the target rank exists in this path
        }
        return target;
    }

    public int size() {
        return size(this.root);
    }

    // can handle null situations
    private int size(Node node) {
        int size = 0;
        if (node != null) {
            size = node.getSubtreePopulation();
        }
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void delete(K key) {
        this.root = hibbardDelete(key, this.root);
    }

    private Node hibbardDelete(K key, Node current) {
        if (current == null) {
            return null;
        }

        if (MyBinSearchSymbolTable.less(current.getKey(), key)) {
            current.setRight(hibbardDelete(key, current.getRight()));
        } else if (MyBinSearchSymbolTable.less(key, current.getKey())) {
            current.setLeft(hibbardDelete(key, current.getLeft()));
        } else {
            if (current.getLeft() == null) {
                current = current.getRight(); // right could be null, in that case current will be deleted (nulled)
            } else if (current.getRight() == null) {
                current = current.getLeft();
            } else {
                // find successor or predecessor
                Node successor = findMin(current.getRight());
                successor.setRight(delMin(current.getRight())); // the right subtree of min is linked to the parent of min, and subtree populations updated
                successor.setLeft(current.getLeft());
                successor.setDepth(current.getDepth());
                current = successor;
            }
        }
        current.setSubtreePopulation(1 + size(current.getLeft()) + size(current.getRight()));
        current.setMaxHeightInSubtree(1 + Math.max(height(current.getLeft()), height(current.getRight())));
        return current;
    }

    private Node findMin(Node current) {
        Node min = null;
        if (current != null) {
            while (current.getLeft() != null) {
                current = current.getLeft();
            }
            min = current;
        }
        return min;
    }

    public void delMin() {
        if (this.root != null) {
            this.root = delMin(this.root);
        }
    }

    // have to update subtree population, so call stack is essential - like put()
    public Node delMin(Node current) {
        if (current.getLeft() == null) {
            decDepth(current.getRight());
            return current.getRight();
        }
        current.setLeft(delMin(current.getLeft()));
        current.setSubtreePopulation(1 + size(current.getLeft()) + size(current.getRight()));
        current.setMaxHeightInSubtree(1 + Math.max(height(current.getLeft()), height(current.getRight())));
        return current;
    }

    private void decDepth(Node current) {
        if (current == null) {
            return;
        }

        current.setDepth(current.getDepth() - 1);
        decDepth(current.getLeft());
        decDepth(current.getRight());
    }

    @Override
    public @NotNull Iterator<K> iterator() {
        Queue<K> keys = new Queue<>(); // to avoid keeping track of array index
        traverseTree(keys, this.root);
        return keys.iterator();
    }

    private void traverseTree(Queue<K> keys, Node current) {
        if (current == null) {
            return;
        }
        traverseTree(keys, current.getLeft());
        keys.enqueue(current.getKey());
        traverseTree(keys, current.getRight());
    }

    public Queue<K> keys(K lo, K hi) throws IllegalArgumentException {
        if (lo.compareTo(hi) > 0) {
            throw new IllegalArgumentException();
        }

        Queue<K> keyQueue = new Queue<>();
        keys(ceil(lo), floor(hi), this.root, keyQueue);
        return keyQueue;
    }

    public double avgCompares() {
        double avgComp = Double.POSITIVE_INFINITY;
        if (size() != 0) {
            avgComp = 1.0 + ((double) internalPathLength(this.root)) / size();
        }
        return avgComp;
    }

    private int internalPathLength(Node current) {
        if (current == null) {
            return 0;
        }
        return current.getDepth() + internalPathLength(current.getLeft()) + internalPathLength(current.getRight());
    }

    private void keys(K lo, K hi, Node current, Queue<K> keyQueue) {
        if (current == null) {
            return;
        }

        int loVsCurrent = lo.compareTo(current.getKey());
        int hiVsCurrent = hi.compareTo(current.getKey());

        if (loVsCurrent < 0) {
            keys(lo, hi, current.getLeft(), keyQueue);
        }

        if (loVsCurrent <= 0 && hiVsCurrent >= 0) {
            keyQueue.enqueue(current.getKey());
        }

        if (hiVsCurrent > 0) {
            keys(lo, hi, current.getRight(), keyQueue);
        }
    }

    public Iterator<K> keysIter(@NotNull K lo, @NotNull K hi) {
        return new KeysIterator(lo, hi);
    }

//    public int height() {
//        return height(this.root);
//    }
//
//    private int height(Node current) {
//        if (current == null) {
//            return 0;
//        }
//        return 1 + Math.max(height(current.getLeft()), height(current.getRight()));
//    }

    public int height() {
        return height(this.root); // might be height(this.root) - 1, according to interpretation of height()
    }

    private int height(Node node) {
        int height;
        if (node == null) {
            height = 0;
        } else {
            height = node.getMaxHeightInSubtree();
        }
        return height;
    }

    private class Node implements Comparable<Node> {
        private final K key;
        private V value;
        private Node left, right;
        private int subtreePopulation;
        private int maxHeightInSubtree;
        private int depth;

        public Node(K key, V value, int depth) {
            this.key = key;
            this.value = value;
            this.depth = depth;
            this.subtreePopulation = 1; // itself
            this.maxHeightInSubtree = 1;
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

        public int getMaxHeightInSubtree() {
            return maxHeightInSubtree;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public void setMaxHeightInSubtree(int maxHeightInSubtree) {
            this.maxHeightInSubtree = maxHeightInSubtree;
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
            return this.key + "-" + this.value;
        }
    }

    private class KeysIterator implements Iterator<K> {
        Stack<Node> pathStack;
        K lo, hi;
        boolean hasNext;

        public KeysIterator(K lo, K hi) {
            this.pathStack = new Stack<>();
            this.lo = lo;
            this.hi = hi;

            // equip the stack with path to ceil(lo)
            stackCeilOfLo(); // invariant: stack is always topped by next node, initially ceil(lo)

            if (!this.pathStack.isEmpty() && keyIsInRange(this.pathStack.peek().getKey())) {
                this.hasNext = true;
            }
        }

        private void stackCeilOfLo() {
            Node current = MyBinarySearchTree.this.root;
            while (current != null) {
                this.pathStack.push(current);
                int cmp = this.lo.compareTo(current.getKey());
                if (cmp < 0) {
                    current = current.getLeft();
                } else if (cmp > 0) {
                    current = current.getRight();
                } else {
                    break;
                }
            }
            // seeked for a lower ceil, but found null OR no key higher than lo
            if (current == null && !this.pathStack.isEmpty()) {
                do {
                    current = this.pathStack.pop();
                } while (this.lo.compareTo(current.getKey()) > 0 && !this.pathStack.isEmpty());

                if (this.lo.compareTo(current.getKey()) <= 0) {
                    this.pathStack.push(current);
                }
            }
        }

        private boolean keyIsInRange(K key) {
            return key.compareTo(this.lo) >= 0 && key.compareTo(this.hi) <= 0;
        }

        @Override
        public boolean hasNext() {
            return this.hasNext;
        }

        @Override
        public K next() {
            if (this.hasNext) {
                Node current = this.pathStack.pop();
                K nextKey = current.getKey();
                findAndStackSuccessor(current); // invariant: the stack is topped by the next node; if the successor is out of bounds, hasNext is set to false
                return nextKey;
            } else {
                throw new IllegalCallerException();
            }
        }

        private void findAndStackSuccessor(Node node) {
            K nodeKey = node.getKey();
            if (node.getRight() != null) {
                // find child successor
                findAndStackChildSuccessor(node);
                // if right child exists, it is lesser than any parent that might be greater than the node
            } else {
                // look for closest greater parent
                findAndStackParentSuccessor(node);
            }
        }

        private void findAndStackChildSuccessor(Node node) {
            this.pathStack.push(node);
            node = node.getRight();
            while (node != null) {
                this.pathStack.push(node);
                node = node.getLeft();
            }

            // minimum successor is greater than hi
            if (!keyIsInRange(this.pathStack.peek().getKey())) {
                this.hasNext = false;
            }
        }

        private void findAndStackParentSuccessor(Node node) {
            Node current;
            do {
                current = this.pathStack.pop();
            } while (node.compareTo(current) > 0 && !this.pathStack.isEmpty());

            if (keyIsInRange(current.getKey())) {
                this.pathStack.push(current);
            } else {
                this.hasNext = false;
            }
        }
    }

    // link back to parent needed, and link identifier (left of right)
    public void inorderTraversalConstantSpace() {

    }

    public boolean isBinaryTreeBST(Node root) {
        return isBinTreeBST(root);
    }

    private boolean isBinTreeBST(Node current) {
        if (current == null) {
            return true;
        }
        boolean BSTOrderExists = (current.getLeft() == null || current.getLeft().compareTo(current) < 0) &&
                (current.getRight() == null || current.getRight().compareTo(current) > 0);
        return BSTOrderExists && isBinTreeBST(current.getLeft()) && isBinTreeBST(current.getRight());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[ ");
        for (K key : this) {
            stringBuilder.append(key).append("-").append(get(key)).append(", ");
        }
        int lastComma = stringBuilder.lastIndexOf(",");
        if (lastComma >= 0) {
            stringBuilder.delete(lastComma, lastComma + 2);
        }
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }

    // breadth first
    public void levelOrderPrint() {
        if (!isEmpty()) {
            Queue<Node> nodeQueue = new Queue<>();
            nodeQueue.enqueue(this.root);

            Node current;
            while (!nodeQueue.isEmpty()) {
                current = nodeQueue.dequeue();
                System.out.print(current + "(" + current.getDepth() + ")" + " ");
                if (current.getLeft() != null) {
                    nodeQueue.enqueue(current.getLeft());
                }
                if (current.getRight() != null) {
                    nodeQueue.enqueue(current.getRight());
                }
            }
        }
    }

    private void enqueue(Queue<K> keyQueue) {

    }

    // test client
    public static void main(String[] args) {
        MyBinarySearchTree<Integer, Character> alphabets = new MyBinarySearchTree<>();
        final int SIZE = 100, LIM = 200;

        for (int i = 0; i < SIZE; i++) {
            alphabets.putIter(StdRandom.uniform(LIM), randomCharacter());
        }

        for (int i = 0; i < SIZE / 2; i++) {
            int random = StdRandom.uniform(LIM);
            System.out.println("Alphabet: key " + random + " values " + alphabets.get(random));
        }
        System.out.println(alphabets);
        System.out.println("Key of rank " + 6 + " is " + alphabets.select(6));

        int lo = 25, hi = 147;
        System.out.println("range query : " + lo + " to " + hi);
        for (Integer key : alphabets.keys(lo, hi)) {
            System.out.print(key + "-" + alphabets.get(key) + " ");
        }
        System.out.println();
        System.out.println("Range query iterative: ");
        Iterator<Integer> iterator = alphabets.keysIter(lo, hi);
        while (iterator.hasNext()) {
            int key = iterator.next();
            System.out.print(key + "-" + alphabets.get(key) + " ");
        }
        System.out.println();

        double a = Double.NaN, b = Double.NaN;
        Double x = a, y = b;
        System.out.println(a + " == " + b + " : " + (a == b));
        System.out.println(x + ".equals(" + y + ") : " + x.equals(y));
        a = 0.0; b = -0.0; x = a; y = b;
        System.out.println(a + " == " + b + " : " + (a == b));
        System.out.println(x + ".equals(" + y + ") : " + x.equals(y));

        System.out.println(alphabets.isBinTreeBST(alphabets.root));

        System.out.println("Tree inorder traversal: " + alphabets);
        System.out.println("Height: " + alphabets.height());
        System.out.println("Average Compares: " + alphabets.avgCompares());
        System.out.println("Level order print: ");
        alphabets.levelOrderPrint();
    }

    public static Character randomCharacter() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return chars.charAt(StdRandom.uniform(52));
    }
}
