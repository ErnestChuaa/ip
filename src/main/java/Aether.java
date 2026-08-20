import java.util.Scanner;

/**
 * Entry point for the Aether chatbot.
 * Stores todos, deadlines, and events in a {@code Task[]} (polymorphism) and can mark or unmark them as done.
 */
public class Aether {
    private static final String NAME = "Aether";
    private static final String LINE = "____________________________________________________________";
    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    /** Maximum number of tasks that can be stored in memory. */
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        String banner = "    _         _   _               \n"
                + "   / \\   ___ | |_| |__   ___ _ __ \n"
                + "  / _ \\ / _ \\| __| '_ \\ / _ \\ '__|\n"
                + " / ___ \\  __/| |_| | | |  __/ |   \n"
                + "/_/   \\_\\___|\\__|_| |_|\\___|_|   \n";

        printMessage(banner + "Hello! I'm " + NAME + ".\nWhat can I do for you?");

        // Tasks stay in memory only; they are not written to disk.
        // Todo, Deadline, and Event objects are stored together as Task (polymorphism).
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals(EXIT_COMMAND)) {
            if (command.equals(LIST_COMMAND)) {
                printMessage(formatTaskList(tasks, taskCount));
            } else if (command.startsWith(MARK_COMMAND + " ")) {
                markTask(tasks, taskCount, command);
            } else if (command.startsWith(UNMARK_COMMAND + " ")) {
                unmarkTask(tasks, taskCount, command);
            } else if (command.startsWith(TODO_COMMAND + " ")) {
                taskCount = addTodo(tasks, taskCount, command);
            } else if (command.startsWith(DEADLINE_COMMAND + " ")) {
                taskCount = addDeadline(tasks, taskCount, command);
            } else if (command.startsWith(EVENT_COMMAND + " ")) {
                taskCount = addEvent(tasks, taskCount, command);
            } else {
                printMessage("I don't understand that command.");
            }
            command = scanner.nextLine();
        }
        printMessage("Bye. Hope to see you again soon!");
        scanner.close();
    }

    /**
     * Adds a todo from a command such as {@code todo borrow book}.
     *
     * @param tasks the stored tasks
     * @param taskCount how many tasks are currently stored
     * @param command the full user command
     * @return the updated task count
     */
    private static int addTodo(Task[] tasks, int taskCount, String command) {
        String description = command.substring((TODO_COMMAND + " ").length()).trim();
        return addTask(tasks, taskCount, new Todo(description));
    }

    /**
     * Adds a deadline from a command such as {@code deadline return book /by Sunday}.
     * The date/time after {@code /by} is kept as a string.
     *
     * @param tasks the stored tasks
     * @param taskCount how many tasks are currently stored
     * @param command the full user command
     * @return the updated task count
     */
    private static int addDeadline(Task[] tasks, int taskCount, String command) {
        String rest = command.substring((DEADLINE_COMMAND + " ").length());
        int byIndex = rest.indexOf("/by");
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + "/by".length()).trim();
        return addTask(tasks, taskCount, new Deadline(description, by));
    }

    /**
     * Adds an event from a command such as {@code event project meeting /from Mon 2pm /to 4pm}.
     * The values after {@code /from} and {@code /to} are kept as strings.
     *
     * @param tasks the stored tasks
     * @param taskCount how many tasks are currently stored
     * @param command the full user command
     * @return the updated task count
     */
    private static int addEvent(Task[] tasks, int taskCount, String command) {
        String rest = command.substring((EVENT_COMMAND + " ").length());
        int fromIndex = rest.indexOf("/from");
        int toIndex = rest.indexOf("/to");
        String description = rest.substring(0, fromIndex).trim();
        String from = rest.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = rest.substring(toIndex + "/to".length()).trim();
        return addTask(tasks, taskCount, new Event(description, from, to));
    }

    /**
     * Stores {@code task} if there is room, and prints the add confirmation.
     *
     * @param tasks the stored tasks
     * @param taskCount how many tasks are currently stored
     * @param task the task to add
     * @return the updated task count
     */
    private static int addTask(Task[] tasks, int taskCount, Task task) {
        if (taskCount >= MAX_TASKS) {
            printMessage("Cannot add more tasks. The list is full.");
            return taskCount;
        }
        tasks[taskCount] = task;
        taskCount++;
        printMessage("Got it. I've added this task:\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list.");
        return taskCount;
    }

    /**
     * Marks the task whose 1-based number is given after {@code mark} as done.
     *
     * @param tasks the stored tasks
     * @param taskCount how many tasks are currently stored
     * @param command the full user command, e.g. {@code mark 2}
     */
    private static void markTask(Task[] tasks, int taskCount, String command) {
        int index = parseTaskIndex(command, MARK_COMMAND, taskCount);
        if (index < 0) {
            printMessage("That task number does not exist.");
            return;
        }
        tasks[index].markAsDone();
        printMessage("Nice! I've marked this task as done:\n  " + tasks[index]);
    }

    /**
     * Marks the task whose 1-based number is given after {@code unmark} as not done.
     *
     * @param tasks the stored tasks
     * @param taskCount how many tasks are currently stored
     * @param command the full user command, e.g. {@code unmark 2}
     */
    private static void unmarkTask(Task[] tasks, int taskCount, String command) {
        int index = parseTaskIndex(command, UNMARK_COMMAND, taskCount);
        if (index < 0) {
            printMessage("That task number does not exist.");
            return;
        }
        tasks[index].markAsNotDone();
        printMessage("OK, I've marked this task as not done yet:\n  " + tasks[index]);
    }

    /**
     * Reads the 1-based task number from a command such as {@code mark 2}.
     *
     * @param command the full user command
     * @param commandWord the leading word to skip, e.g. {@code mark}
     * @param taskCount how many tasks are currently stored
     * @return the 0-based index, or {@code -1} if the number is out of range
     */
    private static int parseTaskIndex(String command, String commandWord, int taskCount) {
        int taskNumber = Integer.parseInt(command.substring((commandWord + " ").length()).trim());
        int index = taskNumber - 1;
        if (index < 0 || index >= taskCount) {
            return -1;
        }
        return index;
    }

    /**
     * Builds a numbered list of stored tasks with their type and done status.
     *
     * @param tasks the stored tasks
     * @param taskCount how many tasks are currently stored
     * @return the formatted list, including a short header
     */
    private static String formatTaskList(Task[] tasks, int taskCount) {
        StringBuilder list = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            list.append('\n');
            list.append(i + 1).append('.').append(tasks[i]);
        }
        return list.toString();
    }

    /**
     * Prints {@code text} wrapped in the chatbot's horizontal divider lines.
     *
     * @param text the message to show to the user
     */
    private static void printMessage(String text) {
        System.out.println(LINE);
        System.out.println(text);
        System.out.println(LINE);
    }
}
