import java.util.Arrays;

/*
 * EXERCISE 3: SORTING CUSTOMER ORDERS
 * =====================================
 *
 * 1. SORTING ALGORITHMS OVERVIEW
 * --------------------------------
 * - Bubble Sort: repeatedly steps through the list, compares adjacent
 *   elements, and swaps them if out of order. Simple but slow:
 *   O(n^2) time in the average/worst case.
 * - Insertion Sort: builds the sorted array one element at a time by
 *   inserting each new element into its correct position among the
 *   already-sorted elements. O(n^2) worst case, but fast (O(n)) on
 *   nearly-sorted data and efficient for small datasets.
 * - Quick Sort: a divide-and-conquer algorithm that picks a pivot,
 *   partitions the array into elements less-than and greater-than the
 *   pivot, then recursively sorts each partition. O(n log n) average
 *   case, O(n^2) worst case (rare, with good pivot choice).
 * - Merge Sort: divide-and-conquer; splits the array in half
 *   recursively, sorts each half, then merges them. Always O(n log n),
 *   and stable, but uses O(n) extra memory.
 */
public class Exercise3_SortingOrders {

    static class Order {
        int orderId;
        String customerName;
        double totalPrice;

        Order(int orderId, String customerName, double totalPrice) {
            this.orderId = orderId;
            this.customerName = customerName;
            this.totalPrice = totalPrice;
        }

        @Override
        public String toString() {
            return "Order{id=" + orderId + ", customer='" + customerName + "', total=" + totalPrice + "}";
        }
    }

    // ---- Bubble Sort: O(n^2) ----
    public static void bubbleSort(Order[] orders) {
        int n = orders.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (orders[j].totalPrice > orders[j + 1].totalPrice) {
                    Order temp = orders[j];
                    orders[j] = orders[j + 1];
                    orders[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break; // already sorted, stop early
        }
    }

    // ---- Quick Sort: O(n log n) average ----
    public static void quickSort(Order[] orders, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(orders, low, high);
            quickSort(orders, low, pivotIndex - 1);
            quickSort(orders, pivotIndex + 1, high);
        }
    }

    private static int partition(Order[] orders, int low, int high) {
        double pivot = orders[high].totalPrice;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (orders[j].totalPrice <= pivot) {
                i++;
                Order temp = orders[i];
                orders[i] = orders[j];
                orders[j] = temp;
            }
        }
        Order temp = orders[i + 1];
        orders[i + 1] = orders[high];
        orders[high] = temp;
        return i + 1;
    }

    public static void main(String[] args) {
        Order[] ordersForBubble = {
            new Order(1, "Alice", 250.75),
            new Order(2, "Bob", 49.99),
            new Order(3, "Charlie", 999.00),
            new Order(4, "Dana", 15.25),
            new Order(5, "Eve", 530.10)
        };

        Order[] ordersForQuick = ordersForBubble.clone();

        System.out.println("=== Before Sorting ===");
        for (Order o : ordersForBubble) System.out.println(o);

        bubbleSort(ordersForBubble);
        System.out.println("\n=== After Bubble Sort (by totalPrice) ===");
        for (Order o : ordersForBubble) System.out.println(o);

        quickSort(ordersForQuick, 0, ordersForQuick.length - 1);
        System.out.println("\n=== After Quick Sort (by totalPrice) ===");
        for (Order o : ordersForQuick) System.out.println(o);

        /*
         * 4. ANALYSIS
         * -----------
         * Bubble Sort:
         *   Best case:    O(n)   - already sorted, one pass with no swaps
         *   Average case: O(n^2)
         *   Worst case:   O(n^2)
         *   Space:        O(1) extra (in-place)
         *
         * Quick Sort:
         *   Best/avg case: O(n log n)
         *   Worst case:    O(n^2)  - happens with poor pivot choices
         *                    (e.g., already-sorted data with last-element
         *                    pivot); mitigated by random/median pivots.
         *   Space:         O(log n) for recursion stack (in-place partitioning)
         *
         * WHY QUICK SORT IS GENERALLY PREFERRED:
         * For large datasets (e.g., thousands of customer orders), the
         * difference between O(n^2) and O(n log n) is enormous. Quick
         * Sort's average-case performance scales much better and it
         * sorts in place with low memory overhead, making it the
         * practical choice for production systems. Bubble Sort is only
         * reasonable for teaching purposes or very small/nearly-sorted
         * datasets where its simplicity outweighs its inefficiency.
         */
    }
}
