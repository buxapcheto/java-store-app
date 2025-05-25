package bg.nbu.store.domain;

import java.time.LocalDate;

/**
 * Хранителна стока.
 */
public class FoodProduct extends Product {

    public FoodProduct(String id, String name, double costPrice,
            LocalDate expiryDate, int quantity) {
        super(id, name, costPrice, Category.FOOD, expiryDate, quantity);
    }

    // вече няма метод markupPercentage()
}
