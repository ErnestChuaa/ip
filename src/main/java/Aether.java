import java.util.Scanner;

/**
 * Entry point for the Aether chatbot.
 * Stores user-entered tasks in memory and lists them when requested.
 */
public class Aether {
    private static final String NAME = "Aether";
    private static final String LINE = "____________________________________________________________";
    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
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
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals(EXIT_COMMAND)) {
            if (command.equals(LIST_COMMAND)) {
                printMessage(formatTaskList(tasks, taskCount));
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
     * Builds a numbered list of stored tasks, one per line.
     *
     * @param tasks the stored task descriptions
     * @param taskCount how many tasks are currently stored
     * @return the formatted list, or an empty string if there are no tasks
     */
    private static String formatTaskList(String[] tasks, int taskCount) {
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < taskCount; i++) {
            if (i > 0) {
                list.append('\n');
            }
            list.append(i + 1).append(". ").append(tasks[i]);
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
