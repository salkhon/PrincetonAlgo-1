package com.company;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Stack;

public class QueueWith2Stack<T> implements MyQueue<T> {
    private final Stack<T> normalStack;
    private final Stack<T> reversedStack;

    public QueueWith2Stack() {
        this.normalStack = new Stack<>();
        this.reversedStack = new Stack<>();
    }

    @Override
    public void enqueue(T item) {
        // queue insert is just like stack inserting
        this.normalStack.push(item);
    }

    @Override
    public T dequeue() {
        T item = null;
        if (!isEmpty()) {
            if (this.reversedStack.empty()) {
                // the first elements of the stack are loaded and kept in the reversed stack
                // once they are empty again, they are reloaded and kept
                while (!this.normalStack.empty()) {
                    this.reversedStack.push(this.normalStack.pop());
                }
            }

            item = this.reversedStack.pop();
        }

        return item;
    }

    @Override
    public boolean isEmpty() {
        return this.normalStack.empty() && this.reversedStack.empty();
    }

    @Override
    public int size() {
        return this.normalStack.size() + this.reversedStack.size();
    }

    // test client
    public static void main(String[] args) {
        QueueWith2Stack<Integer> queueWith2Stack = new QueueWith2Stack<>();

        final int TEST = 10;
        long t1 = System.nanoTime();
        for (int i = 0; i < TEST; i++) {
            Integer random = StdRandom.uniform(1000);
            queueWith2Stack.enqueue(random);
            System.out.println("Enqueue: " + random);

            if (StdRandom.bernoulli()) {
                System.out.println("Dequeue: " + queueWith2Stack.dequeue());
            }
        }
        long t2 = System.nanoTime();

        System.out.println("Amortized time: " + (t2 - t1) / TEST);

    }
}







//public class QueueWith2Stack<T> implements MyQueue<T> {
//    private Stack<T> normalStack;
//    private Stack<T> reversedStack;
//    private boolean normal;
//
//    public QueueWith2Stack() {
//        this.normal = true;
//        this.normalStack = new Stack<>();
//        this.reversedStack = new Stack<>();
//    }
//
//    @Override
//    public void enqueue(T item) {
//        if (this.normal) {
//            this.normalStack.push(item);
//        } else {
//            while (!this.reversedStack.empty()) {
//                this.normalStack.push(this.reversedStack.pop());
//            }
//            this.normalStack.push(item);
//            this.normal = true;
//            // only one stack can be full and the other is empty
//        }
//    }
//
//    @Override
//    public T dequeue() {
//        T item = null;
//        if (!normal) {
//            item = this.reversedStack.pop();
//        } else {
//            while (!this.normalStack.empty()) {
//                this.reversedStack.push(this.normalStack.pop());
//            }
//            item = this.reversedStack.pop();
//            this.normal = false;
//        }
//
//        return item;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return this.normalStack.empty() && this.reversedStack.empty();
//    }
//
//    @Override
//    public int size() {
//        return Math.max(this.normalStack.size(), this.reversedStack.size());
//    }
//
//    // test client
//    public static void main(String[] args) {
//        QueueWith2Stack<Integer> queueWith2Stack = new QueueWith2Stack<>();
//
//        final int TEST = 60000;
//        long t1 = System.nanoTime();
//        for (int i = 0; i < TEST; i++) {
//            Integer random = StdRandom.uniform(1000);
//            queueWith2Stack.enqueue(random);
////            System.out.println("Enqueue: " + random);
//
//            if (StdRandom.bernoulli()) {
////                System.out.println("Dequeue: " + queueWith2Stack.dequeue());
//                queueWith2Stack.dequeue();
//            }
//        }
//        long t2 = System.nanoTime();
//
//        System.out.println("Amortized time: " + (t2 - t1) / TEST);
//    }
//}
