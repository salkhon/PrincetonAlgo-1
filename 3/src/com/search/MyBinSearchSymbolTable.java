package com.search;

import edu.princeton.cs.algs4.StdRandom;

public class MyBinSearchSymbolTable<K extends Comparable<K>, V> implements SymbolTable<K, V> {
    private K[] keys;
    private V[] values;
    private int size;
    private static final int DEF_CAP = 2;

    @SuppressWarnings("unchecked")
    public MyBinSearchSymbolTable() {
        this.keys = (K[]) new Comparable<?>[DEF_CAP];
        this.values = (V[]) new Object[DEF_CAP];
        this.size = 0; // exclusive
    }

    public void put(K key, V value) {
        int targetIndex = interpolationSearchIndex(key);
        // if target key does not exist
        if (!(targetIndex < this.size && equals(this.keys[targetIndex], key))) {
            // make space at targetIndex, which is insertion point
            // blindly moving keys to the left from target index removes the use of one loop comparison - so slightly faster than bubbling
            if (this.size == this.keys.length) {
                resize(this.keys.length * 2);
            }
            makeSpaceAt(targetIndex);
            this.size++;
        }
        this.keys[targetIndex] = key;
        this.values[targetIndex] = value;
//        if (targetIndex >= 0) {
//            this.values[targetIndex] = value;
//        } else {
//            if (this.size == this.keys.length) {
//                resize(this.keys.length * 2);
//            }
//            this.keys[this.size] = key;
//            this.values[this.size++] = value;
//            // bubbling into place - because identifying position of insertion and then copying elements to make space would require linear time anyway
//            for (int i = this.size - 1; i > 0 && less(this.keys[i], this.keys[i - 1]); i--) {
//                exchange(i, i - 1);
//            }
//        }
    }

    private void makeSpaceAt(int targetIndex) {
        System.arraycopy(this.keys, targetIndex, this.keys, targetIndex + 1, this.size - targetIndex);
        System.arraycopy(this.values, targetIndex, this.values, targetIndex + 1, this.size - targetIndex);
    }

    public V get(K key) {
        int targetIndex = interpolationSearchIndex(key);
        V value = null;
        // if not found, will return the insertion point, which does not equal the key - but the first greater key (or out of bounds if none exist)
        if (targetIndex < this.size && equals(this.keys[targetIndex], key)) {
            value = this.values[targetIndex];
        }
        return value;
    }

    public void delete(K key) {
        int targetIndex = interpolationSearchIndex(key);
        if (targetIndex < this.size && equals(this.keys[targetIndex], key)) {
            moveLeftAt(targetIndex);
            this.size--;
        }
    }

    private void moveLeftAt(int index) {
        System.arraycopy(this.keys, index + 1, this.keys, index, this.size - (index + 1));
        System.arraycopy(this.values, index + 1, this.values, index, this.size - (index + 1));
        this.keys[this.size - 1] = null;
        this.values[this.size - 1] = null;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    private void exchange(int index1, int index2) {
        K tempKey = this.keys[index1];
        this.keys[index1] = this.keys[index2];
        this.keys[index2] = tempKey;

         V tempValue = this.values[index1];
         this.values[index1] = this.values[index2];
         this.values[index2] = tempValue;
    }

    // invariant: the left of lo pointer is lesser than the target, the right of hi pointer is greater than the target
    // so if the key where lo and hi are equal is lesser than target key, lo moves right - where key must be inserted
    // if that key is greater than target key, hi moves left - where the right subarray needs to be moved left to make space for insertion - insert at lo
    private int binSearchIndex(K key) {
        int lo = 0, hi = this.size - 1;
        int mid;
        while (lo <= hi) {
            mid = (lo + hi) / 2;
            if (less(this.keys[mid], key)) {
                lo = mid + 1;
            } else if (less(key, this.keys[mid])) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }

        return lo;
    }

    private int interpolationSearchIndex(K key) {
        int targetIndex;
        boolean isInterpolationPossible = false;

        if (!isEmpty() && !minKey().equals(maxKey())) {
            isInterpolationPossible = true;
        }

        if (key instanceof Number && isInterpolationPossible) {
            System.out.println("interpolating");
            int lo = 0, hi = this.size - 1;
            int interpolationPoint;
            Number keyN = (Number) key;
            Number minKeyN = (Number) minKey(), maxKeyN = (Number) maxKey();
            double interpolationCoefficient = (keyN.doubleValue() - minKeyN.doubleValue()) / (maxKeyN.doubleValue() - minKeyN.doubleValue());
            if (interpolationCoefficient < 0) {
                interpolationCoefficient = 0; // insertion at first
            } else if (interpolationCoefficient > 1) {
                interpolationCoefficient = 1; // insertion at last
            }
            
            while (lo <= hi) {
                interpolationPoint = (int) (lo + (hi - lo) * interpolationCoefficient);

                if (less(key, this.keys[interpolationPoint])) {
                    hi = interpolationPoint - 1;
                } else if (less(this.keys[interpolationPoint], key)) {
                    lo = interpolationPoint + 1;
                } else {
                    lo = interpolationPoint;
                    break;
                }
            }
            targetIndex = lo;
        } else {
            targetIndex = binSearchIndex(key);
        }
        return targetIndex;
    }

    public K maxKey() {
        return this.keys[this.size - 1];
    }

    public K minKey() {
        return this.keys[0];
    }

    public static <C extends Comparable<C>> boolean equals(C k1, C k2) {
        return k1.compareTo(k2) == 0;
    }

    public static <C extends Comparable<C>> boolean less(C k1, C k2) {
        return k1.compareTo(k2) < 0;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newLength) {
        K[] keysCopy = (K[]) new Comparable<?>[newLength];
        System.arraycopy(this.keys, 0, keysCopy, 0, this.size);
        this.keys = keysCopy;

        V[] valuesCopy = (V[]) new Object[newLength];
        System.arraycopy(this.values, 0, valuesCopy, 0, this.size);
        this.values = valuesCopy;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[ ");
        for (int i = 0; i < this.keys.length; i++) {
            stringBuilder.append(this.keys[i]).append('-').append(this.values[i]);
            if (i < this.keys.length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(" ]").append(" size: ").append(this.size);
        return stringBuilder.toString();
    }

    public static void insertionTimePercentage() {
        int[] numOfSearches = { 1000, 1000000, 100000000 };
        final int SIZE = 10000;
        MyBinSearchSymbolTable<Integer, String> symbolTable = new MyBinSearchSymbolTable<>();

        for (int i = 0; i < SIZE; i++) {
            symbolTable.put(i, "value " + i);
        }

        for (int search : numOfSearches) {
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < search; i++) {
                symbolTable.get(StdRandom.uniform(SIZE));
            }
            long searchTime = System.currentTimeMillis() - t1;

            t1 = System.currentTimeMillis();
            int insertion = search / 1000;
            for (int i = 0; i < insertion; i++) {
                int random = StdRandom.uniform(SIZE);
                symbolTable.put(random, "Value " + random);
            }
            long insertionTime = System.currentTimeMillis() - t1;
            System.out.println("Percentage of insertion time: " + ((double) insertionTime / (insertionTime + searchTime)) + " size: " + search);
        }
    }

    // test client
    public static void main(String[] args) {
        //insertionTimePercentage();
        MyBinSearchSymbolTable<Integer, Character> alphabets = new MyBinSearchSymbolTable<>();

        alphabets.put(4, 'D');
        System.out.println(alphabets);

        alphabets.put(7, 'F');
        System.out.println(alphabets);

        alphabets.put(6, 'G');
        System.out.println(alphabets);

        alphabets.put(10, 'J');
        System.out.println(alphabets);

        alphabets.put(12, 'l');
        alphabets.put(24, 'X');
        alphabets.put(5, 'E');
        alphabets.put(12, 'L');
        alphabets.put(22, 'V');
        alphabets.put(18, 'R');
        alphabets.put(17, 'Q');
        alphabets.put(11, 'K');
        alphabets.put(2, 'B');
        alphabets.put(1, 'A');

        for (int i = 0; i < 20; i++) {
            int random = StdRandom.uniform(27);
            System.out.println("Alphabet: key " + random + " values " + alphabets.get(random));
        }

        System.out.println("Before Deletion: " + alphabets);
        alphabets.delete(4); alphabets.delete(1);
        System.out.println("After deletion: " + alphabets);
    }
}
