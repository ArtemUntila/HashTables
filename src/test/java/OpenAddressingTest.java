
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class OpenAddressingTest {
    @Test
    public void addTest() {
        OpenAddressingHashTable<Integer> table = new OpenAddressingHashTable<>();
        Random random = new Random();
        int size = 0;

        for (int i = 0; i < 13; i++)
            if (table.add(random.nextInt(100)))
                size++;

        Assertions.assertEquals(size, table.size());

        for (int i = 0; i < 10000; i++)
            if (table.remove(random.nextInt(100)))
                size--;

        Assertions.assertEquals(size, table.size());
    }
}
