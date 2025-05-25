package bg.nbu.store.people;

/**
 * Касиер в магазина.
 */
public class Cashier {
    private final String id;
    private final String name;
    private final double salary;

    public Cashier(String id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }
}