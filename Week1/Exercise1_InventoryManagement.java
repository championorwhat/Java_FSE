import java.util.HashMap;
import java.util.Map;

/*
 * EXERCISE 1: INVENTORY MANAGEMENT SYSTEM
 * ========================================
 *
 * 1. WHY DATA STRUCTURES & ALGORITHMS MATTER HERE
 * ------------------------------------------------
 * A warehouse inventory can contain thousands to millions of distinct
 * products. Operations like adding new stock, updating quantity/price,
 * removing discontinued products, and looking up a product by its ID
 * happen constantly and must be fast. If we stored products in a plain
 * list and searched linearly every time, lookups would take O(n) time,
 * which becomes painfully slow as the catalog grows. Choosing the right
 * data structure lets us do these operations in close to constant time,
 * which keeps the system responsive no matter how large the inventory
 * gets. Algorithms also matter for tasks like sorting products by price
 * or quantity, or searching/filtering by category.
 *
 * 2. SUITABLE DATA STRUCTURES
 * ----------------------------
 * - ArrayList: good for simple sequential storage and iteration, but
 *   lookup/update/delete by productId is O(n) unless the list is sorted
 *   and binary search is used.
 * - HashMap<Integer, Product>: ideal when productId is the primary key.
 *   Average-case O(1) add, update, delete, and lookup by ID.
 * - TreeMap: useful if you need products kept in sorted order by key
 *   (O(log n) operations) at the cost of being slightly slower than
 *   HashMap.
 * We use a HashMap keyed by productId below, since ID-based lookup is
 * the most common operation in an inventory system.
 */
public class Exercise1_InventoryManagement {

    // ---- Product class ----
    static class Product {
        private int productId;
        private String productName;
        private int quantity;
        private double price;

        public Product(int productId, String productName, int quantity, double price) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        public int getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }

        public void setProductName(String productName) { this.productName = productName; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public void setPrice(double price) { this.price = price; }

        @Override
        public String toString() {
            return "Product{id=" + productId + ", name='" + productName +
                   "', qty=" + quantity + ", price=" + price + "}";
        }
    }

    // ---- Inventory using a HashMap<Integer, Product> ----
    static class Inventory {
        private Map<Integer, Product> products = new HashMap<>();

        // O(1) average case
        public void addProduct(Product p) {
            if (products.containsKey(p.getProductId())) {
                System.out.println("Product ID " + p.getProductId() + " already exists. Use update instead.");
                return;
            }
            products.put(p.getProductId(), p);
            System.out.println("Added: " + p);
        }

        // O(1) average case
        public boolean updateProduct(int productId, String newName, Integer newQty, Double newPrice) {
            Product p = products.get(productId);
            if (p == null) {
                System.out.println("Update failed. Product ID " + productId + " not found.");
                return false;
            }
            if (newName != null) p.setProductName(newName);
            if (newQty != null) p.setQuantity(newQty);
            if (newPrice != null) p.setPrice(newPrice);
            System.out.println("Updated: " + p);
            return true;
        }

        // O(1) average case
        public boolean deleteProduct(int productId) {
            Product removed = products.remove(productId);
            if (removed == null) {
                System.out.println("Delete failed. Product ID " + productId + " not found.");
                return false;
            }
            System.out.println("Deleted: " + removed);
            return true;
        }

        // O(1) average case
        public Product getProduct(int productId) {
            return products.get(productId);
        }

        public void printAll() {
            System.out.println("--- Current Inventory ---");
            for (Product p : products.values()) {
                System.out.println(p);
            }
        }
    }

    public static void main(String[] args) {
        Inventory inventory = new Inventory();

        inventory.addProduct(new Product(101, "USB Cable", 200, 3.50));
        inventory.addProduct(new Product(102, "Wireless Mouse", 75, 12.99));
        inventory.addProduct(new Product(103, "Keyboard", 50, 19.99));

        inventory.printAll();

        inventory.updateProduct(102, null, 60, 11.99); // restock/price change
        inventory.deleteProduct(103);

        inventory.printAll();

        /*
         * 4. ANALYSIS
         * -----------
         * Operation complexities with HashMap<Integer, Product>:
         *   addProduct(p)     -> O(1) average, O(n) worst case (hash collisions/resize)
         *   updateProduct(id) -> O(1) average (one get + mutation)
         *   deleteProduct(id) -> O(1) average (one remove)
         *   getProduct(id)    -> O(1) average
         *
         * Compare with an ArrayList<Product> where you'd need to scan
         * the list to find a product by ID:
         *   add    -> O(1) (append at end)
         *   update -> O(n) (must search first)
         *   delete -> O(n) (must search, then shift elements)
         *
         * OPTIMIZATION IDEAS:
         * - Use HashMap for O(1) ID-based access (done here).
         * - If you frequently need products sorted by price or name,
         *   maintain a separate TreeMap or sorted index, or sort only
         *   when needed (lazy sorting) rather than keeping the whole
         *   collection sorted on every insert.
         * - For range queries (e.g., low-stock alerts), a min-heap
         *   (PriorityQueue) on quantity can give O(log n) updates and
         *   O(1) peek at the lowest-stock item.
         * - Use a secondary HashMap<String, List<Integer>> to index by
         *   category if you need fast category-based search.
         */
    }
}
