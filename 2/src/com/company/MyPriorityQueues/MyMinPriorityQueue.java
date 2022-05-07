package com.company.MyPriorityQueues;

import com.company.elementarySort.MyInsertionSort;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class MyMinPriorityQueue<T extends Comparable<T>> implements Iterable<T> {
    private T[] binHeapArray;
    private int size;
    private static final int DEF_CAP = 4;

    @SuppressWarnings("unchecked")
    public MyMinPriorityQueue() {
        this.binHeapArray = (T[]) new Comparable<?>[DEF_CAP];
        this.size = 0;
    }

    public void insert(T key) {
        if (key != null) {
            if (this.size + 1 == this.binHeapArray.length) {
                resize(this.binHeapArray.length * 2);
            }
            this.binHeapArray[++this.size] = key; // first key on index 1
            swimUp(this.size);
        }
    }

    public T delMin() {
        T min = null;
        if (!isEmpty()) {
            min = this.binHeapArray[1];
            MyInsertionSort.exchange(this.binHeapArray, 1, this.size);
            this.binHeapArray[this.size--] = null;
            sinkDown(1);
            if (this.size > 0 && this.size <= this.binHeapArray.length / 4) {
                resize(this.binHeapArray.length / 2);
            }
        }
        return min;
    }

    public T peekMin() {
        return this.binHeapArray[1];
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    private void swimUp(int k) {
        T temp = this.binHeapArray[k];
        while (k > 1 && MyInsertionSort.less(temp, this.binHeapArray[k / 2])) {
            //MyInsertionSort.exchange(this.binHeapArray, k, k / 2);
            this.binHeapArray[k] = this.binHeapArray[k / 2]; // void created at parent
            k /= 2; // at this point, k points to the void
        }
        this.binHeapArray[k] = temp; // the final void is filled with the target
        // saves extra storage of the target every time
    }

    private void sinkDown(int k) {
        T temp = this.binHeapArray[k];
        // there is a child
        while (2 * k <= this.size) {
            k *= 2; // k is now first child
            // if the other child is more favorable, point k to that
            if (k + 1 <= this.size && MyInsertionSort.less(this.binHeapArray[k + 1], this.binHeapArray[k])) {
                k++;
            }
            // if the more favorable child is not more favorable than the parent, no need to demote parent anymore
            if (!MyInsertionSort.less(this.binHeapArray[k], temp)) {
                //break;
                this.binHeapArray[k / 2] = temp;
                return;
            }
            //MyInsertionSort.exchange(this.binHeapArray, k / 2, k);
            this.binHeapArray[k / 2] = this.binHeapArray[k];
        }
        this.binHeapArray[k] = temp;
    }

    public int size() {
        return this.size;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        T[] copy = (T[]) new Comparable<?>[newSize];
        System.arraycopy(this.binHeapArray, 0, copy, 0, this.size + 1);
        this.binHeapArray = copy;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.binHeapArray);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            int i = 1;
            @Override
            public boolean hasNext() {
                return i < MyMinPriorityQueue.this.size;
            }

            @Override
            public T next() {
                return MyMinPriorityQueue.this.binHeapArray[i++];
            }
        };
    }

    // test client
    public static void main(String[] args) {
        MyMinPriorityQueue<Character> minPriorityQueue = new MyMinPriorityQueue<>();
        minPriorityQueue.insert('C'); minPriorityQueue.insert('A'); minPriorityQueue.insert('V'); minPriorityQueue.insert('T'); minPriorityQueue.insert('T');
        minPriorityQueue.insert('R'); minPriorityQueue.insert('D'); minPriorityQueue.insert('E'); minPriorityQueue.insert('S'); minPriorityQueue.insert('Q');
        minPriorityQueue.insert('W'); minPriorityQueue.insert('G'); minPriorityQueue.insert('L'); minPriorityQueue.insert('B'); minPriorityQueue.insert('I');
        minPriorityQueue.insert('V'); minPriorityQueue.insert('J'); minPriorityQueue.insert('O'); minPriorityQueue.insert('P'); minPriorityQueue.insert('Q');

        System.out.println(minPriorityQueue);
        for (int i = 0; i < 5; i++) {
            System.out.println("Remove Min: " + minPriorityQueue.delMin());
            System.out.println(minPriorityQueue);
        }

        minPriorityQueue.insert('X');
        System.out.println(minPriorityQueue);
    }
}
