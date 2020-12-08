
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OpenAddressingTest {

    public void mainMethodsTest(int elements, int bound, int count) {
        Random random = new Random();
        for (int k = 0; k < count; k++) {
            OpenAddressingHashTable<Integer> table = new OpenAddressingHashTable<>();
            int size = 0;
            List<Integer> added = new ArrayList<>();
            for (int i = 0; i < elements; i++) {
                Integer r = random.nextInt(bound);
                if (table.add(r)) {
                    added.add(r);
                    size++;
                }
            }
            Assertions.assertEquals(size, table.size());
            Assertions.assertEquals(added.size(), table.size());
            for (Integer i: added) Assertions.assertTrue(table.contains(i));
            List<Integer> removed = new ArrayList<>();
            for (int i = 0; i < elements; i++) {
                Integer r = random.nextInt(bound);
                if (table.remove(r)) {
                    size--;
                    removed.add(r);
                    added.remove(r);
                }
            }
            Assertions.assertEquals(size, table.size());
            Assertions.assertEquals(added.size(), table.size());
            for (Integer i: removed) Assertions.assertFalse(table.contains(i));
            for (Integer i: added) Assertions.assertTrue(table.contains(i));
        }
    }

    @Test
    public void doMainMethodsTest() {
        mainMethodsTest(10000, 1000, 10);
        mainMethodsTest(100000, 1000, 5);
        mainMethodsTest(1000000, 2000, 1);
    }
}
