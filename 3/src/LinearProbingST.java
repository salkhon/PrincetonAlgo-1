@SuppressWarnings("unchecked")
public class LinearProbingST<K, V> {
    private static final int INIT_SIZE = 16;
    private K[] keysHashTable;
    private V[] valuesHashTable;
    private int numOfElements;

    public LinearProbingST() {
        this.numOfElements = 0;
        this.keysHashTable = (K[]) new Object[INIT_SIZE];
        this.valuesHashTable = (V[]) new Object[INIT_SIZE];
    }

    public void put(K key, V value) {
        if (this.numOfElements >= this.keysHashTable.length / 2) {
            resize(this.keysHashTable.length * 2);
        }

        int i = hash(key);
        while (this.keysHashTable[i] != null) {
            i++;
            i %= this.keysHashTable.length;
        }
        this.keysHashTable[i] = key;
        this.valuesHashTable[i] = value;
        this.numOfElements++;
    }

    public V get(K key) {
        V target = null;
        for (int i = hash(key); this.keysHashTable[i] != null; i++, i %= this.keysHashTable.length) {
            if (this.keysHashTable[i].equals(key)) {
                target = this.valuesHashTable[i];
                break;
            }
        }
        return target;
    }

    public V delete(K key) {
        int hashCode = hash(key);
        V deleted = null;
        if (contains(key)) {
            int h = hashCode;
            while (!this.keysHashTable[h].equals(key)) {
                h++;
                h %= this.keysHashTable.length;
            }
            this.keysHashTable[h] = null;
            deleted = this.valuesHashTable[h];
            this.valuesHashTable[h] = null;
            K reallocationKey;
            V reallocationVal;
            for (int i = (h + 1) % this.keysHashTable.length; this.keysHashTable[i] != null; i++, i %= this.keysHashTable.length) {
                reallocationKey = this.keysHashTable[i];
                this.keysHashTable = null;
                reallocationVal = this.valuesHashTable[i];
                this.valuesHashTable[i] = null;
                this.numOfElements--;
                put(reallocationKey, reallocationVal);
            }
            if (this.numOfElements > 4 && this.numOfElements < this.keysHashTable.length / 8) {
                resize(this.keysHashTable.length / 2);
            }
        }
        return deleted;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % this.keysHashTable.length;
    }

    private void resize(int newSize) {
        K[] reallocatedKeys = (K[]) new Object[newSize];
        V[] reallocatedValues = (V[]) new Object[newSize];
        int hashCode;
        for (int i = 0; i < this.keysHashTable.length; i++) {
            hashCode = hash(this.keysHashTable[i]);
            reallocatedKeys[hashCode] = this.keysHashTable[i];
            reallocatedValues[hashCode] = this.valuesHashTable[i];
        }
        this.keysHashTable = reallocatedKeys;
        this.valuesHashTable = reallocatedValues;
    }

    public static void main(String[] args) {
        String weirdString = "polygenelubricants";
        System.out.println(weirdString.hashCode());
        int hash = 0;
        for (int i = 0; i < weirdString.length(); i++) {
            hash = weirdString.charAt(i) + hash * 31;
        }
        System.out.println(hash); // overflow is causing it
        System.out.println(0x7fffffff + 1);
    }
}
