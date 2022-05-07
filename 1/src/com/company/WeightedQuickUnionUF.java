package com.company;

public class WeightedQuickUnionUF implements UF {
    private final int[] parentID;
    private final int[] treeSizeAtRoot;

    public WeightedQuickUnionUF(int N) {
        this.parentID = new int[N];
        this.treeSizeAtRoot = new int[N];

        for (int i = 0; i < N; i++) {
            this.parentID[i] = i;
            this.treeSizeAtRoot[i] = 1;
        }
    }

    @Override
    public void union(int p, int q) {
        if (p != q) {
            int rootOfp = rootOf(p);
            int rootOfq = rootOf(q);
            // in just QU we did not have to check for equality of roots because we only assigned the parent of the root to itself, which would not affect it's root status
            // but in WQU we also modify the treeSizeAtRoot, so in the case of equal roots, the nodes are already connected and treeSizeAtRoot can't change
            if (rootOfp == rootOfq) {
                return; // no linking
            }

            if (treeSizeAtRoot[rootOfp] >= treeSizeAtRoot[rootOfq]) {
                parentID[rootOfq] = rootOfp;
                // rootOfp remains a root
                this.treeSizeAtRoot[rootOfp] += this.treeSizeAtRoot[rootOfq];
                /*
                * Notice that the whole population of the connected component tree is stored for a node in treeSizeAtRoot array and not the size of the longest arm
                * When performing union, one connected component's root becomes the 1st node family member of another connected component tree
                * The connected component tree which was linked, all it's nodes have now one more arm to traverse before reaching their new tree's root
                * By making the tree population as the comparison parameter to decide which connected component tree's node to root distance should be incremented
                * we minimize the average root to node distance by incrementing the tree with fewer nodes. Because we are choosing fewer nodes to increment root-node distance
                * greedily, our average root-node distance is minimized */
            } else {
                this.parentID[rootOfp] = rootOfq;
                // rootOfq remains a root
                this.treeSizeAtRoot[rootOfq] += this.treeSizeAtRoot[rootOfp];
            }
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return rootOf(p) == rootOf(q);
    }

    private int rootOf(int n) {
        int i = n;
        while (this.parentID[i] != i) {
            i = parentID[i];
        }
        return i;
    }
}
