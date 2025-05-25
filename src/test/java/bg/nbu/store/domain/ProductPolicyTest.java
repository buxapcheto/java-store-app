package bg.nbu.store.domain;

import bg.nbu.store.config.DefaultMarkupPolicy;
import bg.nbu.store.config.MarkupPolicy;
import bg.nbu.store.exceptions.ExpiredProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProductPolicyTest {

    static class TestProduct extends Product {
        TestProduct(String id, String name, double costPrice,
                Category category, LocalDate expiryDate, int quantity) {
            super(id, name, costPrice, category, expiryDate, quantity);
        }
    }

    private MarkupPolicy policy;
    private LocalDate today;
    private LocalDate expiry;

    @BeforeEach
    void setup() {
        policy = new DefaultMarkupPolicy();
        today = LocalDate.of(2025, 5, 25);
        expiry = today.plusDays(2); // границата за FOOD е 2 дни
    }

    @Test
    void nonNearExpiry_NoDiscount() {
        Product p = new TestProduct("1", "P", 100.0, Category.FOOD, expiry.plusDays(5), 5);
        double price = p.getSellingPrice(today, policy);
        // 10% markup => 110.0
        assertEquals(110.0, price, 1e-6);
    }

    @Test
    void nearExpiry_DiscountApplied() {
        // expiry - today = 2 => equal threshold -> no discount? (policy uses days <
        // threshold)
        Product p1 = new TestProduct("2", "P", 100.0, Category.FOOD, expiry, 5);
        double price1 = p1.getSellingPrice(today, policy);
        assertEquals(110.0, price1, 1e-6);

        // less than threshold => discount
        Product p2 = new TestProduct("3", "Q", 100.0, Category.FOOD, today.plusDays(1), 5);
        double price2 = p2.getSellingPrice(today, policy);
        // base 110 * (1 - 0.20) = 88
        assertEquals(88.0, price2, 1e-6);
    }

    @Test
    void nonFoodPolicy_DifferentRates() {
        Product p = new TestProduct("4", "N", 200.0, Category.NON_FOOD, expiry.plusDays(10), 1);
        // NON_FOOD markup 20% => 240
        assertEquals(240.0, p.getSellingPrice(today, policy), 1e-6);
    }

    @Test
    void expired_throwsException() {
        Product p = new TestProduct("5", "X", 50.0, Category.FOOD, today.minusDays(1), 1);
        assertThrows(ExpiredProductException.class, () -> p.getSellingPrice(today, policy));
    }
}
