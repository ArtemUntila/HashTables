
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class OpenAddressingTest {
    @Test
    public void addTest() {
        OpenAddressingHashTable<Integer> table = new OpenAddressingHashTable<>();
        Random random = new Random();
        for (int i = 0; i < 13; i++)
            table.add(random.nextInt(100));
        Assertions.assertEquals(13, table.size());
        Assertions.assertEquals(32, table.capacity);
        int elements = 13;
        for (int i = 0; i < 10000; i++) {
            Integer r = random.nextInt(100);
           try {
               if (table.remove(r))
                   elements--;
           } catch (IndexOutOfBoundsException e) {

               System.out.println("We found a problem: " + r);
           }
        }
        Assertions.assertEquals(elements, table.size());
    }
}
