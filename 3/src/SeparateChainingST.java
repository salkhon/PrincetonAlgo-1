

public class SeparateChainingST<K extends Comparable<K>, V> {
    private static final int initLength = 4;
    private int numOfElements;
    private Node[] hashTable;

    public SeparateChainingST() {
        this.numOfElements = 0;
        this.hashTable = new Node[initLength];
    }

    public void put(K key, V value) {
        if (this.numOfElements >= this.hashTable.length * 5) {
            resizeArray(this.hashTable.length * 2);
        }

        int hashCode = hash(key);
        boolean contains = false;
        for (Node current = this.hashTable[hashCode]; current != null; current = current.getNext()) {
            if (current.getKey().equals(key)) {
                current.setValue(value);
                contains = true;
                break;
            }
        }
        if (!contains) {
            Node node = new Node(key, value);
            node.setNext(this.hashTable[hashCode]);
            this.hashTable[hashCode] = node;
            this.numOfElements++;
        }
    }

    @SuppressWarnings("unchecked")
    public V get(K key) {
        V target = null;
        int hashCode = hash(key);
        for (Node current = this.hashTable[hashCode]; current != null; current = current.getNext()) {
            if (current.getKey().equals(key)) {
                target = (V) current.getValue();
                break;
            }
        }
        return target;
    }

    @SuppressWarnings("unchecked")
    public V delete(K key) {
        V deletedValue = null;
        int hashCode = hash(key);
        for (Node prev = null, current = this.hashTable[hashCode]; current != null; prev = current, current = current.getNext()) {
            if (current.getKey().equals(key)) {
                if (prev != null) {
                    prev.setNext(current.getNext());
                } else {
                    this.hashTable[hashCode] = current.getNext();
                }
                deletedValue = (V) current.getValue();
                this.numOfElements--;
                if (this.numOfElements > 0 && this.numOfElements < this.hashTable.length / 4) {
                    resizeArray(this.hashTable.length / 2);
                }
                break;
            }
        }
        return deletedValue;
    }

    // ensuring user defined K types are properly hashed
    private int hash(K key) {
        return (key.hashCode() % 0x7FFFFFFF) % this.hashTable.length;
    }

    // keep N / M ~ 5
    private void resizeArray(int newSize) {
        Node[] oldHashTable = this.hashTable;
        this.hashTable = new Node[newSize];
        for (Node root : oldHashTable) {
            putAllLinkedNodeInNewHashTable(root);
        }
    }

    @SuppressWarnings("unchecked")
    private void putAllLinkedNodeInNewHashTable(Node root) {
        int hashCode;
       for (Node current = root; current != null; current = current.getNext()) {
            hashCode = hash((K) current.getKey());
            current.setNext(this.hashTable[hashCode]);
            this.hashTable[hashCode] = current;
        }
    }

    private static class Node {
        Object key;
        Object value;
        Node next;

        public Node(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}
