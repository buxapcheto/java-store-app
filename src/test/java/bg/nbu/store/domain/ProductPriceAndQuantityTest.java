package bg.nbu.store.domain;

import bg.nbu.store.config.DefaultMarkupPolicy;
import bg.nbu.store.config.MarkupPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProductPriceAndQuantityTest {

    private MarkupPolicy policy;
    private Product p;
    private LocalDate today;
    private LocalDate expiry;

    @BeforeEach
    void setUp() {
        policy = new DefaultMarkupPolicy();
        today = LocalDate.of(2025, 5, 25);
        expiry = today.plusDays(5);
        // Използваме готовия клас NonFoodProduct, който задава категория и costPrice
        p = new NonFoodProduct("X1", "Test", 100.0, expiry, 10);
    }

    @Test
    void sellingPriceIsCostPlusMarkup() {
        // NON_FOOD надценка 20% => 120.0
        double price = p.getSellingPrice(today, policy);
        assertEquals(120.0, price, 1e-6);
    }

    @Test
    void reduceQuantityReducesValue() {
        p.reduceQuantity(3);
        assertEquals(7, p.getQuantity());
    }

    @Test
    void reducingMoreThanAvailableThrows() {
        assertThrows(IllegalArgumentException.class, () -> p.reduceQuantity(20));
    }
}
