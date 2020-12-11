import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ClosedAddressingTest {

    public void mainMethodsTest(int elements, int bound, int count) {
        Random random = new Random();
        for (int k = 0; k < count; k++) {
            ClosedAddressingHashTable<Integer> table = new ClosedAddressingHashTable<>();
            int size = 0;
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < elements; i++) {
                Integer r = random.nextInt(bound);
                if (table.add(r)) {
                    list.add(r);
                    size++;
                }
            }
            Assertions.assertEquals(size, table.size());
            Assertions.assertEquals(list.size(), table.size());

            for (Integer i : list) Assertions.assertTrue(table.contains(i));

            List<Integer> removed = new ArrayList<>();
            for (int i = 0; i < elements; i++) {
                Integer r = random.nextInt(bound);
                if (table.remove(r)) {
                    size--;
                    removed.add(r);
                    list.remove(r);
                }
            }
            Assertions.assertFalse(table.remove(null));
            Assertions.assertFalse(table.contains(null));
            Assertions.assertEquals(size, table.size());
            Assertions.assertEquals(list.size(), table.size());

            for (Integer i : removed) Assertions.assertFalse(table.contains(i));
        }
    }

    public void iteratorTest(int elements, int bound, int count) { //iterator : next, hasNext, remove
        Random random = new Random();
        for (int k = 0; k < count; k++) {
            ClosedAddressingHashTable<Integer> table = new ClosedAddressingHashTable<>();
            Iterator<Integer> iter = table.iterator();
            Assertions.assertFalse(iter.hasNext());
            Assertions.assertThrows(NoSuchElementException.class, iter::next);

            for (int i = 0; i < elements; i++) table.add(random.nextInt(bound));
            Iterator<Integer> iter1 = table.iterator();
            Iterator<Integer> iter2 = table.iterator();
            Assertions.assertThrows(IllegalStateException.class, iter1::remove);

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

            while (iter3.hasNext()) {
                Integer element = iter3.next();
                iter3.remove();
                Assertions.assertFalse(table.contains(element));
                size--;
                Assertions.assertEquals(size, table.size());
            }
            Assertions.assertTrue(table.isEmpty());
        }
    }

    public void additionalMethodsTest(int elements, int bound, int count) {
        // addAll, containsAll, removeAll, isEmpty, toArray
        Random random = new Random();
        for (int k = 0; k < count; k++) {
            ClosedAddressingHashTable<Integer> table = new ClosedAddressingHashTable<>();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < elements; i++) {
                int r = random.nextInt(bound);
                if (!list.contains(r)) list.add(r);
            }
            Assertions.assertTrue(table.addAll(list));
            Assertions.assertTrue(table.containsAll(list));

            Object[] array = table.toArray();
            Assertions.assertEquals(array.length, table.size());
            for (Object o : array) Assertions.assertTrue(table.contains(o));

            Assertions.assertTrue(table.removeAll(list));
            for (Integer i : list) Assertions.assertFalse(table.contains(i));
            Assertions.assertTrue(table.isEmpty());

            Assertions.assertTrue(table.addAll(list));
            table.clear();
            Assertions.assertTrue(table.isEmpty());
        }
    }

    @Test
    public void doMainMethodsTest() {
        mainMethodsTest(10000, 1000, 10);
        mainMethodsTest(100000, 1000, 5);
        mainMethodsTest(1000000, 1000, 1);
    }

    @Test
    public void doIteratorTest() {
        iteratorTest(10000, 1000, 10);
        iteratorTest(100000, 1000, 5);
        iteratorTest(1000000, 1000, 1);
    }

    @Test
    public void doAdditionalMethodsTest() {
        additionalMethodsTest(10000, 1000, 10);
        additionalMethodsTest(100000, 1000, 5);
        additionalMethodsTest(1000000, 1000, 1);
    }

    @Test
    public void retainTest() {
        List<Integer> list = List.of(1);
        OpenAddressingHashTable<Integer> table = new OpenAddressingHashTable<>();
        table.add(1);
        Assertions.assertFalse(table.retainAll(list));
        table.add(2);
        Assertions.assertTrue(table.retainAll(list));
    }
}
