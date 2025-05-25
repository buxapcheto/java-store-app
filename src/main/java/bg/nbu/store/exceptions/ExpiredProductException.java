package bg.nbu.store.exceptions;

/**
 * Изключение при опит за продажба на изтекъл продукт.
 */
public class ExpiredProductException extends RuntimeException {
    public ExpiredProductException(String id, String name) {
        super("Expired product: id=" + id + ", name=" + name);
    }
}
