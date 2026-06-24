import java.util.HashMap;
import java.util.Map;

/*
 * EXERCISE 7: FINANCIAL FORECASTING
 * ====================================
 *
 * 1. RECURSION
 * --------------
 * Recursion is when a method calls itself to solve a smaller instance
 * of the same problem, until it reaches a "base case" simple enough to
 * answer directly. It can simplify problems that have a naturally
 * repetitive/self-similar structure - like compounding growth over
 * time, where the value at year n depends only on the value at year
 * n-1. Instead of writing a loop that manually tracks state, recursion
 * expresses the relationship directly: futureValue(n) = futureValue(n-1) * (1 + rate).
 */
public class Exercise7_FinancialForecasting {

    // Naive recursive future value calculation.
    // futureValue(presentValue, rate, n) = presentValue * (1 + rate)^n
    // Base case: n == 0 -> return presentValue unchanged.
    public static double futureValueRecursive(double presentValue, double growthRate, int years) {
        if (years == 0) {
            return presentValue; // base case
        }
        return futureValueRecursive(presentValue, growthRate, years - 1) * (1 + growthRate);
    }

    // Memoized version to avoid recomputation when the same (rate, years)
    // pair is requested multiple times (e.g., building a year-by-year
    // forecast table for several scenarios).
    public static double futureValueMemoized(double presentValue, double growthRate, int years,
                                              Map<Integer, Double> cache) {
        if (years == 0) {
            return presentValue;
        }
        if (cache.containsKey(years)) {
            return cache.get(years);
        }
        double result = futureValueMemoized(presentValue, growthRate, years - 1, cache) * (1 + growthRate);
        cache.put(years, result);
        return result;
    }

    public static void main(String[] args) {
        double presentValue = 10000.0; // starting investment/revenue
        double growthRate = 0.07;      // 7% annual growth

        System.out.println("=== Plain Recursive Forecast ===");
        for (int year = 0; year <= 10; year++) {
            double value = futureValueRecursive(presentValue, growthRate, year);
            System.out.printf("Year %2d: %.2f%n", year, value);
        }

        System.out.println("\n=== Memoized Forecast (avoids recomputation) ===");
        Map<Integer, Double> cache = new HashMap<>();
        for (int year = 0; year <= 10; year++) {
            double value = futureValueMemoized(presentValue, growthRate, year, cache);
            System.out.printf("Year %2d: %.2f%n", year, value);
        }

        /*
         * 4. ANALYSIS
         * -----------
         * Plain recursive futureValueRecursive(n):
         *   Each call makes exactly one recursive call (years -> years-1),
         *   so the total work is O(n) time and O(n) space (call stack
         *   depth), where n = number of years. It is NOT exponential
         *   here because the recursion is linear/single-branch - but if
         *   you naively recompute futureValueRecursive(..., year) for
         *   EVERY year from 0..10 inside a loop (as done above), you
         *   redundantly redo all the smaller sub-calculations each time:
         *   computing year 10 redoes years 0-9, computing year 9 redoes
         *   years 0-8, etc. Across a full table build of n years, that
         *   totals O(n^2) work.
         *
         * OPTIMIZATION - MEMOIZATION:
         * The futureValueMemoized version stores previously computed
         * results in a HashMap (cache). When asked for year n, it
         * reuses the cached values for years < n instead of
         * recalculating them, so building the full 0..n forecast table
         * only costs O(n) total instead of O(n^2).
         *
         * Other ways to avoid excessive recursion/computation:
         * - Convert to an iterative loop (bottom-up dynamic programming),
         *   which removes recursive call-stack overhead entirely and is
         *   the standard production approach for this kind of
         *   calculation: value *= (1 + rate) repeated n times.
         * - Use the closed-form formula directly:
         *   futureValue = presentValue * Math.pow(1 + rate, years);
         *   This is O(log n) (or treated as O(1) for fixed-size doubles)
         *   since Math.pow uses fast exponentiation internally, and
         *   avoids recursion altogether.
         */
    }
}
