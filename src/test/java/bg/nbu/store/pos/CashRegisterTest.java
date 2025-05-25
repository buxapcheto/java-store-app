package bg.nbu.store.pos;

import bg.nbu.store.config.DefaultMarkupPolicy;
import bg.nbu.store.domain.NonFoodProduct;
import bg.nbu.store.domain.InventoryItem;
import bg.nbu.store.exceptions.ExpiredProductException;
import bg.nbu.store.exceptions.InsufficientQuantityException;
import bg.nbu.store.receipt.Receipt;
import bg.nbu.store.receipt.ReceiptLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CashRegisterTest {
    private CashRegister register;
    private InventoryItem item;

    @BeforeEach
    void setUp() {
        NonFoodProduct p = new NonFoodProduct(
                "P1", "Widget", 100.0,
                LocalDate.now().plusDays(10),
                5);
        item = new InventoryItem(p);
        register = new CashRegister("R1", List.of(item), new DefaultMarkupPolicy());
        register.assignCashier("C1");
        register.startSale();
    }

    @Test
    void addItemExpiredThrows() {
        NonFoodProduct expired = new NonFoodProduct(
                "P2", "Old", 50.0,
                LocalDate.now().minusDays(1),
                3);
        InventoryItem expItem = new InventoryItem(expired);
        CashRegister reg2 = new CashRegister("R2", List.of(expItem), new DefaultMarkupPolicy());
        reg2.assignCashier("C2");
        reg2.startSale();
        assertThrows(
                ExpiredProductException.class,
                () -> reg2.addItem(expired, 1));
    }

    @Test
    void addItemReducesInventory() {
        register.addItem(item.getProduct(), 2);
        assertEquals(3, item.getQuantity());
    }

    @Test
    void reducingTooMuchThrows() {
        assertThrows(
                InsufficientQuantityException.class,
                () -> register.addItem(item.getProduct(), 10));
    }

    @Test
    void finishSaleReturnsReceiptAndChecksCash() {
        register.addItem(item.getProduct(), 2);
        BigDecimal cashGiven = BigDecimal.valueOf(300.0);
        Receipt receipt = register.finishSale(cashGiven);
        assertEquals("C1", receipt.getCashierId());
        assertEquals(1, receipt.getNumber());
        assertEquals(1, receipt.getLines().size());
        ReceiptLine line = receipt.getLines().get(0);
        assertEquals("P1", line.getProductId());
        assertEquals(2, line.getQuantity());
        assertEquals(120.0, line.getUnitPrice().doubleValue(), 1e-6);
        assertEquals(240.0, receipt.getTotal().doubleValue(), 1e-6);
    }

    @Test
    void insufficientCashThrows() {
        register.addItem(item.getProduct(), 1);
        assertThrows(
                IllegalArgumentException.class,
                () -> register.finishSale(BigDecimal.valueOf(50.0)));
    }
}
