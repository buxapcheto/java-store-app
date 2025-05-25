package bg.nbu.store.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void categoryEnumContainsBothValues() {
        Category[] values = Category.values();
        assertEquals(2, values.length, "Трябва да има точно 2 категории");
        assertTrue(values[0] == Category.FOOD);
        assertTrue(values[1] == Category.NON_FOOD);
    }
}
