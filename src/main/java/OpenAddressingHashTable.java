
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OpenAddressingHashTable<T> {

    private int capacity;

    private final float loadFactor;

    private T[] storage;

    private int size = 0;

    private int hash(T element) {
        int code = element.hashCode();
        if (code >= 0 ) return code % capacity;
        else return (capacity - 1) - (Math.abs(code) % capacity);
    }

    @SuppressWarnings("unchecked")
    public OpenAddressingHashTable(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.storage = (T[]) new Object[capacity];
    }
    public OpenAddressingHashTable(int capacity) {
        this(capacity, 0.75F);
    }

    public OpenAddressingHashTable(float loadFactor) {
        this(16, loadFactor);
    }

    public OpenAddressingHashTable() {
        this(16, 0.75F);
    }

    public int size() {
        return size;
    }

    public boolean contains(T t) {
        int startingIndex = hash(t);
        int index = startingIndex;
        T current = storage[index];
        while (current != null) {
            if (current.equals(t)) {
                return true;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) break;
            current = storage[index];
        }
        return false;
    }

    public boolean add(T t) {
        if (size == (int) (capacity * loadFactor)) resize();
        int index = hash(t);
        T current = storage[index];
        while (current != null && current != removed) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }
    @SuppressWarnings("unchecked")
    private void resize() {
        if (capacity == Integer.MAX_VALUE)
            throw new IllegalStateException("Table size can't be more than max value of integer");
        T[] oldStorage = storage;
        capacity *= 2;
        storage = (T[]) new Object[capacity];
        for (T t: oldStorage) {
            if (t != null) {
                int index = hash(t);
                while (storage[index] != null)
                    index = (index + 1) % capacity;
                storage[index] = t;
            }
        }
    }
    @SuppressWarnings("unchecked")
    private final T removed = (T) new Object();

    public boolean remove(T t) {
        int startingIndex = hash(t);
        int index = startingIndex;
        T current = storage[index];
        while (current != null) {
            if (current.equals(t)) {
                storage[index] = removed;
                size--;
                return true;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) break;
            current = storage[index];
        }
        return false;
    }


    public Iterator<T> iterator() { return new OpenAddressingHashTableIterator();}

    public class OpenAddressingHashTableIterator implements Iterator<T> {

        private int index = 0;
        private int iterations = 0;
        private T current;

        public boolean hasNext() {
            return iterations < size;
        }

        public T next() {
            if (hasNext()) {
                while (storage[index] == null || storage[index] == removed) index++;
                current = storage[index];
                index++;
                iterations++;
                return current;
            } else throw new NoSuchElementException();
        }

        public void remove() {
            if (current != null && current != removed) {
                storage[index - 1] = removed;
                iterations--;
                size--;
            } else throw new IllegalStateException();
        }
    }
}
