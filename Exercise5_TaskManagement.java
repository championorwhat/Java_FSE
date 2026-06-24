/*
 * EXERCISE 5: TASK MANAGEMENT SYSTEM
 * =====================================
 *
 * 1. LINKED LISTS
 * -----------------
 * A linked list stores elements as separate "nodes" scattered anywhere
 * in memory, where each node holds its data plus a reference (pointer)
 * to the next node (and, for doubly linked lists, also a reference to
 * the previous node).
 *
 * - Singly Linked List (SLL): each node points only to the next node.
 *   Traversal is one-directional (head -> tail). Simple and memory
 *   efficient (one pointer per node).
 * - Doubly Linked List (DLL): each node points to both the next and
 *   previous nodes. This allows traversal in both directions and O(1)
 *   removal of a node if you already have a reference to it (no need
 *   to find its predecessor), at the cost of extra memory per node for
 *   the second pointer.
 *
 * Below we implement a Singly Linked List to manage Tasks.
 */
public class Exercise5_TaskManagement {

    static class Task {
        int taskId;
        String taskName;
        String status;

        Task(int taskId, String taskName, String status) {
            this.taskId = taskId;
            this.taskName = taskName;
            this.status = status;
        }

        @Override
        public String toString() {
            return "Task{id=" + taskId + ", name='" + taskName + "', status='" + status + "'}";
        }
    }

    // Node for the singly linked list
    static class Node {
        Task task;
        Node next;

        Node(Task task) {
            this.task = task;
            this.next = null;
        }
    }

    static class TaskLinkedList {
        private Node head;
        private int size = 0;

        // O(1) - add at the head (could also add at tail with a tail
        // pointer for O(1) append, or O(n) without one)
        public void add(Task task) {
            Node newNode = new Node(task);
            newNode.next = head;
            head = newNode;
            size++;
            System.out.println("Added: " + task);
        }

        // O(n) - must walk the list from head until found
        public Task search(int taskId) {
            Node current = head;
            while (current != null) {
                if (current.task.taskId == taskId) {
                    return current.task;
                }
                current = current.next;
            }
            return null;
        }

        // O(n) - visits every node
        public void traverse() {
            System.out.println("--- Task List ---");
            Node current = head;
            while (current != null) {
                System.out.println(current.task);
                current = current.next;
            }
        }

        // O(n) - must find the node (and track its predecessor) to unlink it
        public boolean delete(int taskId) {
            Node current = head;
            Node previous = null;

            while (current != null) {
                if (current.task.taskId == taskId) {
                    if (previous == null) {
                        head = current.next; // deleting the head
                    } else {
                        previous.next = current.next;
                    }
                    size--;
                    System.out.println("Deleted task with ID " + taskId);
                    return true;
                }
                previous = current;
                current = current.next;
            }
            System.out.println("Task ID " + taskId + " not found.");
            return false;
        }

        public int size() {
            return size;
        }
    }

    public static void main(String[] args) {
        TaskLinkedList tasks = new TaskLinkedList();

        tasks.add(new Task(1, "Design database schema", "In Progress"));
        tasks.add(new Task(2, "Write unit tests", "Pending"));
        tasks.add(new Task(3, "Deploy to staging", "Pending"));

        tasks.traverse();

        System.out.println("\nSearching for ID 2: " + tasks.search(2));

        tasks.delete(1);
        tasks.traverse();

        System.out.println("\nTotal tasks remaining: " + tasks.size());

        /*
         * 4. ANALYSIS
         * -----------
         * Operation complexities for a singly linked list:
         *   add (at head)     -> O(1)
         *   search by taskId  -> O(n) (must walk from head)
         *   traverse          -> O(n)
         *   delete by taskId  -> O(n) (find node + unlink)
         *
         * ADVANTAGES OF LINKED LISTS OVER ARRAYS FOR DYNAMIC DATA:
         * - No fixed capacity: a linked list grows and shrinks
         *   naturally, one node at a time, with O(1) insertion at the
         *   head (or tail, with a tail pointer) and no need to ever
         *   copy/resize an entire backing array.
         * - Insertion/removal at a known position (e.g., right after a
         *   given node) is O(1), versus O(n) for an array because
         *   arrays require shifting subsequent elements.
         * - Memory is allocated node-by-node as needed, which can be
         *   useful when the maximum size is unknown or highly variable.
         *
         * The trade-off is that linked lists lose O(1) random access
         * (no "give me the 5th task" without walking the list) and have
         * extra memory overhead per element (the pointer(s)) plus worse
         * cache locality than arrays.
         */
    }
}
