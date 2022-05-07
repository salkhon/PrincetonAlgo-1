package com.company;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;


public class MyBinarySearch {
    private int[] integers;
    private boolean reversed;

    public MyBinarySearch(int[] integers) {
        this.integers = integers;
        this.reversed = false;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public void setIntegers(int[] integers) {
        this.integers = integers;
    }

    public int find(int key) {
        return find(0, this.integers.length, key);
    }

    public int find(int fromIndex, int toIndex, int key) {
        int lo = fromIndex, hi = toIndex - 1;

        int ans = -1;

        while (lo <= hi) {
            int mid = (lo + hi) / 2;

            if (this.integers[mid] > key) {
                if (!reversed) {
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            } else if (this.integers[mid] < key) {
                if (!reversed) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            } else {
                ans = mid;
                break;
            }
        }

        return ans;
    }

    // test client
    public static void main(String[] args) {
        int[] integers = new In("8Kints.txt").readAllInts();
        Arrays.sort(integers);
        MyBinarySearch myBinarySearch = new MyBinarySearch(integers);

        long t1 = System.nanoTime();
        int result = myBinarySearch.find(20165);
        long t2 = System.nanoTime();

        System.out.println("Result: " + result);
        System.out.println("Run time: " + (t2 - t1) + " ns");
    }
}
