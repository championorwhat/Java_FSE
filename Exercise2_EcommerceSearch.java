import java.util.Arrays;
import java.util.Comparator;

/*
 * EXERCISE 2: E-COMMERCE PLATFORM SEARCH FUNCTION
 * ================================================
 *
 * 1. BIG O NOTATION
 * ------------------
 * Big O notation describes how the running time (or memory use) of an
 * algorithm grows as the input size (n) grows, ignoring constant
 * factors and lower-order terms. It lets us compare algorithms
 * independent of hardware or implementation details, and predict how
 * they will behave as data scales (e.g., from 100 products to
 * 10,000,000 products).
 *
 * BEST / AVERAGE / WORST CASE for search:
 * - Best case: the target is found immediately (e.g., first element in
 *   linear search, or the exact middle element in binary search) -> O(1).
 * - Average case: the target is found after looking at roughly half the
 *   data on average (linear search) or after a typical number of
 *   halvings (binary search).
 * - Worst case: the target is not present, or is the last element
 *   checked. Linear search worst case is O(n). Binary search worst
 *   case is O(log n).
 */
public class Exercise2_EcommerceSearch {

    static class Product {
        int productId;
        String productName;
        String category;

        Product(int productId, String productName, String category) {
            this.productId = productId;
            this.productName = productName;
            this.category = category;
        }

        @Override
        public String toString() {
            return "Product{id=" + productId + ", name='" + productName + "', category='" + category + "'}";
        }
    }

    // Linear search: O(n) worst/average case, O(1) best case.
    // Works on unsorted arrays.
    public static Product linearSearchByName(Product[] products, String name) {
        for (Product p : products) {
            if (p.productName.equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    // Binary search: O(log n) worst/average case, O(1) best case.
    // Requires the array to be SORTED by the search key (here, productName).
    public static Product binarySearchByName(Product[] sortedProducts, String name) {
        int low = 0;
        int high = sortedProducts.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = sortedProducts[mid].productName.compareToIgnoreCase(name);

            if (cmp == 0) {
                return sortedProducts[mid];
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Product[] products = {
            new Product(1, "Wireless Mouse", "Electronics"),
            new Product(2, "Bluetooth Speaker", "Electronics"),
            new Product(3, "Yoga Mat", "Fitness"),
            new Product(4, "Running Shoes", "Fitness"),
            new Product(5, "Coffee Maker", "Home"),
            new Product(6, "Desk Lamp", "Home")
        };

        System.out.println("=== Linear Search (unsorted array) ===");
        Product foundLinear = linearSearchByName(products, "Yoga Mat");
        System.out.println("Found: " + foundLinear);

        // For binary search we must sort first
        Product[] sortedProducts = products.clone();
        Arrays.sort(sortedProducts, Comparator.comparing(p -> p.productName));

        System.out.println("\n=== Binary Search (sorted array) ===");
        System.out.println("Sorted order:");
        for (Product p : sortedProducts) System.out.println("  " + p);

        Product foundBinary = binarySearchByName(sortedProducts, "Yoga Mat");
        System.out.println("Found: " + foundBinary);

        Product notFound = binarySearchByName(sortedProducts, "Tablet");
        System.out.println("Search for 'Tablet': " + notFound);

        /*
         * 4. ANALYSIS
         * -----------
         * Linear search:
         *   Best case:    O(1)   - match is the first element
         *   Average case: O(n)   - on average scans about n/2 elements
         *   Worst case:   O(n)   - match is last, or absent
         *   No sorting required; works on any array.
         *
         * Binary search:
         *   Best case:    O(1)   - match is the middle element
         *   Average case: O(log n)
         *   Worst case:   O(log n)
         *   Requires the array to already be sorted by the search key.
         *   Sorting itself costs O(n log n) if not already sorted.
         *
         * WHICH IS MORE SUITABLE FOR AN E-COMMERCE PLATFORM?
         * If the catalog changes frequently (new products added/removed
         * constantly) and you search by an unindexed field, linear
         * search avoids the overhead of re-sorting. However, for a
         * large, mostly-static catalog where searches are frequent
         * (e.g., search-by-name, search-by-SKU), it is worth paying the
         * one-time O(n log n) sorting cost (or maintaining a sorted
         * index / HashMap) so that each subsequent search only costs
         * O(log n) or O(1). In practice, real e-commerce search engines
         * use inverted indexes / hash-based lookups rather than plain
         * binary search, but binary search is the right teaching
         * example for "search in sorted data."
         */
    }
}
