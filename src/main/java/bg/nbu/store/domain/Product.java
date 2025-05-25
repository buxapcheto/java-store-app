package bg.nbu.store.domain;

import bg.nbu.store.config.MarkupPolicy;
import bg.nbu.store.exceptions.ExpiredProductException;
import java.time.LocalDate;

/**
 * Абстрактен клас за стока.
 */
public abstract class Product {
    private final String id;
    private final String name;
    private final double costPrice;
    private final Category category;
    private final LocalDate expiryDate;
    private int quantity;

    protected Product(String id, String name, double costPrice,
            Category category, LocalDate expiryDate, int quantity) {
        this.id = id;
        this.name = name;
        this.costPrice = costPrice;
        this.category = category;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }

    // Гетъри
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Изчислява продажна цена според политиката.
     *
     * @throws ExpiredProductException ако е изтекъл срока
     */
    public double getSellingPrice(LocalDate onDate, MarkupPolicy policy) {
        if (onDate.isAfter(expiryDate)) {
            throw new ExpiredProductException(id, name);
        }
        double markupPct = policy.getMarkupPercent(category);
        double basePrice = costPrice * (1 + markupPct / 100.0);

        if (policy.isNearExpiry(category, expiryDate, onDate)) {
            double discountPct = policy.getExpiryDiscountPercent(category);
            basePrice = basePrice * (1 - discountPct / 100.0);
        }
        return basePrice;
    }

    /**
     * Намалява наличността при продажба.
     *
     * @throws IllegalArgumentException ако количеството е недостатъчно
     */
    public void reduceQuantity(int amount) {
        if (amount > quantity) {
            throw new IllegalArgumentException("Недостатъчно количество: " + amount);
        }
        quantity -= amount;
    }
}
