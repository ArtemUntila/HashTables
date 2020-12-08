import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class ClosedAddressingTest {

    @Test
    public void mainMethodsTest() {
        ClosedAddressingHashTable<Integer> table = new ClosedAddressingHashTable<>();
        Random random = new Random();
        int size = 0;
        for (int i = 0; i < 100; i++) {
            if (table.add(random.nextInt(50))) size++;
        }
        Assertions.assertEquals(size, table.size());
    }
}
