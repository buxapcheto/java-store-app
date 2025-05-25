package bg.nbu.store.config;

import bg.nbu.store.domain.Category;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.Map;

/**
 * Конкретна политика за надценки и намаления.
 */
public class DefaultMarkupPolicy implements MarkupPolicy {

    private final Map<Category, Double> markupPercent = new EnumMap<>(Category.class);
    private final Map<Category, Integer> expiryThresholdDays = new EnumMap<>(Category.class);
    private final Map<Category, Double> expiryDiscountPercent = new EnumMap<>(Category.class);

    /** Ако искаш да подаваш параметризирани стойности: */
    public DefaultMarkupPolicy(double foodMarkup, double nonFoodMarkup) {
        // пример: само два параметъра
        markupPercent.put(Category.FOOD, foodMarkup);
        markupPercent.put(Category.NON_FOOD, nonFoodMarkup);

        // използваме някакви фиксирани праг и discount:
        expiryThresholdDays.put(Category.FOOD, 2);
        expiryThresholdDays.put(Category.NON_FOOD, 3);

        expiryDiscountPercent.put(Category.FOOD, 20.0);
        expiryDiscountPercent.put(Category.NON_FOOD, 10.0);
    }

    /** Без-аргументен конструктор с default стойности */
    public DefaultMarkupPolicy() {
        this(10.0, 20.0);
    }

    @Override
    public double getMarkupPercent(Category category) {
        return markupPercent.getOrDefault(category, 0.0);
    }

    @Override
    public boolean isNearExpiry(Category category, LocalDate expiryDate, LocalDate onDate) {
        long days = ChronoUnit.DAYS.between(onDate, expiryDate);
        Integer threshold = expiryThresholdDays.get(category);
        return threshold != null && days < threshold;
    }

    @Override
    public double getExpiryDiscountPercent(Category category) {
        return expiryDiscountPercent.getOrDefault(category, 0.0);
    }
}
