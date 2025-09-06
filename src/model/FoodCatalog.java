package model;
import java.util.HashMap;
import java.util.Map;

public class FoodCatalog {
    // ID → {ItemName, Category, Unit, ShelfDays, PerPersonLimit}
    public static Map<Integer, Object[]> catalog = new HashMap<>();

    static {
        catalog.put(1, new Object[]{"Rice", "Grain", "kg", 180, 3});        // 3kg per person
        catalog.put(2, new Object[]{"Flour", "Grain", "kg", 120, 3});       // 3kg per person
        catalog.put(3, new Object[]{"Beans", "Protein", "kg", 150, 2});     // 2kg per person
        catalog.put(4, new Object[]{"Lentils", "Protein", "kg", 150, 2});   // 2kg per person
        catalog.put(5, new Object[]{"Cooking Oil", "Essential", "liters", 356, 1}); // 1 liter per person
        catalog.put(6, new Object[]{"Milk", "Dairy", "liters", 7, 1});      // 1 liter per person
        catalog.put(7, new Object[]{"Sugar", "Essential", "kg", 365, 2});   // 2kg per person
        catalog.put(8, new Object[]{"Salt", "Essential", "kg", 730, 1});    // 1kg per person
        catalog.put(9, new Object[]{"Canned Fish", "Canned", "cans", 365, 3}); // 3 cans per person
        catalog.put(10, new Object[]{"Tea", "Beverage", "bags", 365, 1});   // 1 bag pack per person
    }

    public static Object[] getItem(int id) {
        return catalog.get(id);
    }
}
