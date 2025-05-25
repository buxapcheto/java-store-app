package bg.nbu.store.store;

import bg.nbu.store.domain.InventoryItem;
import bg.nbu.store.people.Cashier;
import bg.nbu.store.receipt.Receipt;
import bg.nbu.store.receipt.ReceiptNumberGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Представя магазин с касиери, инвентар и издадени касови бележки.
 */
public class Store {
    private final List<Cashier> cashiers;
    private final List<InventoryItem> inventory;
    private final List<Receipt> receipts = new ArrayList<>();
    private final double deliveryCosts;

    public Store(List<Cashier> cashiers,
            List<InventoryItem> inventory,
            double deliveryCosts) {
        this.cashiers = cashiers;
        this.inventory = inventory;
        this.deliveryCosts = deliveryCosts;
    }

    /** Добавя издадена касова бележка. */
    public void addReceipt(Receipt receipt) {
        receipts.add(receipt);
    }

    /** Общо разходи за заплати на касиери. */
    public double totalSalaryCosts() {
        return cashiers.stream()
                .mapToDouble(Cashier::getSalary)
                .sum();
    }

    /** Общо разходи за доставка. */
    public double totalDeliveryCosts() {
        return deliveryCosts;
    }

    /** Общо разходи (заплати + доставка). */
    public double totalExpenses() {
        return totalSalaryCosts() + totalDeliveryCosts();
    }

    /** Общо приходи от всички касови бележки. */
    public double totalRevenue() {
        return receipts.stream()
                .map(r -> r.getTotal().doubleValue())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    /** Печалба = приходи – разходи. */
    public double profit() {
        return totalRevenue() - totalExpenses();
    }

    /** Общо издадени касови бележки (глобална бройка). */
    public int getTotalReceiptsIssued() {
        return ReceiptNumberGenerator.getCount();
    }

    /** Общ оборот от всички издадени бележки (глобален оборот). */
    public BigDecimal getTotalTurnover() {
        return ReceiptNumberGenerator.getTurnover();
    }
}
