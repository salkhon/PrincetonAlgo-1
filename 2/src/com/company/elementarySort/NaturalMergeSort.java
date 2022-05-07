package com.company.elementarySort;

import edu.princeton.cs.algs4.StdRandom;

public class NaturalMergeSort {
    public static <T extends Comparable<T>> void sort(T[] values) {
        @SuppressWarnings("unchecked")
        T[] aux = (T[]) new Comparable<?>[values.length];
        sort(values, 0, values.length - 1, aux);
    }

    public static <T extends Comparable<T>> void sort(T[] values, int lo, int hi, T[] aux) {
        boolean mergedAtLeastOnce;

        do {
            mergedAtLeastOnce = false;

            int loVar = lo;
            int i = lo + 1;
            int midVar;
            while (i <= hi) {
                while (i <= hi && !MyMergeSort.less(values[i], values[i - 1])) i++;
                if (i > hi) break; // no merge pair

                midVar = i++ - 1;

                while (i <= hi && !MyMergeSort.less(values[i], values[i - 1])) i++;

                MyMergeSort.mergeWithArrayCopy(values, loVar, midVar, i - 1, aux);
                mergedAtLeastOnce = true;

                loVar = i++;
            }
        } while (mergedAtLeastOnce);
    }

    // test client
    public static void main(String[] args) {
        Integer[] integers = new Integer[] { 7, 10, 5, 3, 8, 4, 2, 9, 6 };
        Character[] characters = new Character[] { 'S', 'O', 'R', 'T', 'E', 'X', 'A', 'M', 'P', 'L', 'E' }; // 'M', 'E', 'R', 'G', 'E',
        System.out.println("number of inversions in \"sortexample\" is " + SortCompare.inversions(characters) + " \nis sorted? " + SortCompare.isSorted(characters));

        NaturalMergeSort.sort(integers);
        NaturalMergeSort.sort(characters);

        int SIZE = 100;
        Double[] doubles = new Double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            doubles[i] = StdRandom.uniform() * 100;
        }
        NaturalMergeSort.sort(doubles);

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
        System.out.println("NaturalMergeSort: " + SortCompare.meanTime("NaturalMerge", 100000, 10));
        System.out.println("Merge: " + SortCompare.meanTime("Merge", 100000, 10));
        System.out.println("MergeBU: " + SortCompare.meanTime("MergeBU", 100000, 10));
    }
}
