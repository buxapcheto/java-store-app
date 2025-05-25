package bg.nbu.store.config;

import bg.nbu.store.domain.Category;
import java.time.LocalDate;

/**
 * Интефейс за политика на надценки и намаления в магазина.
 */
public interface MarkupPolicy {

    /**
     * Връща процента надценка за дадена категория.
     *
     * @param category категория на продукта
     * @return процент надценка (0.0–100.0)
     */
    double getMarkupPercent(Category category);

    /**
     * Проверява дали към датата {@code onDate} срокът на годност {@code expiryDate}
     * е по-малък от зададения праг за дадената категория.
     *
     * @param category   категория на продукта
     * @param expiryDate дата на изтичане
     * @param onDate     текуща дата, спрямо която сравняваме
     * @return {@code true}, ако е близо до изтичане
     */
    boolean isNearExpiry(Category category, LocalDate expiryDate, LocalDate onDate);

    /**
     * Връща процента намаление (0.0–100.0), който се прилага
     * при наближаващ срок за дадена категория.
     *
     * @param category категория на продукта
     * @return процент намаление
     */
    double getExpiryDiscountPercent(Category category);
}
