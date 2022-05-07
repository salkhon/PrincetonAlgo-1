package com.company.elementarySort;

import java.util.Arrays;

public class MyHeapSort {
    public static <T extends Comparable<T>> void sort(T[] values) {
        sort(values, 0, values.length - 1);
    }

    public static <T extends Comparable<T>> void sort(T[] values, int lo, int hi) {
        bottomUpHeapOrder(values, lo, hi);
        //topDownHeapOrder(values, lo, hi);
        for (int hiVar = hi; hiVar > lo; hiVar--) {
            sinkDown(values, lo, hiVar, lo); // putting this first to avoid changing loop variable inside for loop - first sinkDown() call will be rejected anyway
            MyInsertionSort.exchange(values, lo, hiVar);
        }
    }

    private static <T extends Comparable<T>> void sinkDown(T[] values, int lo, int hi, int k) {
        int childrenOfk = childrenOf(k);
        while ((childrenOfk <= hi && MyInsertionSort.less(values[k], values[childrenOfk]) ||
                (childrenOfk + 1 <= hi && MyInsertionSort.less(values[k], values[childrenOfk + 1])))) {
            if (childrenOfk + 1 <= hi && MyInsertionSort.less(values[childrenOfk], values[childrenOfk + 1])) {
                childrenOfk++;
            }

            MyInsertionSort.exchange(values, k, childrenOfk);
            k = childrenOfk;
            childrenOfk = childrenOf(k);
        }
    }

    private static <T extends Comparable<T>> void swimUp(T[] values, int lo, int hi, int k) {
        int parentOfk = parentOf(k);
        while (k > lo && MyInsertionSort.less(values[parentOfk], values[k])) {
            MyInsertionSort.exchange(values, parentOfk, k);
            k = parentOfk;
            parentOfk = parentOf(k);
        }
    }

    // going to demote lesser parents
    private static <T extends Comparable<T>> void bottomUpHeapOrder(T[] values, int lo, int hi) {
        // last node is hi, so last parent is parentOf(hi)
        for (int i = parentOf(hi); i >= lo; i--) {
            sinkDown(values, lo, hi, i);
        }
    }

    private static <T extends Comparable<T>> void topDownHeapOrder(T[] values, int lo, int hi) {
        for (int i = lo; i <= hi; i++) {
            swimUp(values, lo, i, i);
        }
    }

    private static int childrenOf(int k) {
        return (k + 1) * 2 - 1;
    }

    private static int parentOf(int k) {
        return (k + 1) / 2 - 1;
    }

    // test client
    public static void main(String[] args) {
        Character[] word = { 'B', 'A', 'B', 'A', 'B', 'A', 'B', 'A', 'C', 'A', 'D', 'A', 'B', 'R', 'A', 'X', 'W', 'W', 'W', 'M', 'M' };
        MyHeapSort.sort(word);
        System.out.println(Arrays.toString(word));
    }
}
