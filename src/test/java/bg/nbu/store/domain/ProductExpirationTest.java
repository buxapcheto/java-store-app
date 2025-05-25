package bg.nbu.store.domain;

import bg.nbu.store.config.DefaultMarkupPolicy;
import bg.nbu.store.exceptions.ExpiredProductException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ProductExpirationTest {

    // Тестов бек клас
    static class TestProduct extends Product {
        TestProduct(String id, String name, double costPrice,
                Category category, LocalDate expiryDate, int quantity) {
            super(id, name, costPrice, category, expiryDate, quantity);
        }
    }

    @Test
    void sellingExpiredProductThrows() {
        LocalDate expiry = LocalDate.of(2025, 1, 1);
        Product p = new TestProduct("1", "A", 10.0, Category.FOOD, expiry, 5);
        assertThrows(ExpiredProductException.class,
                () -> p.getSellingPrice(expiry.plusDays(1), new DefaultMarkupPolicy()));
    }
}
