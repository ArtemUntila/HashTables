import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClosedAddressingTest {

    @Test
    public void mainMethodsTest() {
        ClosedAddressingHashTable<Integer> table = new ClosedAddressingHashTable<>();
        Assertions.assertFalse(table.contains(5));
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
}
