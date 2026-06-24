import java.util.Arrays;

/*
 * EXERCISE 4: EMPLOYEE MANAGEMENT SYSTEM
 * =========================================
 *
 * 1. ARRAY REPRESENTATION IN MEMORY
 * ------------------------------------
 * An array stores its elements in a single contiguous block of memory.
 * Because every element has the same fixed size, the address of any
 * element can be calculated directly as:
 *     address = baseAddress + (index * elementSize)
 * This is why arrays support O(1) random access by index - the system
 * doesn't need to traverse anything, it can jump straight to the
 * element. Advantages include: fast indexed access, good cache locality
 * (elements are physically close together, which is fast for the CPU
 * cache), and a simple, predictable memory layout. The main disadvantage
 * is a fixed size - the array's capacity is set at creation time, so
 * adding more elements than that capacity requires creating a new,
 * larger array and copying everything over (O(n)).
 */
public class Exercise4_EmployeeManagement {

    static class Employee {
        int employeeId;
        String name;
        String position;
        double salary;

        Employee(int employeeId, String name, String position, double salary) {
            this.employeeId = employeeId;
            this.name = name;
            this.position = position;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "Employee{id=" + employeeId + ", name='" + name +
                   "', position='" + position + "', salary=" + salary + "}";
        }
    }

    static class EmployeeArray {
        private Employee[] employees;
        private int size; // number of currently used slots

        EmployeeArray(int capacity) {
            employees = new Employee[capacity];
            size = 0;
        }

        // O(1) amortized if there's room; O(n) if the array must grow
        public boolean add(Employee e) {
            if (size == employees.length) {
                // grow the array (classic array limitation)
                employees = Arrays.copyOf(employees, employees.length * 2);
                System.out.println("(Array resized to capacity " + employees.length + ")");
            }
            employees[size++] = e;
            System.out.println("Added: " + e);
            return true;
        }

        // O(n) worst case - must scan until found
        public Employee search(int employeeId) {
            for (int i = 0; i < size; i++) {
                if (employees[i].employeeId == employeeId) {
                    return employees[i];
                }
            }
            return null;
        }

        // O(n) - visits every element
        public void traverse() {
            System.out.println("--- Employee List ---");
            for (int i = 0; i < size; i++) {
                System.out.println(employees[i]);
            }
        }

        // O(n) worst case - find the element (O(n)), then shift subsequent
        // elements left by one to fill the gap (O(n))
        public boolean delete(int employeeId) {
            for (int i = 0; i < size; i++) {
                if (employees[i].employeeId == employeeId) {
                    for (int j = i; j < size - 1; j++) {
                        employees[j] = employees[j + 1];
                    }
                    employees[size - 1] = null;
                    size--;
                    System.out.println("Deleted employee with ID " + employeeId);
                    return true;
                }
            }
            System.out.println("Employee ID " + employeeId + " not found.");
            return false;
        }
    }

    public static void main(String[] args) {
        EmployeeArray company = new EmployeeArray(3);

        company.add(new Employee(1, "Priya Sharma", "Software Engineer", 75000));
        company.add(new Employee(2, "John Doe", "Product Manager", 90000));
        company.add(new Employee(3, "Maria Lopez", "QA Engineer", 65000));
        company.add(new Employee(4, "Wei Chen", "DevOps Engineer", 80000)); // triggers resize

        company.traverse();

        System.out.println("\nSearching for ID 3: " + company.search(3));

        company.delete(2);
        company.traverse();

        /*
         * 4. ANALYSIS
         * -----------
         * Operation complexities for an array-backed employee store:
         *   add (no resize needed)  -> O(1)
         *   add (resize needed)     -> O(n) for that single call (copy)
         *   search by ID            -> O(n) (linear scan, unsorted)
         *   traverse                -> O(n) (must visit every element)
         *   delete                  -> O(n) (find + shift elements)
         *
         * LIMITATIONS OF ARRAYS:
         * - Fixed size at creation; growing requires O(n) copy.
         * - Insertion/deletion in the middle is O(n) due to shifting.
         * - No built-in fast lookup by a non-index key (e.g., employeeId)
         *   without scanning, unless kept sorted (then binary search is
         *   possible at O(log n), but insertion/deletion are still O(n)).
         *
         * WHEN TO USE ARRAYS:
         * Arrays are a great choice when the number of elements is
         * known and fairly stable, when you need fast indexed access
         * (e.g., "give me employee #5"), and when memory efficiency and
         * cache-friendly iteration matter. For frequent inserts/deletes
         * in the middle of the collection, or when the size is highly
         * dynamic, a linked list or a HashMap (keyed by employeeId) is
         * usually a better fit - see Exercise 5 for linked lists.
         */
    }
}
