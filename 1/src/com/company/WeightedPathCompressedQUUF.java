package com.company;

public class WeightedPathCompressedQUUF implements UF {
    private final int[] parentID;
    private final int[] treeSizeAtRoot;
    private int rootCount;

    public WeightedPathCompressedQUUF(int N) {
        this.parentID = new int[N];
        this.treeSizeAtRoot = new int[N];
        this.rootCount = N;

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
            if (rootOfp == rootOfq) {
                return;
            }

            if (this.treeSizeAtRoot[p] >= this.treeSizeAtRoot[q]) {
                this.parentID[rootOfq] = rootOfp;
                this.treeSizeAtRoot[rootOfp] += this.treeSizeAtRoot[rootOfq];
            } else {
                this.parentID[rootOfp] = rootOfq;
                this.treeSizeAtRoot[rootOfq] += this.treeSizeAtRoot[rootOfp];
            }

            this.rootCount--;
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return rootOf(p) == rootOf(q);
    }

    private int rootOf(int n) {
        int i = n;
        while (this.parentID[i] != i) {
            i = this.parentID[i];
        }
        // path compression - since we know the root of the nodes we traversed, join them directly to the root
        // later if have to join the root to another root, we can still reach the bigger root, but with much less distance between root-node
        int root = i;
        i = n;
        int parentIDVar;
        while (this.parentID[i] != i) {
            parentIDVar = this.parentID[i];
            this.parentID[i] = root;
            i = parentIDVar;
        }

        return root;
    }

    public boolean isAllNodeConnected() {
        return this.rootCount == 1;
    }
}

// for problems:

// find(i) to return max node in connected component containing i
//public class WeightedPathCompressedQUUF implements UF {
//    private final int[] parentID;
//    private final int[] treeSizeAtRoot;
//    private final int[] largestNodeAtRoot;
//
//    public WeightedPathCompressedQUUF(int N) {
//        this.parentID = new int[N];
//        this.treeSizeAtRoot = new int[N];
//        this.largestNodeAtRoot = new int[N];
//        for (int i = 0; i < N; i++) {
//            this.parentID[i] = i;
//            this.treeSizeAtRoot[i] = 1;
//            this.largestNodeAtRoot[i] = i;
//        }
//    }
//
//    public int find(int i) {
//        return this.largestNodeAtRoot[rootOf(i)];
//    }
//
//    @Override
//    public void union(int p, int q) {
//        if (p != q) {
//            int rootOfp = rootOf(p);
//            int rootOfq = rootOf(q);
//            if (rootOfp == rootOfq) {
//                return;
//            }
//
//            if (treeSizeAtRoot[p] >= treeSizeAtRoot[q]) {
//                this.parentID[rootOfq] = rootOfp;
//                treeSizeAtRoot[rootOfp] += treeSizeAtRoot[rootOfq];
//                this.largestNodeAtRoot[rootOfp] = Math.max(largestNodeAtRoot[rootOfp], largestNodeAtRoot[rootOfq]);
//            } else {
//                this.parentID[rootOfp] = rootOfq;
//                this.treeSizeAtRoot[rootOfq] += treeSizeAtRoot[rootOfp];
//                this.largestNodeAtRoot[rootOfq] = Math.max(largestNodeAtRoot[rootOfp], largestNodeAtRoot[rootOfq]);
//            }
//        }
//    }
//
//    @Override
//    public boolean connected(int p, int q) {
//        return rootOf(p) == rootOf(q);
//    }
//
//    private int rootOf(int n) {
//        int i = n;
//        while (parentID[i] != i) {
//            i = parentID[i];
//        }
//
//        int root = i;
//        i = n;
//        int parentIDVar;
//        while (parentID[i] != i) {
//            parentIDVar = parentID[i];
//            parentID[i] = root;
//            i = parentIDVar;
//        }
//
//        return root;
//    }
//}

//class RemoveSuccessor {
//    private final int[] successor;
//    private final int[] predecessor;
//
//    public RemoveSuccessor(int N) {
//        this.predecessor = new int[N];
//        this.successor = new int[N];
//
//        for (int i = 0; i < N; i++) {
//            this.predecessor[i] = (i == 0) ? -1 : i - 1;
//            this.successor[i] = (i == N - 1) ? -1 : i + 1;
//        }
//    }
//
//    public void remove(int x) {
//        if (this.predecessor[x] != -1) {
//            this.successor[this.predecessor[x]] = this.successor[x];
//        }
//        if (this.successor[x] != -1) {
//            this.predecessor[this.successor[x]] = this.predecessor[x];
//        }
//
//        this.successor[x] = -1;
//        this.predecessor[x] = -1;
//    }
//
//    public int successorOf(int x) {
//        return this.successor[x];
//    }
//}



