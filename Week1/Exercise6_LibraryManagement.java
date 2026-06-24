import java.util.Arrays;
import java.util.Comparator;

/*
 * EXERCISE 6: LIBRARY MANAGEMENT SYSTEM
 * ========================================
 *
 * 1. SEARCH ALGORITHMS
 * -----------------------
 * - Linear Search: checks each element one by one until a match is
 *   found or the list ends. Works on any order of data. O(n).
 * - Binary Search: repeatedly halves the search range by comparing the
 *   target to the middle element. Requires the data to be SORTED by
 *   the field being searched. O(log n).
 */
public class Exercise6_LibraryManagement {

    static class Book {
        int bookId;
        String title;
        String author;

        Book(int bookId, String title, String author) {
            this.bookId = bookId;
            this.title = title;
            this.author = author;
        }

        @Override
        public String toString() {
            return "Book{id=" + bookId + ", title='" + title + "', author='" + author + "'}";
        }
    }

    // Linear search by title - O(n)
    public static Book linearSearchByTitle(Book[] books, String title) {
        for (Book b : books) {
            if (b.title.equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }

    // Binary search by title - O(log n). Requires books[] to be sorted by title.
    public static Book binarySearchByTitle(Book[] sortedBooks, String title) {
        int low = 0;
        int high = sortedBooks.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = sortedBooks[mid].title.compareToIgnoreCase(title);

            if (cmp == 0) {
                return sortedBooks[mid];
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Book[] books = {
            new Book(1, "Clean Code", "Robert C. Martin"),
            new Book(2, "The Pragmatic Programmer", "Andrew Hunt"),
            new Book(3, "Effective Java", "Joshua Bloch"),
            new Book(4, "Introduction to Algorithms", "Thomas H. Cormen"),
            new Book(5, "Design Patterns", "Erich Gamma")
        };

        System.out.println("=== Linear Search ===");
        Book foundLinear = linearSearchByTitle(books, "Effective Java");
        System.out.println("Found: " + foundLinear);

        Book[] sortedBooks = books.clone();
        Arrays.sort(sortedBooks, Comparator.comparing(b -> b.title));

        System.out.println("\nBooks sorted by title:");
        for (Book b : sortedBooks) System.out.println("  " + b);

        System.out.println("\n=== Binary Search ===");
        Book foundBinary = binarySearchByTitle(sortedBooks, "Effective Java");
        System.out.println("Found: " + foundBinary);

        Book notFound = binarySearchByTitle(sortedBooks, "Unknown Title");
        System.out.println("Search for 'Unknown Title': " + notFound);

        /*
         * 4. ANALYSIS
         * -----------
         * Linear Search: O(n) in the average/worst case; works
         * regardless of ordering; no preprocessing cost.
         *
         * Binary Search: O(log n); requires the dataset to be sorted by
         * the search key first (sorting itself costs O(n log n) if not
         * already sorted).
         *
         * WHEN TO USE EACH:
         * - Small datasets, or data that changes very frequently
         *   (constant inserts/removals) where re-sorting before every
         *   search would cost more than the search savings: use linear
         *   search.
         * - Large, mostly static datasets where searches happen often
         *   (e.g., a library catalog browsed by many patrons but updated
         *   only occasionally): sort once, then use binary search for
         *   O(log n) lookups - a huge win as the catalog grows (e.g.,
         *   ~17 comparisons for 100,000 books vs up to 100,000 for
         *   linear search).
         * - If lookups are by a single exact key very frequently, a
         *   HashMap<String, Book> (keyed by title or ISBN) gives O(1)
         *   average lookup and may beat both approaches.
         */
    }
}
