package bg.nbu.store.domain;

import java.time.LocalDate;

/**
 * Нехранителна стока.
 */
public class NonFoodProduct extends Product {

    public NonFoodProduct(String id, String name, double costPrice,
            LocalDate expiryDate, int quantity) {
        super(id, name, costPrice, Category.NON_FOOD, expiryDate, quantity);
    }

    // вече няма метод markupPercentage()
}
