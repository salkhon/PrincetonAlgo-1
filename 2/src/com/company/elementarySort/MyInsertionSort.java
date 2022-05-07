package com.company.elementarySort;

import edu.princeton.cs.algs4.StdRandom;

public class MyInsertionSort {
    // <? extends Comparable<?>> does not enforce that ? is comparable with itself, two ? can be different
//    public static <T extends Comparable<T>> void sort(T[] values) {
//        for (int i = 0; i < values.length; i++) {
//            int j = i;
//            while (j > 0 && less(values[j], values[j - 1])) {
//                exchange(values, j, j - 1);
//                j--;
//            }
//        }
//    }
    public static <T extends Comparable<T>> void sort(T[] a, int lo, int hi) { // Sort a[] into increasing order.
        for (int i = lo; i <= hi; i++) { // Insert a[i] among a[i-1], a[i-2], a[i-3]... ..
            for (int j = i; j > lo && less(a[j], a[j-1]); j--)
                exchange(a, j, j-1);
        }
    }

    public static <T extends Comparable<T>> void sort(T[] value) {
        sort(value, 0, value.length - 1);
    }

    // both types have to be the same, wildcard ? does not enforce that
    public static <T extends Comparable<T>> boolean less(T o1, T o2) {
        return o1.compareTo(o2) < 0;
    }

    public static void exchange(Comparable<?>[] values, int index1, int index2) {
        Comparable<?> temp = values[index1];
        values[index1] = values[index2];
        values[index2] = temp;
    }

    // test client
    public static void main(String[] args) {
        Integer[] integers = new Integer[] { 7, 10, 5, 11, 56, 24, 3, 8, 4, 2, 9, 6 };
        Character[] characters = new Character[] { 'S', 'O', 'R', 'T', 'E', 'X', 'A', 'M', 'P', 'L', 'E' };
        MyInsertionSort.sort(integers);
        MyInsertionSort.sort(characters);
        for (int i : integers) {
            System.out.print(i +  " ");
        }
        System.out.println();
        for (char c : characters) {
            System.out.print(c + " ");
        }

        int SIZE = 100;
        Double[] doubles = new Double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            doubles[i] = StdRandom.uniform() * 100;
        }
        MyInsertionSort.sort(doubles);
        System.out.println("\nIs doubles sorted? : " + SortCompare.isSorted(doubles));
    }
}
