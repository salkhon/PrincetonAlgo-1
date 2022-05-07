package com.company.elementarySort;

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class NonRecurQuickSort {
    public static <T extends Comparable<T>> void sort(T[] values) {
        sort(values, 0, values.length - 1);
    }

    public static <T extends Comparable<T>> void sort(T[] values, int lo, int hi) {
        if (lo >= hi) {
            return;
        }

        Stack<Boundary> arrayStack = new Stack<>(); // do the second now(pop it), first later
        arrayStack.push(new Boundary(lo, hi));
        Boundary boundary;
        do {
            boundary = arrayStack.pop();
            lo = boundary.getLo();
            hi = boundary.getHi();
            // the sort that is being executed now is removed, so the hassle of removing executed sort is not there

            if (lo < hi) {
                int pivot = partition(values, lo, hi);

                if (pivot - lo >= hi - pivot) {
                    arrayStack.push(new Boundary(lo, pivot - 1));
                    arrayStack.push(new Boundary(pivot + 1, hi));
                } else {
                    arrayStack.push(new Boundary(pivot + 1, hi));
                    arrayStack.push(new Boundary(lo, pivot - 1));
                }
            }
        } while (!arrayStack.isEmpty());
    }

    public static <T extends Comparable<T>> int partition(T[] values, int lo, int hi) {
        int ptrL = lo + 1, ptrG = hi;

        while (ptrL <= ptrG) {
            while (ptrL <= hi && values[ptrL].compareTo(values[lo]) < 0) {
                ptrL++;
            }

            while (values[ptrG].compareTo(values[lo]) > 0) {
                ptrG--;
            }

            if (ptrL <= ptrG) {
                MyInsertionSort.exchange(values, ptrL++, ptrG--);
            }
        }

        MyInsertionSort.exchange(values, lo, ptrG);
        return ptrG;
    }

    private static class Boundary {
        private int lo;
        private int hi;

        public Boundary(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
        }

        public int getLo() {
            return lo;
        }

        public int getHi() {
            return hi;
        }
    }

    // test client
    public static void main(String[] args) {
        Integer[] integers = { 4, 5, 6, 3, 5, 8, 2, 2, 5, 3, 6, 6, 90, 45, 23, 76, 46, 324, 6, 7, 65, 1, 65, 2, 66, 65, 34, 33, 45, 44, 87, 72, 5275, 2, 7, 2457, 68, 3568, 2457, 2, 4562 };
        NonRecurQuickSort.sort(integers);
        System.out.println(Arrays.toString(integers));
        System.out.println(SortCompare.meanTime("QuickIterative", 100000, 10));
    }
}
