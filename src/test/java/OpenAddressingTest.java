
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class OpenAddressingTest {
    @Test
    public void addAndRemoveTest() {
        OpenAddressingHashTable<Integer> table = new OpenAddressingHashTable<>();
        Random random = new Random();
        int size = 0;

        for (int i = 0; i < 10000; i++)
            if (table.add(random.nextInt(1000)))
                size++;

        Assertions.assertEquals(size, table.size());

        for (int i = 0; i < 10000; i++)
            if (table.remove(random.nextInt(1000)))
                size--;

        Assertions.assertEquals(size, table.size());
    }
}
