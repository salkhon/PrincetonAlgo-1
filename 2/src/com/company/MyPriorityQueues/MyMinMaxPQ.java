package com.company.MyPriorityQueues;

import java.util.Arrays;

public class MyMinMaxPQ<T extends Comparable<T>> {
    private T[] minHeap;
    private T[] maxHeap;
    private int[] minToMaxBridge;
    private int[] maxToMinBridge;
    private int size;

    private static final int DEF_CAP = 2;

    @SuppressWarnings("unchecked")
    public MyMinMaxPQ() {
        this.minHeap = (T[]) new Comparable<?>[DEF_CAP];
        this.maxHeap = (T[]) new Comparable<?>[DEF_CAP];
        this.minToMaxBridge = new int[DEF_CAP];
        Arrays.fill(this.minToMaxBridge, -1);
        this.maxToMinBridge = new int[DEF_CAP];
        Arrays.fill(this.maxToMinBridge, -1);
        this.size = 0;
    }

    private void swimUp(T[] heap, int k) {
        if (heap == this.minHeap) {
            while (k > 1 && less(this.minHeap[k], this.minHeap[k / 2])) {
                exchange(this.minHeap, k, k / 2);
                k /= 2;
            }
        } else {
            while (k > 1 && less(this.maxHeap[k / 2], this.maxHeap[k])) {
                exchange(this.maxHeap, k / 2, k);
                k /= 2;
            }
        }
    }

    private void sinkDown(T[] heap, int k) {
        if (heap == this.minHeap) {
            while (2 * k <= this.size) {
                k *= 2;
                if (k + 1 <= this.size && less(this.minHeap[k + 1], this.minHeap[k])) {
                    k++;
                }
                if (!less(this.minHeap[k], this.minHeap[k / 2])) {
                    break;
                }
                exchange(this.minHeap, k, k / 2);
            }
        } else if (heap == this.maxHeap) {
            while (2 * k <= this.size) {
                k *= 2;
                if (k + 1 <= this.size && less(this.maxHeap[k], this.maxHeap[k + 1])) {
                    k++;
                }
                if (!less(this.maxHeap[k / 2], this.maxHeap[k])) {
                    break;
                }
                exchange(this.maxHeap, k, k / 2);
            }
        }
    }

    private void exchange(Object[] array, int index1, int index2) {
        Object temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;

        if (array == this.minHeap) {
            this.minToMaxBridge[index1] ^= this.minToMaxBridge[index2];
            this.minToMaxBridge[index2] ^= this.minToMaxBridge[index1];
            this.minToMaxBridge[index1] ^= this.minToMaxBridge[index2];

            this.maxToMinBridge[this.minToMaxBridge[index1]] ^= this.maxToMinBridge[this.minToMaxBridge[index2]];
            this.maxToMinBridge[this.minToMaxBridge[index2]] ^= this.maxToMinBridge[this.minToMaxBridge[index1]];
            this.maxToMinBridge[this.minToMaxBridge[index1]] ^= this.maxToMinBridge[this.minToMaxBridge[index2]];
        } else if (array == this.maxHeap) {
            this.maxToMinBridge[index1] ^= this.maxToMinBridge[index2];
            this.maxToMinBridge[index2] ^= this.maxToMinBridge[index1];
            this.maxToMinBridge[index1] ^= this.maxToMinBridge[index2];

            this.minToMaxBridge[this.maxToMinBridge[index1]] ^= this.minToMaxBridge[this.maxToMinBridge[index2]];
            this.minToMaxBridge[this.maxToMinBridge[index2]] ^= this.minToMaxBridge[this.maxToMinBridge[index1]];
            this.minToMaxBridge[this.maxToMinBridge[index1]] ^= this.minToMaxBridge[this.maxToMinBridge[index2]];
        }
    }

    private boolean less(T o1, T o2) {
        return o1.compareTo(o2) < 0;
    }

    public void insert(T key) {
        if (key != null) {
            if (this.size == this.minHeap.length - 1) {
                resizeArrays(this.minHeap.length * 2);
            }

            this.minHeap[++this.size] = key;
            this.maxHeap[this.size] = key;

            this.minToMaxBridge[this.size] = this.size;
            this.maxToMinBridge[this.size] = this.size;

            swimUp(this.minHeap, this.size);
            swimUp(this.maxHeap, this.size);
            System.out.println("inserted: " + key);
//            System.out.println(this);
        }
    }

    public T delMin() {
        T min = this.minHeap[1];
        exchange(this.minHeap, 1, this.size);
        int exchangedMinToMaxPosition = this.minToMaxBridge[this.size];
        exchange(this.maxHeap, this.minToMaxBridge[this.size], this.size);
//        System.out.println(this.minHeap[this.size] + " " + this.maxHeap[this.size]);
//        System.out.println(this.minToMaxBridge[this.size] + " " + this.maxToMinBridge[this.size] + " " + positionInMinToMaxBridge);

        this.minHeap[this.size] = null;
        this.maxHeap[this.size--] = null;

        sinkDown(this.minHeap, 1);
        if (less(this.maxHeap[exchangedMinToMaxPosition / 2], this.maxHeap[exchangedMinToMaxPosition])) {
            swimUp(this.maxHeap, exchangedMinToMaxPosition);
        } else {
            sinkDown(this.maxHeap, exchangedMinToMaxPosition);
        }
        if (this.size > 0 && this.size <= this.minHeap.length / 4) {
            resizeArrays(this.minHeap.length / 2);
        }
        return min;
    }

    public T delMax() {
        T max = this.maxHeap[1];
        exchange(this.maxHeap, 1, this.size);
        int exchangedMaxToMinPosition = this.maxToMinBridge[this.size];
        exchange(this.minHeap, this.maxToMinBridge[this.size], this.size);

        this.maxHeap[this.size] = null;
        this.minHeap[this.size--] = null;

        sinkDown(this.maxHeap, 1);
        if (less(this.minHeap[exchangedMaxToMinPosition / 2], this.minHeap[exchangedMaxToMinPosition])) {
            swimUp(this.minHeap, exchangedMaxToMinPosition);
        } else {
            sinkDown(this.minHeap, exchangedMaxToMinPosition);
        }
        if (this.size > 0 && this.size <= this.minHeap.length / 4) {
            resizeArrays(this.minHeap.length / 2);
        }
        return max;
    }

    public T findMin() {
        return this.minHeap[1];
    }

    public T findMax() {
        return this.maxHeap[1];
    }

    @SuppressWarnings("unchecked")
    public void resizeArrays(int newSize) {
        System.out.println("resizing to : " + newSize);
        T[] minCopy = (T[]) new Comparable<?>[newSize];
        System.arraycopy(this.minHeap, 0, minCopy, 0, this.size + 1);
        this.minHeap = minCopy;

        T[] maxCopy = (T[]) new Comparable<?>[newSize];
        System.arraycopy(this.maxHeap, 0, maxCopy, 0, this.size + 1);
        this.maxHeap = maxCopy;

        int[] minToMaxCopy = new int[newSize];
        System.arraycopy(this.minToMaxBridge, 0, minToMaxCopy, 0, this.size + 1);
        this.minToMaxBridge = minToMaxCopy;

        int[] maxToMinCopy = new int[newSize];
        System.arraycopy(this.maxToMinBridge, 0, maxToMinCopy, 0, this.size + 1);
        this.maxToMinBridge = maxToMinCopy;
    }

    @Override
    public String toString() {
        return "Min Heap: " + Arrays.toString(this.minHeap) + "\nMaxHeap: " + Arrays.toString(this.maxHeap);
    }

    // test client
    public static void main(String[] args) {
        MyMinMaxPQ<Character> minMaxPQ = new MyMinMaxPQ<>();
        minMaxPQ.insert('C'); minMaxPQ.insert('A'); minMaxPQ.insert('V'); minMaxPQ.insert('T'); minMaxPQ.insert('T');
        minMaxPQ.insert('R'); minMaxPQ.insert('D'); minMaxPQ.insert('E'); minMaxPQ.insert('S'); minMaxPQ.insert('Q');
        minMaxPQ.insert('W'); minMaxPQ.insert('G'); minMaxPQ.insert('L'); minMaxPQ.insert('B'); minMaxPQ.insert('I');
        minMaxPQ.insert('V'); minMaxPQ.insert('J'); minMaxPQ.insert('O'); minMaxPQ.insert('P'); minMaxPQ.insert('Q');

        System.out.println(minMaxPQ);

        System.out.println(Arrays.toString(minMaxPQ.minToMaxBridge));
        System.out.println(Arrays.toString(minMaxPQ.maxToMinBridge));

        for (int i = 0; i < 3; i++) {
            System.out.println("Remove Min: " + minMaxPQ.delMin());
            System.out.println(minMaxPQ);
        }

        for (int i = 0; i < 3; i++) {
            System.out.println("Remove Max: " + minMaxPQ.delMax());
            System.out.println(minMaxPQ);
        }

        minMaxPQ.insert('X');
        System.out.println(minMaxPQ);
    }
}
