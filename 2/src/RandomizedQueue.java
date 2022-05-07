import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] values;
    private int head;
    private int size;
    private int capacity = 2;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.values = (Item[]) new Object[this.capacity];
        this.head = 0;
        this.size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    // add the item
    public void enqueue(Item item) {
        verifyArgumentLegality(item);

        if (this.size == this.capacity) {
            resizeWithShuffle(this.capacity * 2);
        }

        this.values[(this.head + this.size++) % this.capacity] = item;
    }

    // remove and return a random item
    // between head and tail
    public Item dequeue() {
        verifyElementAvailability();

        // picking random from inside queue, swapping it with head, and dequeue-ing
        int random = StdRandom.uniform(this.size);
        Item temp = this.values[(this.head + random) % this.capacity];
        this.values[(this.head + random) % this.capacity] = this.values[this.head];
        this.values[this.head] = temp;

        Item item = this.values[this.head];
        this.values[this.head] = null;
        this.head++;
        this.head %= this.capacity;
        this.size--;

        if (this.size > 0 && this.size <= this.capacity / 4) {
            resizeWithShuffle(this.capacity / 2); // implement a resizeWithShuffle() ?
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        verifyElementAvailability();

        int random = StdRandom.uniform(this.size);
        return this.values[(this.head + random) % this.capacity];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Iterator<>() {
            private int next = 0;
            @Override
            public boolean hasNext() {
                return this.next < RandomizedQueue.this.size;
            }
            @Override
            public Item next() {
                if (hasNext()) {
                    return RandomizedQueue.this.values[(RandomizedQueue.this.head + this.next++) % RandomizedQueue.this.capacity];
                } else {
                    throw new NoSuchElementException();
                }
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

    private void resizeWithShuffle(int resizeCapacity) {
        Item[] copy = (Item[]) new Object[resizeCapacity];

//        // traversal space
//        int[] traversalSpace = new int[this.size];
//        for (int i = 0; i < this.size; i++) {
//            traversalSpace[i] = i;
//        }
//
//        StdRandom.shuffle(traversalSpace);

        for (int i = 0; i < this.size; i++) {
//            copy[i] = this.values[(this.head + traversalSpace[i]) % this.capacity];
            copy[i] = this.values[(this.head + i) % this.capacity];

        }

        this.values = copy;
        this.head = 0;
        this.capacity = resizeCapacity;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        for (int i = 0; i < 50; i++) {
            randomizedQueue.enqueue(StdRandom.uniform(1000));
        }

        final int TEST = 1000;
        for (int i = 0; i < TEST; i++) {
            int random = StdRandom.uniform(1000);
            randomizedQueue.enqueue(random);
            System.out.println("Enqueue: " + random);
            if (StdRandom.bernoulli()) {
                System.out.println("Random dequeue: " + randomizedQueue.dequeue());
                System.out.println("Size: " + randomizedQueue.size());
            }
        }

        System.out.println("Iteration:======================");
        int i = 0;
        for (Integer integer : randomizedQueue) {
            System.out.println(i++ + " " + integer);
        }
        System.out.println("size: " + randomizedQueue.size());
    }
}
