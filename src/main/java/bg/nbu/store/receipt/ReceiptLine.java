package bg.nbu.store.receipt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ReceiptLine {
    private final String productId;
    private final String productName;
    private final BigDecimal unitPrice;
    private final int quantity;

    @JsonCreator
    public ReceiptLine(
            @JsonProperty("productId") String productId,
            @JsonProperty("productName") String productName,
            @JsonProperty("unitPrice") BigDecimal unitPrice,
            @JsonProperty("quantity") int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
