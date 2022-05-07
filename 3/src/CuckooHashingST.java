import edu.princeton.cs.algs4.StdRandom;

@SuppressWarnings("unchecked")
public class CuckooHashingST<K, V> {
    private K[] keyHashTable1;
    private K[] keyHashTable2;
    private V[] valueHashTable1;
    private V[] valueHashTable2;

    private int populationHashTable1;
    private int populationHashTable2;

    private int lgM1;
    private int lgM2;

    public static final int initSize;
    public static final long[] primeTable;

    static {
        initSize = 4;
        primeTable = new long[32];
        long twoPower;
        for (int i = 5; i < primeTable.length; i++) {
            twoPower = (long) Math.pow(2, i);
            while (!isPrime(twoPower)) {
                twoPower--;
            }
            primeTable[i] = twoPower;
        }
    }

    public static boolean isPrime(long n) {
        boolean isPrime = true;
        if (n > 2) {
            for (long i = 2; i * i <= n; i++) {
                if (n % i == 0) {
                    isPrime = false;
                    break;
                }
            }
        } else if (n < 2) {
            isPrime = false;
        }
        return isPrime;
    }

    public CuckooHashingST() {
        this.keyHashTable1 = (K[]) new Object[initSize];
        this.keyHashTable2 = (K[]) new Object[initSize];
        this.valueHashTable1 = (V[]) new Object[initSize];
        this.valueHashTable2 = (V[]) new Object[initSize];
        this.lgM1 = this.lgM2 = (int) lg(initSize);
        this.populationHashTable1 = this.populationHashTable2 = 0;
    }

    public void put(K key, V value) {
        if (contains1(key)) {
            this.valueHashTable1[hash1(key)] = value;
        } else if (contains2(key)) {
            this.valueHashTable2[hash2(key)] = value;
        } else {
            K[] keyTargetTable = this.keyHashTable1;
            V[] valueTargetTable = this.valueHashTable1;
            int hashCode = hash1(key);
            while (keyTargetTable[hashCode] != null) {
                K keyT = keyTargetTable[hashCode];
                V valT = valueTargetTable[hashCode];
                keyTargetTable[hashCode] = key;
                valueTargetTable[hashCode] = value;
                key = keyT;
                value = valT;
                if (keyTargetTable == this.keyHashTable1) {
                    keyTargetTable = this.keyHashTable2;
                    valueTargetTable = this.valueHashTable2;
                    hashCode = hash2(key);
                } else {
                    keyTargetTable = this.keyHashTable1;
                    valueTargetTable = this.valueHashTable1;
                    hashCode = hash1(key);
                }
            }

            if (keyTargetTable == this.keyHashTable1) {
                if (populationHashTable1 >= this.keyHashTable1.length / 2) {
                    resize(this.keyHashTable1.length * 2, true);
                }
                this.keyHashTable1[hashCode] = key;
                this.valueHashTable1[hashCode] = value;
                this.populationHashTable1++;
            } else {
                if (populationHashTable2 >= this.keyHashTable2.length / 2) {
                    resize(this.keyHashTable2.length * 2, false);
                }
                this.keyHashTable2[hashCode] = key;
                this.valueHashTable2[hashCode] = value;
                this.populationHashTable2++;
            }
        }
    }

    public V get(K key) {
        V target;
        int hashCode = hash1(key);
        if (key.equals(this.keyHashTable1[hashCode])) {
            target = this.valueHashTable1[hashCode];
        } else {
            hashCode = hash2(key);
            target = this.valueHashTable2[hashCode];
        }
        return target;
    }

    public boolean contains(K key) {
        return contains1(key) || contains2(key);
    }

    private boolean contains1(K key) {
        return key.equals(this.keyHashTable1[hash1(key)]);
    }

    private boolean contains2(K key) {
        return key.equals(this.keyHashTable2[hash2(key)]);
    }

    private double lg(double x) {
        return Math.log(x) / Math.log(2);
    }

    private int hash1(K key) {
        int hashCode = key.hashCode() & 0x7fffffff;
        if (this.lgM1 < 26) {
            hashCode %= primeTable[this.lgM1 + 5];
        }
        return hashCode % this.keyHashTable1.length;
    }


    private int hash2(K key) {
        int hashCode = key.hashCode() & 0x7fffffff;
        if (this.lgM2 < 26) {
            hashCode %= primeTable[this.lgM2 + 5];
        }
        return hashCode % this.keyHashTable2.length;
    }

    private void resize(int newSize, boolean resizeTable1) {
        K[] resizingKeyHashTable;
        V[] resizingValueHashTable;
        System.out.println("Resizing table " + (resizeTable1 ? "1" : "2") + " to size: " + newSize);

        if (resizeTable1) {
            resizingKeyHashTable = this.keyHashTable1;
            resizingValueHashTable = this.valueHashTable1;
            this.keyHashTable1 = (K[]) new Object[newSize];
            this.valueHashTable1 = (V[]) new Object[newSize];
            this.lgM1 = (int) lg(newSize);

            for (int i = 0; i < resizingKeyHashTable.length; i++) {
                if (resizingKeyHashTable[i] != null) {
                    int rehash = hash1(resizingKeyHashTable[i]);
                    this.keyHashTable1[rehash] = resizingKeyHashTable[i];
                    this.valueHashTable1[rehash] = resizingValueHashTable[i];
                }
            }
        } else {
            resizingKeyHashTable = this.keyHashTable2;
            resizingValueHashTable = this.valueHashTable2;
            this.keyHashTable2 = (K[]) new Object[newSize];
            this.valueHashTable2 = (V[]) new Object[newSize];
            this.lgM2 = (int) lg(newSize);

            for (int i = 0; i < resizingKeyHashTable.length; i++) {
                if (resizingKeyHashTable[i] != null) {
                    int rehash = hash2(resizingKeyHashTable[i]);
                    this.keyHashTable2[rehash] = resizingKeyHashTable[i];
                    this.valueHashTable2[rehash] = resizingValueHashTable[i];
                }
            }
        }
    }

    // test client
    public static void main(String[] args) {
        CuckooHashingST<Character, Integer> alphabets = new CuckooHashingST<>();
        for (long prime : primeTable) {
            System.out.println(prime);
        }
        for (int i = 0; i < 50; i++) {
            int randomAlpha = StdRandom.uniform(26);
            int random = StdRandom.uniform(26);
            alphabets.put((char) ('a' + randomAlpha), random);
            System.out.println("Putting: " + (char) ('a' + randomAlpha) + " : " + random);
        }
        for (int i = 0; i < 26; i++) {
            System.out.println((char) ('z' - i) + " : " + alphabets.get((char) ('z' - i)));
        }
    }
}
