import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Solutions for:
 * 1) Setting up JUnit
 * 2) Basic JUnit tests
 * 3) Assertions in JUnit
 * 4) AAA pattern + setup/teardown
 *
 * Note: This file uses JUnit 5 APIs so the solutions can live in a single
 * source file without requiring public top-level classes.
 */
class JUnitBasicCalculator {
    int add(int a, int b) {
        return a + b;
    }

    int subtract(int a, int b) {
        return a - b;
    }
}

class JUnitBasicCalculatorTest {
    private JUnitBasicCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new JUnitBasicCalculator();
    }

    @AfterEach
    void tearDown() {
        calculator = null;
    }

    // Exercise 2 + 4: simple test + AAA pattern
    @Test
    void add_shouldReturnSum() {
        // Arrange
        int a = 2;
        int b = 3;

        // Act
        int result = calculator.add(a, b);

        // Assert
        assertEquals(5, result);
    }

    @Test
    void subtract_shouldReturnDifference() {
        // Arrange
        int a = 10;
        int b = 4;

        // Act
        int result = calculator.subtract(a, b);

        // Assert
        assertEquals(6, result);
    }
}

class JUnitBasicAssertionsTest {
    @Test
    void shouldDemonstrateCommonAssertions() {
        // Assert equals
        assertEquals(5, 2 + 3);

        // Assert true
        assertTrue(5 > 3);

        // Assert false
        assertFalse(5 < 3);

        // Assert null
        assertNull(null);

        // Assert not null
        assertNotNull(new Object());
    }
}
