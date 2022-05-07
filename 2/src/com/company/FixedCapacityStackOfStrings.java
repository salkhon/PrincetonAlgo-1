package com.company;

import java.util.Iterator;

public class FixedCapacityStackOfStrings implements Iterable<String> {
    private int capacity;
    private String[] values;
    private int top;

    public FixedCapacityStackOfStrings(int capacity) {
        this.capacity = capacity;
        this.values = new String[capacity];
        this.top = 0;
    }

    public void push(String item) {
        if (top < capacity) {
            this.values[this.top++] = item;
        } else {
            System.out.print("Stack is full.");
        }
        System.out.println("Top: " + this.top + ", Capacity: " + this.capacity);
    }

    public String pop() {
        String returnValue = null;
        if (top > 0) {
             returnValue = this.values[--this.top];
             this.values[this.top] = null;
        } else {
            System.out.println("Stack is empty");
        }
        System.out.println("Top: " + this.top + ", Capacity: " + this.capacity);
        return returnValue;
    }

    public boolean isEmpty() {
        return this.top == 0;
    }

    public boolean isFull() {
        return this.top == this.capacity;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0; i < this.top; i++) {
            stringBuilder.append(this.values[i]);
            if (i < this.top - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private int next = FixedCapacityStackOfStrings.this.capacity - 1;
            @Override
            public boolean hasNext() {
                return next > 0;
            }

            @Override
            public String next() {
                return FixedCapacityStackOfStrings.this.values[--next];
            }
        };
    }

    // test client
    public static void main(String[] args) {
        String input = "to be or not to - be - -";
        String[] parts = input.split(" ");
        final int CAPACITY = 10;
        FixedCapacityStackOfStrings fixedCapacityStackOfStrings = new FixedCapacityStackOfStrings(CAPACITY);
        for (String part : parts) {
            if (part.equals("-")) {
                System.out.println(fixedCapacityStackOfStrings.pop());
            } else {
                fixedCapacityStackOfStrings.push(part);
            }
        }

        System.out.println(fixedCapacityStackOfStrings);
    }
}
