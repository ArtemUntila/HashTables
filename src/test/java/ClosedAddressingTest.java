import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ClosedAddressingTest<T> {

    @Test
    public void mainMethodsTest() {
        ClosedAddressingHashTable<Integer> table = new ClosedAddressingHashTable<>();
        Random random = new Random();
        int size = 0;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Integer r = random.nextInt(50);
            if (table.add(r)) {
                size++;
                list.add(r);
            }
        }
        table.add(-50);
        list.add(-50);
        size++;
        Assertions.assertEquals(size, table.size());
        Assertions.assertEquals(list.size(), table.size());
        for (Integer i: list) Assertions.assertTrue(table.contains(i));
        List<Integer> removed = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Integer r = random.nextInt(50);
            if (table.remove(r)) {
                size--;
                removed.add(r);
                list.remove(r);
            }
        }
        Assertions.assertEquals(size, table.size());
        Assertions.assertEquals(list.size(), table.size());
        for (Integer i: list) Assertions.assertTrue(table.contains(i));
        for (Integer i: removed) Assertions.assertFalse(table.contains(i));
    }

    public void iteratorTest(int elements, int bound) { //iterator : next, hasNext, remove
        Random random = new Random();
        ClosedAddressingHashTable<Integer> table = new ClosedAddressingHashTable<>();
        Iterator<Integer> iter = table.iterator();
        Assertions.assertFalse(iter.hasNext());
        Assertions.assertThrows(NoSuchElementException.class, iter::next);

        for (int i = 0; i < elements; i++) table.add(random.nextInt(bound));
        Iterator<Integer> iter1 = table.iterator();
        Iterator<Integer> iter2 = table.iterator();
        //Assertions.assertThrows(IllegalStateException.class, iter1::remove);

        while (iter1.hasNext()) Assertions.assertEquals(iter1.next(), iter2.next());
        Assertions.assertThrows(NoSuchElementException.class, iter1::next);
        Assertions.assertThrows(NoSuchElementException.class, iter2::next);

        Iterator<Integer> iter3 = table.iterator();
        Integer i = iter3.next();
        int size = table.size() - 1;
        iter3.remove();
        Assertions.assertThrows(IllegalStateException.class, iter3::remove);
        Assertions.assertFalse(table.contains(i));
        Assertions.assertEquals(size, table.size());
    }

    @Test
    public void doIteratorTest() {
        iteratorTest(10000, 1000);
        iteratorTest(100000, 1000);
        iteratorTest(1000000, 2000);
    }
}
