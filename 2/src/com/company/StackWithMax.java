package com.company;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StackWithMax<T extends Comparable<T>> implements MyStack<T>, Iterable<T> {
    private T[] values;
    private int top;
    private int capacity = 2;
    private final Class<T[]> arrayType;

    public StackWithMax(Class<T[]> arrayType) {
        this.arrayType = arrayType;
        this.values = arrayType.cast(Array.newInstance(arrayType.getComponentType(), this.capacity));
    }

    private void resize(int resizedCapacity) {
        T[] copy = this.arrayType.cast(Array.newInstance(this.arrayType.getComponentType(), resizedCapacity));
        System.arraycopy(this.values, 0, copy, 0, top);
        this.capacity = resizedCapacity;
        this.values = copy;
    }

    @Override
    public void push(T item) {
        if (this.top == this.capacity) {
            resize(this.capacity * 2);
        }

        this.values[this.top++] = item;
    }

    @Override
    public T pop() {
        T item = null;
        if (!isEmpty()) {
            item = this.values[--this.top];

            if (this.top > 0 && this.top <= this.capacity / 4) {
                resize(this.capacity / 2);
            }
        } else {
            System.out.println("Stack is empty");
        }

        return item;
    }

    @Override
    public int size() {
        return this.top;
    }

    @Override
    public boolean isEmpty() {
        return this.top == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int next = StackWithMax.this.top;

            @Override
            public boolean hasNext() {
                return this.next > 0;
            }

            @Override
            public T next() {
                return StackWithMax.this.values[--this.next];
            }
        };
    }

    public T max() {
        if (!isEmpty()) {
            Iterator<T> iter = iterator();
            T maxItem;

            maxItem = iter.next();
            while (iter.hasNext()) {
                T nextItem = iter.next();
                if (nextItem.compareTo(maxItem) > 0) {
                    maxItem = nextItem;
                }
            }

            return maxItem;
        } else {
            System.out.println("Stack is empty");
            return null;
        }
    }

    // test client
    public static void main(String[] args) {
        StackWithMax<Double> stackWithMax = new StackWithMax<>(Double[].class);

        for (int i = 0; i < 100; i++) {
            stackWithMax.push(StdRandom.cauchy());
        }

        System.out.println("Max by max(): " + stackWithMax.max());

        Double max = 0.0;
        while (!stackWithMax.isEmpty()) {
            Double popped = stackWithMax.pop();
            if (popped > max) {
                max = popped;
            }
        }

        System.out.println("Max by popping items: " + max);
    }
}
