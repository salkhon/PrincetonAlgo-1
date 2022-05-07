package com.company;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {
    private int[] integers;

    public ThreeSum(int[] integers) {
        this.integers = integers;
    }

    public void setIntegers(int[] integers) {
        this.integers = integers;
    }

    public List<Integer[]> findThreeSumZeros() {
        List<Integer[]> results = new ArrayList<>();
        if (this.integers.length >= 3) {
            for (int i = 0; i < this.integers.length - 2; i++) {
                for (int j = i + 1; j < this.integers.length - 1; j++) {
                    for (int k = j + 1; k < this.integers.length; k++) {
                        if (this.integers[i] + this.integers[j] + this.integers[k] == 0) {
                            results.add(new Integer[]{ this.integers[i], this.integers[j], this.integers[k] });
                        }
                    }
                }
            }
        } // combinatorial algorithm: takes Nc3 iterations

        return results;
    }

    // test client
    public static void main(String[] args) {
        In in = new In("4Kints.txt");
        int[] integers = in.readAllInts();
        ThreeSum threeSum = new ThreeSum(integers);

        List<Integer[]> zeroTriplets = null;

        final int TESTS = 5;
        int[] times = new int[TESTS];
        for (int i = 0; i < TESTS; i++) {
            long t1 = System.currentTimeMillis();
            zeroTriplets = threeSum.findThreeSumZeros();
            long t2 = System.currentTimeMillis();
            times[i] = (int) ((int) t2 - t1);
        }

        for (Integer[] triplet : zeroTriplets) {
            System.out.println(triplet[0] + " " + triplet[1] + " " + triplet[2]);
        }

        System.out.println("Count: " + zeroTriplets.size());

        System.out.println("Running time: " + StdStats.mean(times) + " milli seconds");
    }
}
