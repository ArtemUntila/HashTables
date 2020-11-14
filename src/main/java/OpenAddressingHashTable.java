import java.util.Iterator;
import java.util.NoSuchElementException;

public class OpenAddressingHashTable<T>{
    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingHashTable(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        storage = new Object[capacity];
    }

    private final Object removed = new Object();

    public int size() {
        return size;
    }

    public boolean contains(Object o) {
        int startingIndex = startingIndex(o);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) break;
            current = storage[index];
        }
        return false;
    }

    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && current != removed) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        int startingIndex = startingIndex(o);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
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
        private Object current;

        public boolean hasNext() {
            return iterations < size;
        }

        @SuppressWarnings("unchecked")
        public T next() {
            if (hasNext()) {
                while (storage[index] == null || storage[index] == removed) index++;
                current = storage[index];
                index++;
                iterations++;
                return (T) current;
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
