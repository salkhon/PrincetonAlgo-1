package com.company;

import java.util.Iterator;

// LinkedList implementation: does not have to deal with resizing
public class StackOfStrings implements Iterable<String> {
    private Node<String> front;

    public StackOfStrings(Node<String> front) {
        this.front = front;
    }

    public void push(String s) {
        this.front = new Node<>(s, this.front); // reference variables are located in stack and it's value (KEY: which is the reference to the object) is copied to methods and constructors
    }

    public String pop() {
        String value = null;
        if (this.front != null) {
            value = this.front.getValue();
            this.front = this.front.getNext(); // if last item was removed, node.getNext() will assign null to this.front again - so the initial null is preserved
        }

        return value;
    }

    public boolean isEmpty() {
        return this.front == null;
    }

    @Override
    public String toString() {
        // for each requires a iterable
        StringBuilder stringBuilder = new StringBuilder("{ ");
        for (String items : this) {
            stringBuilder.append(items).append(" -> ");
        }
        stringBuilder.append("null }");
        return stringBuilder.toString();
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            Node<String> next = StackOfStrings.this.front;
            // we can eliminate the need for null checking by keeping -current- to the -next- element always, so the client hasNext() call does the null checking for us

            @Override
            public boolean hasNext() {
                return this.next != null;
            }

            @Override
            public String next() {
                String item = this.next.getValue();
                this.next = this.next.getNext();
                return item;
            }
        };
    }

    // test client
    public static void main(String[] args) {
        String input = "to be or not to - be - -";
        String[] parts = input.split(" ");
        StackOfStrings stackOfStrings = new StackOfStrings(null);
        for (String part : parts) {
            if (part.equals("-")) {
                System.out.println(stackOfStrings.pop());
            } else {
                stackOfStrings.push(part);
            }
        }

        System.out.println(stackOfStrings);
    }
}
