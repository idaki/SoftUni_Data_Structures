import java.security.Key;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class HashTable<K, V> implements Iterable<KeyValue<K, V>> {
    private final static int INITIAL_CAPACITY = 16;
    private final static double LOAD_FACTOR = 0.80d;
    private LinkedList<KeyValue<K, V>>[] slots;

    private int count;
    private int capacity;

    public HashTable() {
        this(INITIAL_CAPACITY);
    }

    public HashTable(int capacity) {
        this.slots = new LinkedList[capacity];

        this.count = 0;
        this.capacity = capacity;
    }

    public void add(K key, V value) {
        //throw new UnsupportedOperationException();
        this.growIfNeeded();


        int index = findSlotNumber(key);

        LinkedList<KeyValue<K, V>> list = this.slots[index];

        if (list == null) {
            list = new LinkedList<>();
        }

        for (KeyValue<K, V> current : list) {
            if (current.getKey().equals(key)) {
                throw new IllegalStateException("Key exists");
            }
        }
        KeyValue<K, V> toInsert = new KeyValue<>(key, value);

        list.add(toInsert);

        this.slots[index] = list;
        this.count++;
    }

    private int findSlotNumber(K key) {
        // throw new UnsupportedOperationException();
        return Math.abs(key.hashCode() % this.capacity);
    }

    private void growIfNeeded() {
        //    throw new UnsupportedOperationException();
        if (false) {
            grow();
        }
    }

    private void grow() {
        throw new UnsupportedOperationException();
    }

    public int size() {
        // throw new UnsupportedOperationException();
        return this.count;
    }

    public int capacity() {
        //  throw new UnsupportedOperationException();
        return this.capacity;
    }

    public boolean addOrReplace(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public V get(K key) {
        throw new UnsupportedOperationException();
    }

    public KeyValue<K, V> find(K key) {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(K key) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(K key) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public Iterable<K> keys() {
        throw new UnsupportedOperationException();
    }

    public Iterable<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<KeyValue<K, V>> iterator() {
        //  throw new UnsupportedOperationException();
        return new Iterator<KeyValue<K, V>>() {
            int passedThroughCount = 0;
            int currentIndex = 0;
            Deque<KeyValue<K,V>> elements = new ArrayDeque();

            @Override
            public boolean hasNext() {
                return passedThroughCount < count;
            }

            @Override
            public KeyValue<K, V> next() {
                if(!elements.isEmpty()){
                    return elements.poll();
                }
                while (slots[currentIndex] == null) {
                    currentIndex++;
                }
                elements.addAll(slots[currentIndex]);

                currentIndex++;
                passedThroughCount+= slots[currentIndex].size();
                return elements.poll();
            }
        };
    }
}
