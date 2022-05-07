package com.company;

import edu.princeton.cs.algs4.StdIn;

public class Josephus {
    public static void main(String[] args) {
        int N = StdIn.readInt();
        int M = StdIn.readInt();

        MyQueue<Integer> peoples = new MyQueueResizingArray<>(Integer[].class);
        for (int i = 0; i < N; i++) {
            peoples.enqueue(i);
        }

        while (peoples.size() != 0) {
            for (int i = 1; i < M; i++) {
                peoples.enqueue(peoples.dequeue());
            }
            System.out.println(peoples.dequeue()); // removing every Mth person
        }
    }
}
