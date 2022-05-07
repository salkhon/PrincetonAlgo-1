package com.company;

public interface MyQueue<T> {
    void enqueue(T item);
    T dequeue();
    boolean isEmpty();
    int size();
}
