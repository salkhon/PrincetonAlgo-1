package com.company.MyPriorityQueues;

import com.company.elementarySort.MyInsertionSort;

public class MyUnorderedMaxPQ<T extends Comparable<T>> {
    private T[] values;
    private int size;
    private static final int DEF_CAP = 2;

    @SuppressWarnings("unchecked")
    public MyUnorderedMaxPQ() {
        this.values = (T[]) new Comparable[DEF_CAP];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyUnorderedMaxPQ(T[] values) {
        this.values = (T[]) new Comparable[values.length];
        this.size = 0;
        for (T t : values) {
            if (t != null) {
                this.values[this.size++] = t;
            }
        }
    }

    public void insert(T key) {
        if (this.size == this.values.length) {
            resize(this.values.length * 2);
        }
        if (key != null) {
            this.values[this.size++] = key;
        }
    }

    public T delMaxPQ() {
        int max = 0;
        for (int i = 0; i < this.size; i++) {
            if (this.values[i].compareTo(this.values[max]) > 0) {
                max = i;
            }
        }
        MyInsertionSort.exchange(this.values, max, --this.size);
        T item = this.values[this.size];
        this.values[this.size] = null;
        if (this.size <= this.values.length / 4) {
            resize(this.values.length / 2);
        }
        return item;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCap) {
        T[] copy = (T[]) new Comparable[newCap];
        for (int i = 0, j = 0; i < this.values.length; i++) {
            if (this.values[i] != null) {
                copy[j++] = this.values[i];
            }
        }
        this.values = copy;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public T max() {
        int max = 0;
        for (int i = 0; i < this.size; i++) {
            if (this.values[i].compareTo(this.values[max]) > 0) {
                max = i;
            }
        }
        return this.values[max];
    }

    public int size() {
        return this.size;
    }
}

class OrderedMaxPQ<T extends Comparable<T>> {
    private T[] values;
    private int size;
    private final static int DEF_CAP = 2;

    @SuppressWarnings("unchecked")
    public OrderedMaxPQ() {
        this.values = (T[]) new Comparable[DEF_CAP];
        this.size = 0;
    }

    public void insert(T key) {
        if (key != null) {
            if (this.size == this.values.length) {
                resize(this.values.length * 2);
            }

            int pos = binarySearchForPosition(key);
            System.arraycopy(this.values, pos, this.values, pos + 1, this.size - pos);
            this.values[pos] = key;
            this.size++;
        }
    }

    private int binarySearchForPosition(T key) {
        if (isEmpty()) {
            return 0;
        }
        int ptrL = 0, ptrR = this.size - 1;
        // target position is before mid, mid crossing this.size - 1 means target is last
        int mid = (ptrL + ptrR) / 2;
        while (ptrL < this.size && ptrL <= ptrR) {
            if (this.values[mid].compareTo(key) < 0) {
                ptrL = mid + 1;
            } else if (mid > 0 && this.values[mid - 1].compareTo(key) >= 0) {
                ptrR = mid - 1;
            } else {
                break;
            }
            mid = (ptrL + ptrR) / 2;
        }
        if (ptrL > ptrR) {
            mid = this.size;
        }
        return mid;
    }

    public T delMax() {
        T item = null;
        if (!isEmpty()) {
            item = this.values[--this.size];
            this.values[this.size] = null;
        }
        return item;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCap) {
        T[] copy = (T[]) new Comparable[newCap];
        for (int i = 0, j = 0; i < this.size; i++) {
            if (this.values[i] != null) {
                copy[j++] = this.values[i];
            }
        }
        this.values = copy;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[ ");
        for (int i = 0; i < this.size; i++) {
            stringBuilder.append(this.values[i]);
            if (i < this.size - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    // test client
    public static void main(String[] args) {
        OrderedMaxPQ<Integer> pq = new OrderedMaxPQ<>();
        pq.insert(2); pq.insert(5); pq.insert(5); pq.insert(1); pq.insert(3); pq.insert(0); pq.insert(-1); pq.insert(3); pq.insert(4);
        pq.insert(6); pq.insert(6); pq.insert(-1); pq.insert(2); pq.insert(0); pq.insert(34); pq.insert(23); pq.insert(-23); pq.insert(-20);
        System.out.println(pq.size());
        System.out.println(pq.toString());
        System.out.println("deleted: " + pq.delMax() + " " + pq.delMax());
        System.out.println(pq);
    }
}
