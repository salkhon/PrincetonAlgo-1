package com.company.elementarySort;

import java.util.ArrayList;

public class MySelectionSort {

    //    public static <E extends Comparable<E>> void sort(E[] values) {
//        int min;
//        for (int i = 0; i < values.length; i++) {
//            min = i;
//            for (int j = i + 1; j < values.length; j++) {
//                if (less(values[j], values[min])) {
//                    min = j;
//                }
//            }
//            exchange(values, i, min);
//        }
//    }
    public static <T extends Comparable<T>> void sort(T[] a) { // Sort a[] into increasing order.
        int N = a.length; // array length
        for (int i = 0; i < N; i++) { // Exchange a[i] with smallest entry in a[i+1...N).
            int min = i; // index of minimal entr.
            for (int j = i + 1; j < N; j++)
                if (less(a[j], a[min])) min = j;
            exchange(a, i, min);
        }
    }

    private static <E extends Comparable<E>> boolean less(E o1, E o2) {
        return o1.compareTo(o2) < 0;
    }

    // to access heap, you need to access member, an to access member you need an object that is consistent with the calling method
    // here array is the consistent object, and it's members are its entries
    private static <E extends Comparable<E>> void exchange(Comparable<E>[] values, int index1, int index2) {
        Comparable<E> temp = values[index1];
        values[index1] = values[index2];
        values[index2] = temp;
    }

    // test client
    public static void main(String[] args) {
        Integer[] arr = new Integer[]{7, 10, 5, 3, 8, 4, 2, 9, 6};

        MySelectionSort.sort(arr);

        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
