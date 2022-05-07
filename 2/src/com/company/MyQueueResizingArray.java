package com.company;

import java.lang.reflect.Array;
import java.util.Iterator;

public class MyQueueResizingArray<T> implements MyQueue<T>, Iterable<T> {
    private final Class<T[]> backingArrayType;
    private T[] values;
    private int head;
    private int tail;
    private int numOfItems; // makes tail redundant, also using tail to calculate number of elements makes this redundant - choosing one or the other would have been better
    private int capacity;

    public static final int DEFAULT_CAP = 1;

    public MyQueueResizingArray(Class<T[]> backingArrayType, int capacity) {
        this.backingArrayType = backingArrayType;
        this.values = backingArrayType.cast(Array.newInstance(backingArrayType.getComponentType(), capacity));
        this.head = 0;
        this.tail = 0;
        this.numOfItems = 0;
        this.capacity = capacity;
    }

    public MyQueueResizingArray(Class<T[]> backingArrayType) {
        this(backingArrayType, DEFAULT_CAP);
    }

    @Override
    public void enqueue(T item) {
        if (this.numOfItems == this.capacity) {
            resizeArray(this.capacity * 2);
        }

        this.values[this.tail++] = item;
        this.tail %= this.capacity;
        this.numOfItems++;
    }

    @Override
    public T dequeue() {
        T item = null;
        if (numOfItems != 0) {
            item = this.values[this.head];
            this.values[this.head] = null;
            this.head++;
            this.head %= this.capacity;
            this.numOfItems--;

            if (this.numOfItems > 0 && this.numOfItems <= this.capacity / 4) {
                resizeArray(this.capacity / 2);
            }
        }

        return item;
    }

    @Override
    public boolean isEmpty() {
        return this.numOfItems == 0;
    }

    @Override
    public int size() {
        return this.numOfItems;
    }

    private void resizeArray(int capacity) {
        T[] copy = this.backingArrayType.cast(Array.newInstance(this.backingArrayType.getComponentType(), capacity));
        int j = 0;
        for (int i = this.head; j < this.numOfItems; i = (i + 1) % this.capacity) {
            copy[j++] = this.values[i];
        }
        this.head = 0;
        this.tail = j;

        this.values = copy;
        this.capacity = capacity;
        System.out.println("------------Resized to: " + this.capacity);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{ ");
        for (T item : this) {
            stringBuilder.append(item).append( " ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int next = 0;

            @Override
            public boolean hasNext() {
                return this.next < MyQueueResizingArray.this.numOfItems;
            }

            @Override
            public T next() {
                T item = MyQueueResizingArray.this.values[(MyQueueResizingArray.this.head + this.next) % MyQueueResizingArray.this.capacity];
                this.next++;
                return item;
            }
        };
    }

    // test client
    public static void main(String[] args) {
        String[] parts = {"to", "be", "or", "not", "to", "-", "be", "-", "adding", "more", "content", "for", "-", "testing", "-", "-", "purposes", "more", "-", "and",  "-", "more", "civilization"};
        MyQueueResizingArray<String> myQueueResizingArray = new MyQueueResizingArray<>(String[].class);
        for (String part : parts) {
            if (part.equals("-")) {
                System.out.println(myQueueResizingArray.dequeue());
            } else {
                myQueueResizingArray.enqueue(part);
            }
        }

        System.out.println(myQueueResizingArray + " NofEle: " + myQueueResizingArray.size());
    }
}
