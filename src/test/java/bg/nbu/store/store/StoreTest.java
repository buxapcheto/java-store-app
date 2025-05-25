package bg.nbu.store.store;

import bg.nbu.store.domain.InventoryItem;
import bg.nbu.store.domain.NonFoodProduct;
import bg.nbu.store.people.Cashier;
import bg.nbu.store.receipt.Receipt;
import bg.nbu.store.receipt.ReceiptLine;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    @Test
    void financialCalculationsAreCorrect() {
        Cashier c1 = new Cashier("C1", "Ivan", 1000.0);
        Cashier c2 = new Cashier("C2", "Maria", 1200.0);
        NonFoodProduct p = new NonFoodProduct("P", "Prod", 50.0,
                LocalDate.now().plusDays(5), 10);
        InventoryItem item = new InventoryItem(p);

        Store store = new Store(List.of(c1, c2), List.of(item), 500.0);

        // Първа бележка: 2 бр. по 60 = 120
        ReceiptLine line1 = new ReceiptLine("P", "Prod", BigDecimal.valueOf(60.0), 2);
        Receipt r1 = new Receipt(1, "C1", LocalDateTime.now(), List.of(line1), BigDecimal.valueOf(120));

        // Втора бележка: 3 бр. по 60 = 180
        ReceiptLine line2 = new ReceiptLine("P", "Prod", BigDecimal.valueOf(60.0), 3);
        Receipt r2 = new Receipt(2, "C2", LocalDateTime.now(), List.of(line2), BigDecimal.valueOf(180));

        store.addReceipt(r1);
        store.addReceipt(r2);

        assertEquals(2200.0, store.totalSalaryCosts(), 1e-6);
        assertEquals(500.0, store.totalDeliveryCosts(), 1e-6);
        assertEquals(2700.0, store.totalExpenses(), 1e-6);
        assertEquals(300.0, store.totalRevenue(), 1e-6);
        assertEquals(-2400.0, store.profit(), 1e-6);
    }
}
