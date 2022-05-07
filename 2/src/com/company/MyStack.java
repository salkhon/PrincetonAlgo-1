package com.company;

public interface MyStack<T> {
    void push(T item);
    T pop();
    int size();
    boolean isEmpty();
}
