package com.company;

public class QuickFindUF implements UF {
    // instead of actually forming sets of connected components, we only need to tie up the connected components with a common identifier
    private final int[] componentID;

    public QuickFindUF(int N) {
        this.componentID = new int[N];
        for (int i = 0; i < N; i++) {
            this.componentID[i] = i;
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return componentID[p] == componentID[q];
    }

    @Override
    public void union(int p, int q) {
        if (!connected(p, q)) {
            int from = componentID[p];
            int to = componentID[q];
            for (int i = 0; i < componentID.length; i++) {
                if (componentID[i] == from) {
                    componentID[i] = to;
                }
            }
        }
    }
}
