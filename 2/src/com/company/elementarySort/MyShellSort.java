package com.company.elementarySort;

public class MyShellSort {
    public static <T extends Comparable<T>> void sort(T[] values) {
        // we'll use increment sequence of 3x + 1
        int x = 0;
        do {
            x = 3 * x + 1;
        } while (3 * x + 1 <= values.length);

        while (x > 0) {
            x = (x - 1) / 3;
            int h = 3 * x + 1;
            // first h values don't have previous sequences to compare with
            for (int i = h; i < values.length; i++) {
                int j = i;
                while (j >= h && less(values[j], values[j - h])) {
                    exchange(values, j, j - h);
                    j -= h;
                }
            }
        }
    }

    private static <T extends Comparable<T>> boolean less(T o1, T o2) {
        return o1.compareTo(o2) < 0;
    }

    private static void exchange(Object[] values, int index1, int index2) {
        Object temp = values[index1];
        values[index1] = values[index2];
        values[index2] = temp;
    }

    // test client
    public static void main(String[] args) {
        Integer[] integers = new Integer[] { 7, 10, 5, 3, 8, 4, 2, 9, 6 };
        Character[] characters = new Character[] { 'S', 'O', 'R', 'T', 'E', 'X', 'A', 'M', 'P', 'L', 'E' };
        MyShellSort.sort(integers);
        MyShellSort.sort(characters);
        for (int i : integers) {
            System.out.print(i +  " ");
        }
        System.out.println();
        for (char c : characters) {
            System.out.print(c + " ");
        }
    }
}
