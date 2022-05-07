package com.company;

public class MoveToFront<T> {
    private Node<T> first;

    public MoveToFront() {
    }

    public void insert (T item) {
        deleteIfContains(item);
        this.first = new Node<>(item, this.first);
    }

    private void deleteIfContains(T item) {
        if (this.first != null) {
            Node<T> current = this.first;

            if (!this.first.getValue().equals(item)) {
                while (current.getNext() != null) {
                    if (current.getNext().getValue().equals(item)) {
                        current.setNext(current.getNext().getNext());
                        break;
                    }
                    current = current.getNext();
                }

            } else {
                this.first = this.first.getNext();
            }
        }
    }

    @Override
    public String toString() {
        Node<T> current = this.first;

        StringBuilder stringBuilder = new StringBuilder("[ ");
        while (current != null) {
            stringBuilder.append(current.getValue());
            if (current.getNext() != null) {
                stringBuilder.append(" -> ");
            }
            current = current.getNext();
        }
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }

    // test client
    public static void main(String[] args) {
        MoveToFront<Character> moveToFront = new MoveToFront<>();
        Character[] characters = new Character[] { 'a', 'b', 'c', 'a', 'd', 'e', 'g', 't', 't', 'g', 'e' };
        for (char c : characters) {
            moveToFront.insert(c);
            System.out.println(moveToFront);
        }
    }
}
