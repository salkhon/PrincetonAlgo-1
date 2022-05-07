package com.company;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.lang.reflect.Array;
import java.util.Iterator;

public class MyStackResizingArray<T> implements MyStack<T>, Iterable<T> {
    private final Class<T[]> storageType;
    private T[] values;
    private int length;

    public static final int DEFAULT_CAPACITY = 2;

    public MyStackResizingArray(Class<T[]> storageType)  {
        this.storageType = storageType;
        this.values = storageType.cast(Array.newInstance(storageType.getComponentType(), DEFAULT_CAPACITY));
        this.length = 0;
    }

    @Override
    public void push(T item) {
        if (this.length == this.values.length) {
            resize(this.values.length * 2);
        }

        this.values[this.length++] = item;
    }

    @Override
    public T pop() {
        T item = null;
        if (this.length > 0) {
            item = this.values[--this.length];
            this.values[this.length] = null;

            if (this.length < this.values.length / 4) {
                resize(this.values.length / 2);
            }
        }

        return item;
    }

    @Override
    public int size() {
        return this.length;
    }

    @Override
    public boolean isEmpty() {
        return this.length == 0;
    }

    private void resize(int resizedCapacity) {
        T[] copy = this.storageType.cast(Array.newInstance(this.storageType.getComponentType(), resizedCapacity));
        System.arraycopy(this.values, 0, copy, 0, this.length);
        this.values = copy;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int next = MyStackResizingArray.this.length;
            @Override
            public boolean hasNext() {
                return this.next > 0;
            }

            @Override
            public T next() {
                return MyStackResizingArray.this.values[--this.next];
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[ ");

        for (T item : this) {
            stringBuilder.append(item).append(" ");
        }

        return stringBuilder.append("]").toString();
    }

    // test client
    public static void main(String[] args) {
        MyStackResizingArray<Integer> myStackResizingArray = new MyStackResizingArray<>(Integer[].class);

        for (int i = 0; i < 100; i++) {
            myStackResizingArray.push(StdRandom.uniform(1000));
            if (i % 10 == 0) {
                System.out.println("Size: " + myStackResizingArray.size());
            }
        }

        System.out.println(myStackResizingArray);
    }
}
