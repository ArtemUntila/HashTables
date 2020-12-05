
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OpenAddressingHashTable<T> {

    public int capacity;

    private final float loadFactor;

    private Object[] storage;

    private int size = 0;

    private int hash(Object element) {
        int code = element.hashCode();
        if (code >= 0 ) return code % storage.length;
        else return (storage.length - 1) - (Math.abs(code) % storage.length);
    }

    public OpenAddressingHashTable(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.storage = new Object[capacity];
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

    private final Object removed = new Object();

    public int size() {
        return size;
    }

    public boolean contains(Object o) {
        int startingIndex = hash(o);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % storage.length;
            if (index == startingIndex) break;
            current = storage[index];
        }
        return false;
    }

    public boolean add(Object o) {
        if (size == (int) (capacity * loadFactor)) resize();
        int index = hash(o);
        Object current = storage[index];
        while (current != null && current != removed) {
            /*if (current.equals(o)) {
                return false;
            }*/
            index = (index + 1) % storage.length;
            current = storage[index];
        }
        storage[index] = o;
        size++;
        return true;
    }

    private void resize() {
        if (capacity == Integer.MAX_VALUE)
            throw new IllegalStateException("Table size can't be more than max value of integer");
        Object[] oldStorage = storage;
        capacity *= 2;
        storage = new Object[capacity];
        int s = size;
        for (Object element: oldStorage) {
            if (element != null) this.add(element);
        }
        size -= s;
    }

    public boolean remove(Object o) {
        int startingIndex = hash(o);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                storage[index] = removed;
                size--;
                return true;
            }
            index = (index + 1) % storage.length;
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
