package bg.nbu.store.domain;

import bg.nbu.store.exceptions.InsufficientQuantityException;

/**
 * Представлява наличност от дадена стока в склада.
 */
public class InventoryItem {
    private final Product product;

    public InventoryItem(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return product.getQuantity();
    }

    /**
     * Премахва количество от наличността.
     * 
     * @throws InsufficientQuantityException ако няма достатъчно
     */
    public void remove(int amount) {
        if (amount > product.getQuantity()) {
            throw new InsufficientQuantityException(
                    product.getId(),
                    amount,
                    product.getQuantity());
        }
        product.reduceQuantity(amount);
    }
}
