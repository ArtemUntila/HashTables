import java.util.*;

public class ClosedAddressingHashTable<T> implements Set<T> {
    public int capacity;

    private final float loadFactor;

    private LinkedList<T>[] storage;

    private int size = 0;

    private int hash(Object element) {
        int code = element.hashCode();
        if (code >= 0 ) return code % capacity;
        else return (capacity - 1) - (Math.abs(code) % capacity);
    }

    @SuppressWarnings("unchecked")
    public ClosedAddressingHashTable(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.storage = new LinkedList[capacity];
    }
    public ClosedAddressingHashTable(int capacity) {
        this(capacity, 0.75F);
    }

    public ClosedAddressingHashTable(float loadFactor) {
        this(16, loadFactor);
    }

    public ClosedAddressingHashTable() {
        this(16, 0.75F);
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        LinkedList<T> current = storage[hash(o)];
        return current != null && current.contains(o);
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    public boolean add(T t) {
        if (size == (int) (capacity * loadFactor)) resize();
        int index = hash(t);
        LinkedList<T> current = storage[index];
        if (current == null) {
            storage[index] = new LinkedList<>();
            storage[index].add(t);
        }
        else if (!current.contains(t)) storage[index].add(t);
        else return false;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        if (capacity <= 0) throw new IllegalStateException("Table capacity can't be more than max value of integer");
        LinkedList<T>[] oldStorage = storage;
        storage = new LinkedList[capacity];
        for (LinkedList<T> list: oldStorage)
            if (list != null)
                for (T t : list) {
                int index = hash(t);
                if (storage[index] == null) storage[index] = new LinkedList<>();
                storage[index].add(t);
            }
    }

    public boolean remove(Object o) {
        int index = hash(o);
        if (storage[index] != null && storage[index].remove(o)) {
            size--;
            return true;
        }
        else return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Iterator<T> iterator() {
        return new ClosedAddressingHashTableIterator();
    }

    private class ClosedAddressingHashTableIterator implements Iterator<T> {

        private int iterations;

        private T current;

        private Iterator<T> iter;

        private int index;

        private LinkedList<T> currentList;

        public ClosedAddressingHashTableIterator() {
            current = null;
            currentList = null;
            iter = null;
            iterations = 0;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return iterations < size;
        }

        @Override
        public T next() {
           if (hasNext()) {
               if(iter == null || !iter.hasNext()) {
                   while (currentList == null || currentList.isEmpty()) {
                       index++;
                       currentList = storage[index];
                   }
                   iter = currentList.iterator();
               }
               current = iter.next();
               iterations++;
               return current;
           } else throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            if (current != null) {
                iter.remove();
                current = null;
                size--;
                iterations--;
            } else throw new IllegalStateException();
        }
    }
}
