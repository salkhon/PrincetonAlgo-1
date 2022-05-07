package com.company;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSumWithBinSearch {
    private int[] integers;

    public ThreeSumWithBinSearch(int[] integers) {
        this.integers = integers;
    }

    public void setIntegers(int[] integers) {
        this.integers = integers;
    }

    public List<Integer[]> getZeroTriples() {
        List<Integer[]> results = new ArrayList<>();
        MyBinarySearch myBinarySearch = new MyBinarySearch(this.integers);

        for (int i = 0; i < this.integers.length - 2; i++) {
            for (int j = i + 1; j < this.integers.length - 1; j++) {
                // bin search space remains at least of length 1
                int index = myBinarySearch.find(j + 1, this.integers.length, -(this.integers[i] + this.integers[j]));
//                int index = Arrays.binarySearch(this.integers, j + 1, this.integers.length, -(this.integers[i] + this.integers[j]));
                if (index >= 0) {
                    Integer[] temp = { this.integers[i], this.integers[j], this.integers[index] };
                    results.add(temp);
                }
            }
        }

        return results;
    }

    // test client
    public static void main(String[] args) {
        int[] integers = new In("8Kints.txt").readAllInts();
        Arrays.sort(integers);

        ThreeSumWithBinSearch threeSumWithBinSearch = new ThreeSumWithBinSearch(integers);

        List<Integer[]> zeroTriplets = null;

        final int TESTS = 5;
        int[] times = new int[TESTS];
        for (int i = 0; i < TESTS; i++) {
            long t1 = System.currentTimeMillis();
            zeroTriplets = threeSumWithBinSearch.getZeroTriples();
            long t2 = System.currentTimeMillis();
            times[i] = (int) ((int) t2 - t1);
        }

        zeroTriplets.forEach((triplet) -> System.out.println(triplet[0] + " " + triplet[1] + " " + triplet[2]));
        System.out.println("Count: " + zeroTriplets.size());
        System.out.println("Runtime: " + StdStats.mean(times) + " ms");
    }
}
