package bg.nbu.store.receipt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Receipt {
    private final int number;
    private final String cashierId;
    private final LocalDateTime dateTime;
    private final List<ReceiptLine> lines;
    private final BigDecimal total;

    @JsonCreator
    public Receipt(
            @JsonProperty("number") int number,
            @JsonProperty("cashierId") String cashierId,
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("lines") List<ReceiptLine> lines,
            @JsonProperty("total") BigDecimal total) {
        this.number = number;
        this.cashierId = cashierId;
        this.dateTime = dateTime;
        this.lines = lines;
        this.total = total;
    }

    public int getNumber() {
        return number;
    }

    public String getCashierId() {
        return cashierId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<ReceiptLine> getLines() {
        return lines;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
