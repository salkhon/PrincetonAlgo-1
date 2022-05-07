import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] values;
    private int head;
    private int size;
    private int capacity = 2;

    // construct an empty deque
    public Deque() {
        this.values = (Item[]) new Object[this.capacity];
        this.head = 0;
        this.size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        verifyArgumentLegality(item);

        if (size() == this.capacity) {
            resize(this.capacity * 2);
        }

        if (!isEmpty()) {
            this.head--;
            if (this.head == -1) {
                this.head = this.capacity - 1;
            }
        }
        this.values[this.head] = item;
        this.size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        verifyArgumentLegality(item);

        if (size() == this.capacity) {
            resize(this.capacity * 2);
        }

        this.values[(this.head + this.size++) % this.capacity] = item;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        verifyElementAvailability();

        Item item = this.values[this.head];
        this.head++;
        this.head %= this.capacity;
        this.size--;

        if (this.size > 0 && this.size <= this.capacity / 4) {
            resize(this.capacity / 2);
        }

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        verifyElementAvailability();

        Item item = this.values[(this.head + --this.size) % this.capacity];
        this.values[(this.head + this.size) % this.capacity] = null;

        if (this.size > 0 && this.size <= this.capacity / 4) {
            resize(this.capacity / 2);
        }

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iterator<>() {
            private int next = 0;
            @Override
            public boolean hasNext() {
                return this.next < Deque.this.size;
            }
            @Override
            public Item next() {
                if (hasNext()) {
                    return Deque.this.values[(Deque.this.head + this.next++) % Deque.this.capacity];
                } else {
                    throw new NoSuchElementException();
                }
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private void verifyArgumentLegality(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    private void verifyElementAvailability() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    private void resize(int resizedCapacity) {
        Item[] copy = (Item[]) new Object[resizedCapacity];
        for (int i = this.head, j = 0; j < this.size; i = (i + 1) % this.capacity, j++) {
            copy[j] = this.values[i];
        }

        this.values = copy;
        this.head = 0;
        this.capacity = resizedCapacity;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> integerDeque = new Deque<>();

        final int TEST = 1000;
        for (int i = 0; i < TEST; i++) {
            int random = StdRandom.uniform(1000);
            if (StdRandom.bernoulli()) {
                integerDeque.addFirst(random);
                System.out.println("Added First: " + random);
            } else {
                integerDeque.addLast(random);
                System.out.println("Added Last: " + random);
            }

            if (StdRandom.bernoulli()) {
                if (StdRandom.bernoulli()) {
                    System.out.println("Removed First: " + integerDeque.removeFirst());
                } else {
                    System.out.println("Removed Last: " + integerDeque.removeLast());
                }

                System.out.println("Size: " + integerDeque.size());
            }
        }

        System.out.println("Remaining:===================================");
        int size = integerDeque.size();
        for (int i = 0; i < size; i++) {
            System.out.println(integerDeque.removeFirst());
        }
        System.out.println("Now Empty: " + integerDeque.isEmpty());
    }
}
