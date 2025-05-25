package bg.nbu.store.receipt;

import java.math.BigDecimal;

/**
 * Генератор на поредни номера за касови бележки и акумулатор на оборот.
 */
public class ReceiptNumberGenerator {
    private static int counter = 0;
    private static BigDecimal turnover = BigDecimal.ZERO;

    /**
     * Връща следващия пореден номер и добавя сумата към оборота.
     * 
     * @param amount – общата стойност на новата бележка
     */
    public static int next(BigDecimal amount) {
        counter++;
        turnover = turnover.add(amount);
        return counter;
    }

    /** Връща текущия брой издадени бележки. */
    public static int getCount() {
        return counter;
    }

    /** Връща общия оборот (сбор от total на всички бележки). */
    public static BigDecimal getTurnover() {
        return turnover;
    }
}
