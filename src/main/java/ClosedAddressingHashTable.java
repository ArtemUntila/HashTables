import java.util.LinkedList;

public class ClosedAddressingHashTable<T> {
    public int capacity;

    private final float loadFactor;

    private LinkedList<T>[] storage;

    private int size = 0;

    private int hash(T element) {
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

    public boolean contains(T t) {
        return storage[hash(t)].contains(t);
    }

    public boolean add(T t) {
        if (size == (int) (capacity * loadFactor)) resize();
        int index = hash(t);
        if (storage[index] == null) {
            storage[index] = new LinkedList<>();
            storage[index].add(t);
        }
        else if (!storage[index].contains(t))
            storage[index].add(t);
        else return false;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        if (capacity <= 0)
            throw new IllegalStateException("Table capacity can't be more than max value of integer");
        LinkedList<T>[] oldStorage = storage;
        storage = new LinkedList[capacity];
        for (LinkedList<T> list: oldStorage)
            if (list != null)
                for (T t : list) {
                int index = hash(t);
                if (storage[index] == null)
                    storage[index] = new LinkedList<>();
                storage[index].add(t);
            }
    }

    //public boolean remove(T t) {}
}
