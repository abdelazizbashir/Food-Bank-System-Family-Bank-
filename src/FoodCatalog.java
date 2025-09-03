import java.util.HashMap;
import java.util.Map;

public class FoodCatalog {
    public static Map<Integer, Object[]> catalog = new HashMap<>();

    static {
        // ID → {ItemName, Category, Unit, ShelfDays}
        catalog.put(1, new Object[]{"Rice", "Grain", "kg", 180});
        catalog.put(2, new Object[]{"Flour", "Grain", "kg", 120});
        catalog.put(3, new Object[]{"Beans", "Protein", "kg", 150});
        catalog.put(4, new Object[]{"Lentils", "Protein", "kg", 150});
        catalog.put(5, new Object[]{"Cooking Oil", "Essential", "liters", 356});
        catalog.put(6, new Object[]{"Milk", "Dairy", "liters", 7});
        catalog.put(7, new Object[]{"Sugar", "Essential", "kg", 365});
        catalog.put(8, new Object[]{"Salt", "Essential", "kg", 730});
        catalog.put(9, new Object[]{"Canned Fish", "Canned", "cans", 365});
        catalog.put(10, new Object[]{"Tea", "Beverage", "bags", 365});
    }

    // CHANGED: removed any console display method. GUI reads from 'catalog'.
    public static Object[] getItem(int id) {
        return catalog.get(id);
    }
}
