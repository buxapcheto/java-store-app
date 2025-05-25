package bg.nbu.store.exceptions;

/**
 * Изключение при недостатъчно количество от стока.
 */
public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException(String id, int requested, int available) {
        super("Insufficient quantity for product id=" + id +
                ": requested=" + requested + ", available=" + available);
    }
}
