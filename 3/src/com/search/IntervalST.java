package com.search;

public class IntervalST<K extends Comparable<K>, V> {
    private Node root;

    public IntervalST() {
    }

    public Node getAnIntervalIntersection(K lo, K hi) {
        Node target = null;
        if (!isEmpty()) {
            target = getAnIntervalIntersection(lo, hi, this.root);
        }
        return target;
    }

    private Node getAnIntervalIntersection(K lo, K hi, Node current) {
        if (current == null) {
            return null;
        }

        if (intersects(lo, hi, current)) {
            // return current
        } else if (current.getLeft() == null) {
            current = getAnIntervalIntersection(lo, hi, current.getRight());
        } else if (current.getLeft().getMaxInSubtree().compareTo(lo) < 0) {
            current = getAnIntervalIntersection(lo, hi, current.getRight());
        } else {
            current = getAnIntervalIntersection(lo, hi, current.getLeft());
        }
        return current;
    }

    // 4 types of intersection:         |------|        |--------|          |-------|              |-------|        interval in tree
    //                              |------|              |----|                |------|        |-------------|     query
    private boolean intersects(K lo, K hi, Node current) {
        int cmpQueryLoIntervalLo = lo.compareTo(current.getLo());
        int cmpQueryLoIntervalHi = lo.compareTo(current.getHi());
        int cmpQueryHiIntervalLo = hi.compareTo(current.getLo());

        return cmpQueryLoIntervalLo < 0 && cmpQueryHiIntervalLo > 0 || // covers for 1 and 4
                cmpQueryLoIntervalLo > 0 && cmpQueryLoIntervalHi < 0; // covers for 2 and 3
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    private class Node {
        K lo, hi;
        V val;
        K maxInSubtree;
        Node left, right;

        public Node(K lo, K hi, V val) {
            this.lo = lo;
            this.hi = hi;
            this.val = val;
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

        public K getLo() {
            return lo;
        }

        public void setLo(K lo) {
            this.lo = lo;
        }

        public K getHi() {
            return hi;
        }

        public void setHi(K hi) {
            this.hi = hi;
        }

        public V getVal() {
            return val;
        }

        public void setVal(V val) {
            this.val = val;
        }

        public K getMaxInSubtree() {
            return maxInSubtree;
        }

        public void setMaxInSubtree(K maxInSubtree) {
            this.maxInSubtree = maxInSubtree;
        }
    }
}
