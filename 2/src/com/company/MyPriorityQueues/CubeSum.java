package com.company.MyPriorityQueues;

import org.jetbrains.annotations.NotNull;

public class CubeSum {
    private int N;
    private final MyMinPriorityQueue<Triplet> tripletMyMinPriorityQueue;

    public CubeSum(int N) {
        this.N = N;
        this.tripletMyMinPriorityQueue = new MyMinPriorityQueue<>();
        for (int i = 0; i <= N; i++) {
            this.tripletMyMinPriorityQueue.insert(new Triplet(i, 0));
        }
    }

    // space proportional to N
    public void printCubeSumsWithoutExcessSpace() {
        Triplet minTriplet;
        while (!this.tripletMyMinPriorityQueue.isEmpty()) {
            minTriplet = this.tripletMyMinPriorityQueue.delMin();
            System.out.println(minTriplet);
            if (minTriplet.getY() < this.N) {
                this.tripletMyMinPriorityQueue.insert(new Triplet(minTriplet.getX(), minTriplet.getY() + 1));
                // inserted one will always be greater than the deleted one, so if the next min is equal to this, we have a taxicab number
            }
        }
    }

    private static class Triplet implements Comparable<Triplet> {
        private final int sum;
        private final int x, y;

        public Triplet(int x, int y) {
            this.x = x;
            this.y = y;
            this.sum = x * x * x + y * y * y;
        }

        @Override
        public int compareTo(@NotNull CubeSum.Triplet o) {
            return Integer.compare(this.sum, o.sum);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return this.x + " + " + this.y + " = " + this.sum;
        }
    }

    // test client
    public static void main(String[] args) {
        CubeSum cubeSum = new CubeSum(100);
        cubeSum.printCubeSumsWithoutExcessSpace();
    }
}
