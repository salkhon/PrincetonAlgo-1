package com.company;

import java.lang.reflect.Array;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class ResizingArray<T> implements Iterable<T> {
    private final Class<T[]> backingArrayType;
    private T[] values;
    private int capacity;
    private int length;
    private int numOfElements;
    private static final int DEFAULT_CAPACITY = 2;

    public ResizingArray(int capacity, Class<T[]> backingArrayType) {
        this.backingArrayType = backingArrayType;
        this.values = backingArrayType.cast(Array.newInstance(backingArrayType.getComponentType(), capacity));
        this.capacity = capacity;
        this.length = 0;
        this.numOfElements = 0;
    }

    private ResizingArray(Class<T[]> backingArrayType) {
        this(DEFAULT_CAPACITY, backingArrayType);
    }

    public int length() {
        return this.length;
    }

    public T get(int index) throws IndexOutOfBoundsException {
        if (index >= 0 && index < length) {
            return this.values[index];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void add(T item) throws NullPointerException{
        if (item != null) {
            if (this.length >= this.capacity) {
                resizeArray(this.capacity * 2);
            }

            this.values[this.length++] = item;
            this.numOfElements++;
        } else {
            throw new NullPointerException();
        }
    }

    public void remove(int index) throws IndexOutOfBoundsException {
        if (index >= 0 && index < length) {
            if (this.values[index] != null) {
                this.values[index] = null;
                this.numOfElements--;
            }
        } else {
            throw  new IndexOutOfBoundsException();
        }

        if (this.numOfElements > 0 && this.numOfElements < this.capacity / 4) {
            resizeArray(this.capacity / 2); // avoids THRASHING
            // invariant: array is always 25% to 100% full
        }
    }

    private void resizeArray(int capacity) {
        System.out.println("resizing array!:  to " + capacity);
        T[] newArray = this.backingArrayType.cast(Array.newInstance(this.backingArrayType.getComponentType(), capacity));
        this.capacity = capacity;

        int newLength = 0;
        for (int i = 0; i < this.length; i++) {
            if (this.values[i] != null) {
                newArray[newLength++] = this.values[i];
            }
        }
        this.values = newArray;
        this.length = newLength + 1;
        this.numOfElements = newLength;
    }

    public int indexOf(T item) throws NullPointerException {
        if (item != null) {
            for (int i = 0; i < this.length; i++) {
                if (item.equals(this.values[i])) {
                    return i;
                }
            }

            return -1;
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            int current = -1;
            int next = 0;

            @Override
            public boolean hasNext() {
                boolean ans = false;
                while (this.next < ResizingArray.this.length && ResizingArray.this.values[next] == null) {
                    this.next++;
                }

                if (this.next < ResizingArray.this.length) {
                    ans = true;
                }

                return ans;
            }

            @Override
            public T next() {
                this.current = this.next;
                this.next++;
                return ResizingArray.this.values[this.current];
            }
        };
    }

    // test client
    public static void main(String[] args) {
        ResizingArray<Integer> resizingArray = new ResizingArray<>(Integer[].class);
        for (int i = 0; i < 50; i++) {
            resizingArray.add(i);
        }

        for (int i = 0; i < 30; i++) {
            resizingArray.remove(StdRandom.uniform(resizingArray.length() - 1));
        }

        resizingArray.remove(resizingArray.indexOf(27));
        resizingArray.add(999);

        for (int i = 0; i < resizingArray.length; i++) {
            System.out.println(resizingArray.get(i));
        }

        for (int i = 0; i < 1000; i++) {
            resizingArray.add(i);
        }

        for (int i = 0; i < resizingArray.length; i++) {
            System.out.println(resizingArray.get(i));
        }

        for (int i = 0; i < 800; i++) {
            resizingArray.remove(StdRandom.uniform(resizingArray.length()));
        }

        System.out.println("iterable=================");
        for (Integer integer : resizingArray) {
            System.out.println(integer);
        }

    }
}
