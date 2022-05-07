package com.company.elementarySort;

import edu.princeton.cs.algs4.StdRandom;

public class MyKnuthShuffle {
    // insertion shuffle
    public static void shuffle(Object[] values) {
        for (int i = 0; i < values.length; i++) {
            exchange(values, i, StdRandom.uniform(i + 1));
        }
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
        MyKnuthShuffle.shuffle(integers);
        MyKnuthShuffle.shuffle(characters);
        for (int i : integers) {
            System.out.print(i +  " ");
        }
        System.out.println();
        for (char c : characters) {
            System.out.print(c + " ");
        }
    }
}
