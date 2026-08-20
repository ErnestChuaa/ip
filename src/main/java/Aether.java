import java.util.Scanner;

/**
 * Entry point for the Aether chatbot.
 * Stores user-entered tasks in memory, lists them, and can mark them as done.
 */
public class Aether {
    private static final String NAME = "Aether";
    private static final String LINE = "____________________________________________________________";
    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
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
        String[] tasks = new String[MAX_TASKS];
        // Parallel array: isDone[i] is true when tasks[i] has been marked done.
        boolean[] isDone = new boolean[MAX_TASKS];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals(EXIT_COMMAND)) {
            if (command.equals(LIST_COMMAND)) {
                printMessage(formatTaskList(tasks, isDone, taskCount));
            } else if (command.startsWith(MARK_COMMAND + " ")) {
                markTask(tasks, isDone, taskCount, command);
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = command;
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
     * @param tasks the stored task descriptions
     * @param isDone whether each stored task is done
     * @param taskCount how many tasks are currently stored
     * @param command the full user command, e.g. {@code mark 2}
     */
    private static void markTask(String[] tasks, boolean[] isDone, int taskCount, String command) {
        int taskNumber = Integer.parseInt(command.substring((MARK_COMMAND + " ").length()).trim());
        int index = taskNumber - 1;
        if (index < 0 || index >= taskCount) {
            printMessage("That task number does not exist.");
            return;
        }
        isDone[index] = true;
        printMessage("Nice! I've marked this task as done:\n  "
                + formatStatus(isDone[index]) + tasks[index]);
    }

    /**
     * Builds a numbered list of stored tasks with their done status.
     *
     * @param tasks the stored task descriptions
     * @param isDone whether each stored task is done
     * @param taskCount how many tasks are currently stored
     * @return the formatted list, including a short header
     */
    private static String formatTaskList(String[] tasks, boolean[] isDone, int taskCount) {
        StringBuilder list = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            list.append('\n');
            list.append(i + 1).append('.').append(formatStatus(isDone[i])).append(tasks[i]);
        }
        return list.toString();
    }

    /**
     * Returns the checkbox shown next to a task: {@code [X]} if done, {@code [ ]} if not.
     *
     * @param isDone whether the task is done
     * @return the status marker followed by a space
     */
    private static String formatStatus(boolean isDone) {
        return isDone ? "[X] " : "[ ] ";
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
