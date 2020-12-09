
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class OpenAddressingHashTable<T> implements Set<T> {

    private int capacity;

    private final float loadFactor;

    private T[] storage;

    private int size = 0;

    private int hash(Object element) {
        int code = element.hashCode();
        if (code >= 0) return code % capacity;
        else return (capacity - 1) - (Math.abs(code) % capacity);
    }

    @SuppressWarnings("unchecked")
    public OpenAddressingHashTable(int capacity, float loadFactor) {
        if (capacity <= 0 || loadFactor <= 0) throw new IllegalArgumentException("Illegal parameters for table");
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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        int startingIndex = hash(o);
        int index = startingIndex;
        T current = storage[index];
        while (current != null) {
            if (current.equals(o)) return true;
            index = (index + 1) % capacity;
            if (index == startingIndex) break;
            current = storage[index];
        }
        return false;
    }

    @Override
    public boolean add(T t) {
        if (size == (int) (capacity * loadFactor)) resize();
        int index = hash(t);
        T current = storage[index];
        while (current != null && current != removed) {
            if (current.equals(t)) return false;
            index = (index + 1) % capacity;
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        if (capacity <= 0) throw new IllegalStateException("Table capacity can't be more than max value of integer");
        T[] oldStorage = storage;
        storage = (T[]) new Object[capacity];
        for (T t : oldStorage) {
            if (t != null) {
                int index = hash(t);
                while (storage[index] != null) index = (index + 1) % capacity;
                storage[index] = t;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private final T removed = (T) new Object();

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int startingIndex = hash(o);
        int index = startingIndex;
        T current = storage[index];
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

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c.isEmpty()) return false;
        for (Object o : c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = false;
        if (!c.isEmpty())
            for (T t : c)
                if (t != null) changed = add(t);
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.isEmpty()) return false;
        boolean changed = false;
        if (size > c.size())
            for (Object o : c)
                changed = remove(o);
        else changed = removeIf(c::contains); //use iterator()
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        storage = (T[]) new Object[capacity];
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        while (iterator().hasNext()) {
            array[index] = iterator().next();
            index++;
        }
        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new OpenAddressingHashTableIterator();
    }

    public class OpenAddressingHashTableIterator implements Iterator<T> {

        private int index = 0;
        private int iterations = 0;
        private T current;

        @Override
        public boolean hasNext() {
            return iterations < size;
        }

        @Override
        public T next() {
            if (hasNext()) {
                while (storage[index] == null || storage[index] == removed) index++;
                current = storage[index];
                index++;
                iterations++;
                return current;
            } else throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            if (current != null && current != removed) {
                storage[index - 1] = removed;
                iterations--;
                size--;
            } else throw new IllegalStateException();
        }
    }
}
