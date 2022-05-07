package com.company.elementarySort;

import edu.princeton.cs.algs4.StdRandom;

public class MyMergeSortBU {
    // the main purpose of recursion in merge sort was to organize the merge() calls - which is the basis of merge sort
    // if we approach by merging the base case tiny sub-arrays at first, then merging twice that size and so forth - we can organize the merge calls avoiding the recursion overhead
    // however that raises an issue of the length of the array not being the a multiple of the merge size, in that case the last sub-array might not be of the merging size
    // merging sub arrays of unequal length is not an issue for merge() as long as we can specify the appropriate mid -
    // because there is no dependency on the sub-arrays being equal in merge implementation

    // so our approach will be to check if the left sorted sub-array exists in full size, and then merge it with what ever that is left for the right sorted sub-array
    // whatever that's left means - if another equal mergeSize array is left merge that, else merge the smaller portion that's left (ending at values.length - 1)
    public static <T extends Comparable<T>> void sort(T[] values) {
        @SuppressWarnings("unchecked")
        T[] aux = (T[]) new Comparable<?>[values.length];

        for (int subArraySize = 1; subArraySize < values.length; subArraySize *= 2) {
            for (int i = 0; i + subArraySize < values.length; i += 2 * subArraySize) {
                if (MyMergeSort.less(values[i + subArraySize], values[i + subArraySize - 1])) {
                    MyMergeSort.mergeWithArrayCopy(values, i, i + subArraySize - 1, Math.min(i + 2 * subArraySize - 1, values.length - 1), aux);
                }
            }
        }
    }

    // test client
    public static void main(String[] args) {
        Integer[] integers = new Integer[] { 7, 10, 5, 3, 8, 4, 2, 9, 6 };
        Character[] characters = new Character[] { 'S', 'O', 'R', 'T', 'E', 'X', 'A', 'M', 'P', 'L', 'E' }; // 'M', 'E', 'R', 'G', 'E',
        System.out.println("number of inversions in \"sortexample\" is " + SortCompare.inversions(characters) + " \nis sorted? " + SortCompare.isSorted(characters));

        MyMergeSortBU.sort(integers);
        MyMergeSortBU.sort(characters);

        int SIZE = 100;
        Double[] doubles = new Double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            doubles[i] = StdRandom.uniform() * 100;
        }
        MyMergeSortBU.sort(doubles);

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
