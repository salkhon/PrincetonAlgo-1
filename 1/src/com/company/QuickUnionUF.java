package com.company;

public class QuickUnionUF implements UF {
    // the defect of quick find was the entire array traversal for union, quick union tackles that by making identifiers of nodes leading to connected nodes - like trees
    // a roots parent is itself, that is how we identify roots
    private final int[] parentID;

    public QuickUnionUF(int N) {
        this.parentID = new int[N];
        for (int i = 0; i < N; i++) {
            this.parentID[i] = i;
        }
    }

    @Override
    public void union(int p, int q) {
        if (p != q) {
            int rootOfp = rootOf(p);
            int rootOfq = rootOf(q);
            this.parentID[rootOfp] = rootOfq; // one connected component tree's root has a parent in it's union connected component's root - as a result they have the same root after union
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return rootOf(p) == rootOf(q);
    }

    private int rootOf(int n) {
        int i = n;
        while (parentID[i] != i) {
            i = parentID[i];
        }
        return i;
    }
}
