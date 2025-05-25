package bg.nbu.store.domain;

import bg.nbu.store.exceptions.InsufficientQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {

    private InventoryItem item;
    private Product p;

    @BeforeEach
    void setup() {
        // използваме NonFoodProduct, quantity = 5
        p = new NonFoodProduct("P1", "Prod", 50.0,
                LocalDate.of(2025, 12, 31), 5);
        item = new InventoryItem(p);
    }

    @Test
    void initialQuantityMatchesProduct() {
        assertEquals(5, item.getQuantity());
    }

    @Test
    void removeReducesProductQuantity() {
        item.remove(3);
        assertEquals(2, item.getQuantity());
    }

    @Test
    void removeMoreThanAvailableThrows() {
        assertThrows(InsufficientQuantityException.class,
                () -> item.remove(10));
    }
}
