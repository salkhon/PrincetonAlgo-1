package com.company;

import java.util.Iterator;

public class MyQueueLinkedList<T> implements MyQueue<T>, Iterable<T> {
    private Node<T> first;
    private Node<T> last;
    private int size;

    public MyQueueLinkedList() {
        this.size = 0;
    }

    @Override
    public void enqueue(T item) {
        if (this.last != null && item != null) {
            this.last.setNext(new Node<>(item));
            this.last = this.last.getNext();
            this.size++;
        } else if (this.last == null) {
            this.first = new Node<>(item);
            this.last = this.first;
            this.size++;
        }
    }

    @Override
    public T dequeue() {
        T item = null;
        if (this.first != null) {
            item = this.first.getValue();
            this.first = this.first.getNext();
            this.size--;

            if (this.first == null) {
                this.last = null;
            }
        } else {
            System.out.println("Cannot dequeue from empty queue");
        }
        return item;
    }

    @Override
    public boolean isEmpty() {
        return this.first == null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{ ");

        for (T item : this) {
            stringBuilder.append(item).append(" -> ");
        }

        stringBuilder.append("null }");

        return stringBuilder.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            Node<T> next = MyQueueLinkedList.this.first;
            @Override
            public boolean hasNext() {
                return this.next != null;
            }

            @Override
            public T next() {
                T item = this.next.getValue();
                this.next = this.next.getNext();
                return item;
            }
        };
    }

    // test client
    public static void main(String[] args) {
        String[] parts = { "to", "be", "or", "not", "to", "-", "be", "-" };
        MyQueueLinkedList<String> myQueueLinkedList = new MyQueueLinkedList<>();
        for (String part : parts) {
            if (part.equals("-")) {
                System.out.println(myQueueLinkedList.dequeue());
            } else {
                myQueueLinkedList.enqueue(part);
            }
        }

        System.out.println(myQueueLinkedList);
    }
}
