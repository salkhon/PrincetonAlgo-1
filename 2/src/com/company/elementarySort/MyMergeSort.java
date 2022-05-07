package com.company.elementarySort;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class MyMergeSort {
    private static final int CUTOFF = 7;

//    public enum Approach { TopDown, BottomUp }

    // non recursive method, that initiates the recursion
    // creating aux[] here will save merge sort from recursive allocation and copying
    // creating aux[] in recursive process is a performance bug that slows down merge sort
    public static <T extends Comparable<T>> void sort(T[] values) {
        @SuppressWarnings("unchecked")
        T[] aux = (T[]) new Comparable[values.length];
        System.arraycopy(values, 0, aux, 0, values.length); // when the recursive call hits the base-case both value[] and aux[] are identical
        sortTopDown(values, 0, values.length - 1, aux);

//        if (approach.equals(Approach.TopDown)) {
//            sortTopDown(values, 0, values.length - 1, aux);
//        } else if (approach.equals(Approach.BottomUp)) {
//            sortBottomUp(values, aux);
//        }
    }

    // hi is inclusive
    private static <T extends Comparable<T>> void merge(T[] values, int lo, int mid, int hi, T[] aux) {
//        assert SortCompare.isSorted(values, lo, mid);
//        assert SortCompare.isSorted(values, mid + 1, hi);

        // assumes aux[] contains half sorted sub-arrays - the alternation between value[] and aux[] occurs in sort(), merge() is unaware

        int i = lo, ptrL = lo, ptrR = mid + 1;
        while (i <= hi) {
            if (ptrL > mid) {
                values[i++] = aux[ptrR++];
            } else if (ptrR > hi) {
                values[i++] = aux[ptrL++];
            } else if (less(aux[ptrR], aux[ptrL])) {
                values[i++] = aux[ptrR++];
            } else {
                values[i++] = aux[ptrL++];
            }
        }

//        assert SortCompare.isSorted(values, lo, hi);
    }

    public static <T extends Comparable<T>> boolean less(T o1, T o2) {
        return o1.compareTo(o2) < 0;
    }

    // Top - Down recursive approach:
    public static <T extends Comparable<T>> void sortTopDown(T[] values, int lo, int hi, T[] aux) {
        if (hi - lo + 1 <= CUTOFF) {
            MyInsertionSort.sort(values, lo, hi);
            return;
        }

        int mid = (lo + hi) / 2;

        sortTopDown(aux, lo, mid, values);
        sortTopDown(aux, mid + 1, hi, values);
        // sorted sub arrays now exist in aux[]

        if (!less(aux[mid + 1], aux[mid])) {
            System.arraycopy(aux, lo, values, lo, hi - lo + 1);
            return;
        }
        merge(values, lo, mid, hi, aux);
    }

    // Bottom-up iterative approach
//    private static <T extends Comparable<T>> void sortBottomUp(T[] values, T[] aux) {
//        for (int i = 2; i <= values.length; i *= 2) {
//            for (int j = 0; j + i <= values.length; j += i) {
//                mergeWithArrayCopy(values, j, (j + j + i - 1) / 2, j + i - 1, aux);
//            }
//            // left over
//            if (values.length % i > i / 2) {
//                int lo = values.length - (values.length % i), mid = lo + i / 2 - 1;
//                mergeWithArrayCopy(values, lo, mid, values.length - 1, aux);
//            }
//
//            if (i * 2 > values.length) {
//                mergeWithArrayCopy(values, 0, i - 1, values.length - 1, aux);
//            }
//        }
//    }

    public static <T extends Comparable<T>> void mergeWithArrayCopy(T[] values, int lo, int mid, int hi, T[] aux) {
        assert SortCompare.isSorted(values, lo, mid);
        assert SortCompare.isSorted(values, mid + 1, hi);

        //System.arraycopy(values, lo, aux, lo, hi - lo + 1);
        // aux[] size half as the value[] - although not helpful, because we don't create the array in merge() because of recursive allocation cost
        System.arraycopy(values, lo, aux, lo, mid - lo + 1);

        int i = lo, ptrL = lo, ptrR = mid + 1;
        while (i <= hi) {
            if (ptrL > mid) {
                // values[i++] = aux[ptrR++];
                values[i++] = values[ptrR++];
            } else if (ptrR > hi) {
                values[i++] = aux[ptrL++];
            } else if (less(values[ptrR], aux[ptrL])) {
                values[i++] = values[ptrR++];
            } else {
                values[i++] = aux[ptrL++];
            }
        }
        assert SortCompare.isSorted(values, lo, hi);
    }

    // reduces the aux[] sub-array exhaustion ptr test code
    // copying the left sorted sub-array in decreasing order ensures that when a pointer exhausts a sub-array, it will reach the largest entry of the other sub-array -
    // which can never be less than what the other sub-array pointer is currently at (if it is not exhausted too)
    // unstable because ptrL can exhaust and reach the largest entry of the right sub-array, which might be equal to the ptrR pointed entry
    // that case because og ptrL preference in the case of equality, the order is broken
    public static <T extends Comparable<T>> void unstableMerge(T[] values, int lo, int mid, int hi, T[] aux) {
        System.arraycopy(values, lo, aux, lo, mid - lo + 1);
        for (int i = hi; i > mid; i--) {
            aux[mid + 1 + (hi - i)] = values[i]; // sorted array copied in decreasing order
        }

        int i = lo, ptrL = lo, ptrR = hi;
        while (i <= hi) {
            if (less(aux[ptrR], aux[ptrL])) {
                values[i++] = aux[ptrR--];
            } else {
                values[i++] = aux[ptrL++];
            }
        }
    }

    // test client
    public static void main(String[] args) {
        Integer[] integers = new Integer[] { 7, 10, 5, 3, 8, 4, 2, 9, 6 };
        Character[] characters = new Character[] { 'S', 'O', 'R', 'T', 'E', 'X', 'A', 'M', 'P', 'L', 'E' }; // 'M', 'E', 'R', 'G', 'E',
        System.out.println("number of inversions in \"sortexample\" is " + SortCompare.inversions(characters) + " \nis sorted? " + SortCompare.isSorted(characters));

        MyMergeSort.sort(integers);
        MyMergeSort.sort(characters);

        int SIZE = 10000;
        Double[] doubles = new Double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            doubles[i] = StdRandom.uniform() * 100;
        }
        MyMergeSort.sort(doubles);

        for (int i : integers) {
            System.out.print(i +  " ");
        }
        System.out.println();
        for (char c : characters) {
            System.out.print(c + " ");
        }
        System.out.println();
        for (double d : doubles) {
            System.out.print(d + " ");
        }
        System.out.println("\nIs doubles sorted? : " + SortCompare.isSorted(doubles));
    }
}
