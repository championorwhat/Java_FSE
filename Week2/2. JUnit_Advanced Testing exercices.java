import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.stream.Stream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Solutions for advanced JUnit exercises.
 * JUnit 5 is used throughout.
 */
class AdvEvenChecker {
    boolean isEven(int number) {
        return number % 2 == 0;
    }
}

// Exercise 1: Parameterized Tests
class AdvEvenCheckerParameterizedTest {
    private final AdvEvenChecker checker = new AdvEvenChecker();

    @ParameterizedTest
    @ValueSource(ints = { 0, 2, 4, 6, 8, 10, -2, -4 })
    void isEven_shouldReturnTrueForEvenNumbers(int number) {
        assertTrue(checker.isEven(number), "Expected " + number + " to be even");
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 3, 5, 7, 9, -1, -3 })
    void isEven_shouldReturnFalseForOddNumbers(int number) {
        assertFalse(checker.isEven(number), "Expected " + number + " to be odd");
    }
}

// Exercise 2: Test Suites and Categories
@Suite
@SelectClasses({
    AdvEvenCheckerParameterizedTest.class,
    AdvOrderedTests.class,
    AdvExceptionThrowerTest.class,
    AdvPerformanceTesterTest.class
})
class AdvJUnitAllTests {
    // Suite marker only.
}

// Exercise 3: Test Execution Order
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdvOrderedTests {
    @Test
    @Order(1)
    void firstTest() {
        assertTrue(true);
    }

    @Test
    @Order(2)
    void secondTest() {
        assertEquals("JUnit", "JUnit");
    }

    @Test
    @Order(3)
    void thirdTest() {
        assertNotNull("done");
    }
}

// Exercise 4: Exception Testing
class AdvExceptionThrower {
    void throwException() {
        throw new IllegalStateException("Expected exception");
    }
}

class AdvExceptionThrowerTest {
    @Test
    void throwException_shouldThrowIllegalStateException() {
        AdvExceptionThrower thrower = new AdvExceptionThrower();

        IllegalStateException ex = assertThrows(IllegalStateException.class, thrower::throwException);
        assertEquals("Expected exception", ex.getMessage());
    }
}

// Exercise 5: Timeout and Performance Testing
class AdvPerformanceTester {
    void performTask() {
        // Simulate a lightweight operation.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Task was interrupted");
        }
    }
}

class AdvPerformanceTesterTest {
    @Test
    @Timeout(1) // seconds
    void performTask_shouldCompleteWithinTimeLimit() {
        new AdvPerformanceTester().performTask();
    }

    @Test
    void performTask_shouldCompleteUsingDurationAssertion() {
        assertTimeout(Duration.ofSeconds(1), () -> new AdvPerformanceTester().performTask());
    }
}
