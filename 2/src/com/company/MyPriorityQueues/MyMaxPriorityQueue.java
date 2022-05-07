package com.company.MyPriorityQueues;

import com.company.elementarySort.MyInsertionSort;

import java.util.Arrays;

public class MyMaxPriorityQueue<T extends Comparable<T>> {
    private T[] binHeapArray;
    private int size;
    private static final int DEF_CAP = 4;

    @SuppressWarnings("unchecked")
    public MyMaxPriorityQueue() {
        this.binHeapArray = (T[]) new Comparable<?>[DEF_CAP];
        this.size = 0;
    }

    private void swimUp(int k) {
        while (k > 1 && less(this.binHeapArray[k / 2], this.binHeapArray[k])) {
            exchange(this.binHeapArray, k / 2, k);
            k /= 2;
        }
    }

    // we're only introducing heap-order violation on the top and on the bottom, so middle violations are not a concern
    // if they were a concern as in heap-sort first heap-ify pass, sink down would have to check if the parent were lesser than any of their children before proceeding with the loop
    // otherwise, exchange would take place even when the sub-heap was ordered - creating new order violation
    // we're putting a bottom node to the top, so we know the heap was previously ordered - and thus the bottom node will fall through
    private void sinkDown(int k) {
        while (2 * k <= this.size) {
            k *= 2;
            if (k + 1 <= this.size && less(this.binHeapArray[k], this.binHeapArray[k + 1])) {
                k++;
            }
            if (!MyInsertionSort.less(this.binHeapArray[k], this.binHeapArray[k / 2])) {
                break;
            }

            exchange(this.binHeapArray, k / 2, k);
        }
    }

    public void insert(T key) {
        if (key != null) {
            if (this.size == this.binHeapArray.length - 1) {
                resizeArray(this.binHeapArray.length * 2);
            }
            this.binHeapArray[++this.size] = key;
            swimUp(this.size);
        }
    }

    public T delMax() {
        T max = null;
        if (!isEmpty()) {
            max = this.binHeapArray[1];
            exchange(this.binHeapArray, 1, this.size);
            this.binHeapArray[this.size--] = null;
            sinkDown(1);

            if (this.size > 0 && this.size < this.binHeapArray.length / 4) {
                resizeArray(this.binHeapArray.length / 2);
            }
        }
        return max;
    }

    public T peekMax() {
        return this.binHeapArray[1];
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    private boolean less(T o1, T o2) {
        return o1.compareTo(o2) < 0;
    }

    private void exchange(Object[] array, int index1, int index2) {
        Object temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    @SuppressWarnings("unchecked")
    private void resizeArray(int newSize) {
        T[] copy = (T[]) new Comparable<?>[newSize];
        System.arraycopy(this.binHeapArray, 0, copy, 0, this.binHeapArray.length);
        this.binHeapArray = copy;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.binHeapArray);
    }

    // test client
    public static void main(String[] args) {
        MyMaxPriorityQueue<Character> maxPriorityQueue = new MyMaxPriorityQueue<>();
        maxPriorityQueue.insert('C'); maxPriorityQueue.insert('A'); maxPriorityQueue.insert('V'); maxPriorityQueue.insert('T'); maxPriorityQueue.insert('T');
        maxPriorityQueue.insert('R'); maxPriorityQueue.insert('D'); maxPriorityQueue.insert('E'); maxPriorityQueue.insert('S'); maxPriorityQueue.insert('Q');
        maxPriorityQueue.insert('W'); maxPriorityQueue.insert('G'); maxPriorityQueue.insert('L'); maxPriorityQueue.insert('B'); maxPriorityQueue.insert('I');
        maxPriorityQueue.insert('V'); maxPriorityQueue.insert('J'); maxPriorityQueue.insert('O'); maxPriorityQueue.insert('P'); maxPriorityQueue.insert('Q');

        System.out.println(maxPriorityQueue);
        for (int i = 0; i < 5; i++) {
            System.out.println("Remove Max: " + maxPriorityQueue.delMax());
            System.out.println(maxPriorityQueue);
        }

        maxPriorityQueue.insert('X');
        System.out.println(maxPriorityQueue);
    }
}
