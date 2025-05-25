package bg.nbu.store.pos;

import bg.nbu.store.config.DefaultMarkupPolicy;
import bg.nbu.store.config.MarkupPolicy;
import bg.nbu.store.domain.InventoryItem;
import bg.nbu.store.domain.Product;
import bg.nbu.store.exceptions.ExpiredProductException;
import bg.nbu.store.receipt.Receipt;
import bg.nbu.store.receipt.ReceiptLine;
import bg.nbu.store.receipt.ReceiptNumberGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CashRegister {
    private final String registerId;
    private final List<InventoryItem> inventory;
    private final MarkupPolicy policy;
    private String cashierId;
    private List<ReceiptLine> currentLines;

    public CashRegister(String registerId, List<InventoryItem> inventory) {
        this(registerId, inventory, new DefaultMarkupPolicy());
    }

    public CashRegister(String registerId, List<InventoryItem> inventory, MarkupPolicy policy) {
        this.registerId = registerId;
        this.inventory = inventory;
        this.policy = policy;
    }

    public void assignCashier(String cashierId) {
        this.cashierId = cashierId;
    }

    public void startSale() {
        currentLines = new ArrayList<>();
    }

    public void addItem(Product p, int qty) {
        LocalDate today = LocalDateTime.now().toLocalDate();

        if (today.isAfter(p.getExpiryDate())) {
            throw new ExpiredProductException(p.getId(), p.getName());
        }

        InventoryItem item = inventory.stream()
                .filter(i -> i.getProduct().getId().equals(p.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + p.getId()));

        item.remove(qty);

        BigDecimal unitPrice = BigDecimal.valueOf(p.getSellingPrice(today, policy));
        currentLines.add(new ReceiptLine(p.getId(), p.getName(), unitPrice, qty));
    }

    public Receipt finishSale(BigDecimal cashGiven) {
        // 1) изчисляваме тотала
        BigDecimal total = currentLines.stream()
                .map(line -> line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (cashGiven.compareTo(total) < 0) {
            throw new IllegalArgumentException("Insufficient cash");
        }

        // 2) генерираме Receipt обекта и обновяваме и броя, и оборота
        int receiptNumber = ReceiptNumberGenerator.next(total);
        Receipt receipt = new Receipt(
                receiptNumber,
                cashierId,
                LocalDateTime.now(),
                currentLines,
                total);

        // 3) печат в конзолата
        System.out.println("=== Receipt #" + receipt.getNumber() + " ===");
        System.out.println("Cashier: " + receipt.getCashierId());
        System.out.println("Date:    " + receipt.getDateTime());
        for (ReceiptLine line : receipt.getLines()) {
            System.out.printf("%s (%s) x%d @ %s%n",
                    line.getProductName(),
                    line.getProductId(),
                    line.getQuantity(),
                    line.getUnitPrice());
        }
        System.out.println("Total:   " + receipt.getTotal());
        System.out.println("========================");

        // 4) сериализиране в JSON и записване в файл
        ObjectMapper mapper = new ObjectMapper()
                .findAndRegisterModules()
                .enable(SerializationFeature.INDENT_OUTPUT);

        String filename = "receipt-" + receiptNumber + ".txt";
        try (FileWriter fw = new FileWriter(filename)) {
            mapper.writeValue(fw, receipt);
            System.out.println(">>> Generated " + filename);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return receipt;
    }

    /**
     * Позволява извън CashRegister-а да вземем текущите receipt-lines,
     * за да смятаме total-а динамично.
     */
    public List<ReceiptLine> getCurrentLines() {
        return Collections.unmodifiableList(currentLines);
    }
}
