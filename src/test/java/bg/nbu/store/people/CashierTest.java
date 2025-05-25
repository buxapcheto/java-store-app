package bg.nbu.store.people;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CashierTest {

    @Test
    void cashierFieldsAreSetCorrectly() {
        Cashier c = new Cashier("C1", "Ivan", 1000.0);
        assertEquals("C1", c.getId());
        assertEquals("Ivan", c.getName());
        assertEquals(1000.0, c.getSalary(), 1e-6);
    }
}