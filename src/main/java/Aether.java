import java.util.Scanner;

/**
 * Entry point for the Aether chatbot.
 * Stores user-entered tasks in memory, lists them, and can mark or unmark them as done.
 */
public class Aether {
    private static final String NAME = "Aether";
    private static final String LINE = "____________________________________________________________";
    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
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
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = new Task(command);
                taskCount++;
                printMessage("added: " + command);
            } else {
                printMessage("Cannot add more tasks. The list is full.");
            }
            command = scanner.nextLine();
        }
        printMessage("Bye. Hope to see you again soon!");
        scanner.close();
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
     * Builds a numbered list of stored tasks with their done status.
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
