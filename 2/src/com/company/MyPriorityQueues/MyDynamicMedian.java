package com.company.MyPriorityQueues;

import java.util.Arrays;

// to find the median the goal is to partition the input into to subsets, left ot the median and right to the median
// the input sequence could be random, but priority queue enables to quickly find the max of left subset, min of right subset
// which are the median for even number of keys, and the larger subset's root is the median for odd size
public class MyDynamicMedian<T extends Comparable<T>> {
    MyMaxPriorityQueue<T> leftMaxPQ;
    MyMinPriorityQueue<T> rightMinPQ;

    private int size;

    public MyDynamicMedian() {
        this.leftMaxPQ = new MyMaxPriorityQueue<>();
        this.rightMinPQ = new MyMinPriorityQueue<>();
        this.size = 0;
    }

    public void insert(T key) {
        if (size != 0) {
            if (less(key, this.leftMaxPQ.peekMax())) {
                if (this.leftMaxPQ.size() + 1 - this.rightMinPQ.size() > 1) {
                    this.rightMinPQ.insert(this.leftMaxPQ.delMax());
                }
                this.leftMaxPQ.insert(key);
            } else {
                if (this.rightMinPQ.size() + 1 - this.leftMaxPQ.size() > 1) {
                    this.leftMaxPQ.insert(this.rightMinPQ.delMin());
                }
                this.rightMinPQ.insert(key);
            }
        } else {
            this.leftMaxPQ.insert(key);
        }
        this.size++;
    }

    public T findMedian() {
        T median;
        if (this.size % 2 == 0) {
            median = this.leftMaxPQ.peekMax();
        } else {
            if (this.leftMaxPQ.size() >= this.rightMinPQ.size()) {
                median = this.leftMaxPQ.peekMax();
            } else {
                median = this.rightMinPQ.peekMin();
            }
        }
        return median;
    }

    public boolean less(T k1, T k2) {
        return k1.compareTo(k2) < 0;
    }

    @Override
    public String toString() {
        return this.leftMaxPQ + " " + this.rightMinPQ;
    }

    // test client
    public static void main(String[] args) {
        MyDynamicMedian<Character> myDynamicMedian = new MyDynamicMedian<>();
        myDynamicMedian.insert('C');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('A');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('V');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('T');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('T');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('R');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('D');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('E');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('S');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('Q');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('W');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('G');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('L');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('B');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('I');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('V');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('J');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('O');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('P');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);
        myDynamicMedian.insert('Q');
        System.out.println(myDynamicMedian.findMedian());
        System.out.println(myDynamicMedian);

    }
}
