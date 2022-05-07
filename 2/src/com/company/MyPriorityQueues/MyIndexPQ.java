package com.company.MyPriorityQueues;

import java.util.Arrays;

public class MyIndexPQ<T extends Comparable<T>> {
    public enum Order { MIN, MAX }

    private int[] indexHeapArray;
    private int[] indexPosInHeap;
    private T[] indexValue;
    private final Order order;
    private int size;
    private static final int DEF_CAP = 2;

    @SuppressWarnings("unchecked")
    public MyIndexPQ(Order order) {
        this.indexHeapArray = new int[DEF_CAP];
        Arrays.fill(this.indexHeapArray, -1);
        this.indexPosInHeap = new int[DEF_CAP];
        Arrays.fill(this.indexPosInHeap, -1);
        this.indexValue = (T[]) new Comparable<?>[DEF_CAP];
        this.size = 0;
        this.order = order;
    }

    public void insert(T key) {
        if (key != null) {
            if (this.size == this.indexValue.length - 1) {
                resizeArrays(this.indexValue.length * 2);
            }
            int i = this.size++;
            while (i > 0) {
                if (this.indexValue[i] == null) {
                    break;
                }
                i--;
            }
            if (i == 0) {
                i = this.size;
            }
            this.indexValue[i] = key;
            this.indexHeapArray[this.size] = i;
            this.indexPosInHeap[i] = this.size;

            swimUp(this.size);
        }
    }

    public T delRoot() {
        int extremeIndex = this.indexHeapArray[1];
        T extreme = this.indexValue[extremeIndex];
        exchange(this.size, 1);
        this.indexValue[extremeIndex] = null;
        this.indexHeapArray[this.size--] = -1;
        this.indexPosInHeap[extremeIndex] = -1;
        sinkDown(1);
        return extreme;
    }

    public T getRoot() {
        return this.indexValue[this.indexHeapArray[1]];
    }

    public void change(int k, T key) {
        if (this.indexValue[k] != null) {
            this.indexValue[k] = key;
            int posOfIndex = this.indexPosInHeap[k];
            if (posOfIndex > 1 && (this.order == Order.MAX && less(posOfIndex / 2, posOfIndex) || this.order == Order.MIN && less(posOfIndex, posOfIndex / 2))) {
                swimUp(posOfIndex);
            } else {
                sinkDown(posOfIndex);
            }
        }
    }
    
    public void delete(int k) {
        if (this.indexValue[k] != null) {
            this.indexValue[k] = null;
            int posInHeap = this.indexPosInHeap[k];
            this.indexPosInHeap[k] = -1;
            exchange(posInHeap, this.size);
            this.indexHeapArray[this.size--] = -1;
            if (posInHeap > 1 && (this.order == Order.MAX && less(posInHeap / 2, posInHeap) || this.order == Order.MIN && less(posInHeap, posInHeap / 2))) {
                swimUp(posInHeap);
            } else {
                sinkDown(posInHeap);
            }
        }
    }

    private void swimUp(int k) {
        while (k > 1 &&
                ((this.order == Order.MAX && less(k / 2, k)) ||
                (this.order == Order.MIN && less(k, k / 2)))) {
            exchange(k, k / 2); // must change indexMap
            k /= 2;
        }
    }

    private void sinkDown(int k) {
        while (2 * k <= this.size) {
            k *= 2;
            if (k + 1 <= this.size && ((this.order == Order.MAX && less(k, k + 1)) || (this.order == Order.MIN && less(k + 1, k)))) {
                k++;
            }
            if ((this.order == Order.MAX && !less(k / 2, k)) || (this.order == Order.MIN && !less(k, k / 2))) {
                break;
            }
            exchange(k / 2, k);
        }
    }

    private boolean less(int i, int j) {
        return this.indexValue[this.indexHeapArray[i]].compareTo(indexValue[this.indexHeapArray[j]]) < 0;
    }

    private void exchange(int i, int j) {
        this.indexHeapArray[i] ^= this.indexHeapArray[j];
        this.indexHeapArray[j] ^= this.indexHeapArray[i];
        this.indexHeapArray[i] ^= this.indexHeapArray[j];

        final int index1 = this.indexHeapArray[j], index2 = this.indexHeapArray[i];

        this.indexPosInHeap[index1] ^= this.indexPosInHeap[index2];
        this.indexPosInHeap[index2] ^= this.indexPosInHeap[index1];
        this.indexPosInHeap[index1] ^= this.indexPosInHeap[index2];
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean contains(int k) {
        return this.indexValue[k] != null;
    }

    @SuppressWarnings("unchecked")
    private void resizeArrays(int newSize) {
        int[] heapCopy = new int[newSize];
        System.arraycopy(this.indexHeapArray, 0, heapCopy, 0, this.size + 1);
        this.indexHeapArray = heapCopy;

        int[] indexPosCopy = new int[newSize];
        System.arraycopy(this.indexPosInHeap, 0, indexPosCopy, 0, this.size + 1);
        this.indexPosInHeap = indexPosCopy;

        T[] valueCopy = (T[]) new Comparable<?>[newSize];
        System.arraycopy(this.indexValue, 0, valueCopy, 0, this.size + 1);
        this.indexValue = valueCopy;
    }

    @Override
    public String toString() {
        return "Index Value: " + Arrays.toString(this.indexValue) + "\nHeap Index: " + Arrays.toString(this.indexHeapArray) +
                "\nIndex Position in Heap: " + Arrays.toString(this.indexPosInHeap);
    }

    // test client
    public static void main(String[] args) {
        MyIndexPQ<Character> myIndexPQ = new MyIndexPQ<>(Order.MAX);
        myIndexPQ.insert('C'); myIndexPQ.insert('A'); myIndexPQ.insert('V'); myIndexPQ.insert('T'); myIndexPQ.insert('T');
        myIndexPQ.insert('R'); myIndexPQ.insert('D'); myIndexPQ.insert('E'); myIndexPQ.insert('S'); myIndexPQ.insert('Q');
        myIndexPQ.insert('W'); myIndexPQ.insert('G'); myIndexPQ.insert('L'); myIndexPQ.insert('B'); myIndexPQ.insert('I');
        myIndexPQ.insert('V'); myIndexPQ.insert('J'); myIndexPQ.insert('O'); myIndexPQ.insert('P'); myIndexPQ.insert('Q');

        System.out.println(myIndexPQ);

        for (int i = 0; i < 5; i++) {
            System.out.println("Remove Max: " + myIndexPQ.delRoot());
            System.out.println(myIndexPQ);
        }

        myIndexPQ.delete(3);
        System.out.println("Deleting index 3: " + myIndexPQ);

        myIndexPQ.delete(6);
        System.out.println("Deleting index 6: " + myIndexPQ);

        System.out.println("Contains index 7 ? " + myIndexPQ.contains(7));

        myIndexPQ.insert('X');
        System.out.println(myIndexPQ);
    }
}
