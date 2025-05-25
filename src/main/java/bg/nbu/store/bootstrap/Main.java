package bg.nbu.store.bootstrap;

import bg.nbu.store.config.DefaultMarkupPolicy;
import bg.nbu.store.config.MarkupPolicy;
import bg.nbu.store.domain.FoodProduct;
import bg.nbu.store.domain.InventoryItem;
import bg.nbu.store.domain.NonFoodProduct;
import bg.nbu.store.people.Cashier;
import bg.nbu.store.pos.CashRegister;
import bg.nbu.store.receipt.Receipt;
import bg.nbu.store.receipt.ReceiptLine;
import bg.nbu.store.receipt.ReceiptNumberGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Cashier cashier = new Cashier("C1", "Ivan", 0.0);

        // 3 продукта в инвентара
        NonFoodProduct p1 = new NonFoodProduct("P1", "Widget", 100.0, LocalDate.now().plusDays(10), 50);
        FoodProduct p2 = new FoodProduct("P2", "Apple", 0.50, LocalDate.now().plusDays(5), 100);
        FoodProduct p3 = new FoodProduct("P3", "Milk", 1.00, LocalDate.now().plusDays(7), 30);

        List<InventoryItem> inventory = List.of(
                new InventoryItem(p1),
                new InventoryItem(p2),
                new InventoryItem(p3));

        // настройка политика и каса
        MarkupPolicy policy = new DefaultMarkupPolicy();
        CashRegister register = new CashRegister("REG1", inventory, policy);
        register.assignCashier(cashier.getId());

        // Първа бележка: 1xP1,2xP2,1xP3
        register.startSale();
        register.addItem(p1, 1);
        register.addItem(p2, 2);
        register.addItem(p3, 1);
        BigDecimal total1 = calculateTotal(register);
        Receipt r1 = register.finishSale(total1);
        System.out.println(r1);

        // Втора бележка: 2xP1,1xP2,1xP3
        register.startSale();
        register.addItem(p1, 2);
        register.addItem(p2, 1);
        register.addItem(p3, 1);
        BigDecimal total2 = calculateTotal(register);
        Receipt r2 = register.finishSale(total2);
        System.out.println(r2);

        // Трета бележка: 3xP2,2xP3
        register.startSale();
        register.addItem(p2, 3);
        register.addItem(p3, 2);
        BigDecimal total3 = calculateTotal(register);
        Receipt r3 = register.finishSale(total3);
        System.out.println(r3);

        // ** Глобални метрики **
        System.out.println();
        System.out.printf("Издадени бележки: %d%n", ReceiptNumberGenerator.getCount());
        System.out.printf("Общ оборот:       %.2f%n", ReceiptNumberGenerator.getTurnover());
    }

    private static BigDecimal calculateTotal(CashRegister reg) {
        return reg.getCurrentLines().stream()
                .map(line -> line.getUnitPrice()
                        .multiply(BigDecimal.valueOf(line.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
