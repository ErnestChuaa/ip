import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for the Aether chatbot.
 * Stores todos, deadlines, and events in an {@code ArrayList} of {@code Task}
 * (polymorphism) and can mark or unmark them as done.
 * Invalid input is reported with {@link AetherException}; the chatbot prints the message and keeps running.
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
    /** Commands the user can type; shown in error messages as a hint. */
    private static final String COMMAND_HINT =
            "Try: list, todo, deadline, event, mark, unmark, or bye.";

    public static void main(String[] args) {
        String banner = "    _         _   _               \n"
                + "   / \\   ___ | |_| |__   ___ _ __ \n"
                + "  / _ \\ / _ \\| __| '_ \\ / _ \\ '__|\n"
                + " / ___ \\  __/| |_| | | |  __/ |   \n"
                + "/_/   \\_\\___|\\__|_| |_|\\___|_|   \n";

        printMessage(banner + "Hello! I'm " + NAME + ".\nWhat can I do for you?");

        // ArrayList grows as needed, so there is no fixed MAX_TASKS or separate count.
        // Todo, Deadline, and Event objects are stored together as Task (polymorphism).
        ArrayList<Task> tasks = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals(EXIT_COMMAND)) {
            try {
                if (command.trim().isEmpty()) {
                    throw new AetherException("Please type a command. " + COMMAND_HINT);
                } else if (command.trim().equals(LIST_COMMAND)) {
                    printMessage(formatTaskList(tasks));
                } else if (isCommand(command, MARK_COMMAND)) {
                    markTask(tasks, command);
                } else if (isCommand(command, UNMARK_COMMAND)) {
                    unmarkTask(tasks, command);
                } else if (isCommand(command, TODO_COMMAND)) {
                    addTodo(tasks, command);
                } else if (isCommand(command, DEADLINE_COMMAND)) {
                    addDeadline(tasks, command);
                } else if (isCommand(command, EVENT_COMMAND)) {
                    addEvent(tasks, command);
                } else {
                    throw new AetherException("I don't recognise that command. " + COMMAND_HINT);
                }
            } catch (AetherException e) {
                printMessage(e.getMessage());
            }
            command = scanner.nextLine();
        }
        printMessage("Bye. Hope to see you again soon!");
        scanner.close();
    }

    /**
     * Returns whether {@code command} is {@code commandWord}, with or without arguments.
     * {@code todo} and {@code todo borrow book} both count as the todo command.
     *
     * @param command the full user input
     * @param commandWord the expected first word, e.g. {@code todo}
     * @return {@code true} if this input uses that command
     */
    private static boolean isCommand(String command, String commandWord) {
        String trimmed = command.trim();
        return trimmed.equals(commandWord) || trimmed.startsWith(commandWord + " ");
    }

    /**
     * Returns the text after {@code commandWord}, or an empty string if there is none.
     *
     * @param command the full user input
     * @param commandWord the leading word to skip
     * @return the arguments, trimmed
     */
    private static String getArguments(String command, String commandWord) {
        String trimmed = command.trim();
        if (trimmed.equals(commandWord)) {
            return "";
        }
        return trimmed.substring(commandWord.length()).trim();
    }

    /**
     * Adds a todo from a command such as {@code todo borrow book}.
     *
     * @param tasks the stored tasks
     * @param command the full user command
     * @throws AetherException if the description is empty
     */
    private static void addTodo(ArrayList<Task> tasks, String command) throws AetherException {
        String description = getArguments(command, TODO_COMMAND);
        if (description.isEmpty()) {
            throw new AetherException("The description of a todo cannot be empty. Try: todo borrow book");
        }
        addTask(tasks, new Todo(description));
    }

    /**
     * Adds a deadline from a command such as {@code deadline return book /by Sunday}.
     * The date/time after {@code /by} is kept as a string.
     *
     * @param tasks the stored tasks
     * @param command the full user command
     * @throws AetherException if the description or {@code /by} date is missing
     */
    private static void addDeadline(ArrayList<Task> tasks, String command) throws AetherException {
        String rest = getArguments(command, DEADLINE_COMMAND);
        int byIndex = rest.indexOf("/by");
        if (byIndex < 0) {
            throw new AetherException("A deadline needs a /by date. Try: deadline return book /by Sunday");
        }
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new AetherException(
                    "The description of a deadline cannot be empty. Try: deadline return book /by Sunday");
        }
        if (by.isEmpty()) {
            throw new AetherException(
                    "The /by date of a deadline cannot be empty. Try: deadline return book /by Sunday");
        }
        addTask(tasks, new Deadline(description, by));
    }

    /**
     * Adds an event from a command such as {@code event project meeting /from Mon 2pm /to 4pm}.
     * The values after {@code /from} and {@code /to} are kept as strings.
     *
     * @param tasks the stored tasks
     * @param command the full user command
     * @throws AetherException if the description, {@code /from}, or {@code /to} is missing
     */
    private static void addEvent(ArrayList<Task> tasks, String command) throws AetherException {
        String rest = getArguments(command, EVENT_COMMAND);
        int fromIndex = rest.indexOf("/from");
        int toIndex = rest.indexOf("/to");
        if (fromIndex < 0 || toIndex < 0) {
            throw new AetherException(
                    "An event needs /from and /to times. Try: event project meeting /from Mon 2pm /to 4pm");
        }
        if (toIndex < fromIndex) {
            throw new AetherException("Put /from before /to. Try: event project meeting /from Mon 2pm /to 4pm");
        }
        String description = rest.substring(0, fromIndex).trim();
        String from = rest.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = rest.substring(toIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new AetherException("The description of an event cannot be empty. "
                    + "Try: event project meeting /from Mon 2pm /to 4pm");
        }
        if (from.isEmpty()) {
            throw new AetherException("The /from time of an event cannot be empty. "
                    + "Try: event project meeting /from Mon 2pm /to 4pm");
        }
        if (to.isEmpty()) {
            throw new AetherException("The /to time of an event cannot be empty. "
                    + "Try: event project meeting /from Mon 2pm /to 4pm");
        }
        addTask(tasks, new Event(description, from, to));
    }

    /**
     * Stores {@code task} and prints the add confirmation.
     *
     * @param tasks the stored tasks
     * @param task the task to add
     */
    private static void addTask(ArrayList<Task> tasks, Task task) {
        tasks.add(task);
        printMessage("Got it. I've added this task:\n  " + task
                + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Marks the task whose 1-based number is given after {@code mark} as done.
     *
     * @param tasks the stored tasks
     * @param command the full user command, e.g. {@code mark 2}
     * @throws AetherException if the task number is missing, not a whole number, or out of range
     */
    private static void markTask(ArrayList<Task> tasks, String command) throws AetherException {
        int index = parseTaskIndex(command, MARK_COMMAND, tasks.size());
        tasks.get(index).markAsDone();
        printMessage("Nice! I've marked this task as done:\n  " + tasks.get(index));
    }

    /**
     * Marks the task whose 1-based number is given after {@code unmark} as not done.
     *
     * @param tasks the stored tasks
     * @param command the full user command, e.g. {@code unmark 2}
     * @throws AetherException if the task number is missing, not a whole number, or out of range
     */
    private static void unmarkTask(ArrayList<Task> tasks, String command) throws AetherException {
        int index = parseTaskIndex(command, UNMARK_COMMAND, tasks.size());
        tasks.get(index).markAsNotDone();
        printMessage("OK, I've marked this task as not done yet:\n  " + tasks.get(index));
    }

    /**
     * Reads the 1-based task number from a command such as {@code mark 2}.
     *
     * @param command the full user command
     * @param commandWord the leading word to skip, e.g. {@code mark}
     * @param taskCount how many tasks are currently stored
     * @return the 0-based index
     * @throws AetherException if the number is missing, not a whole number, or out of range
     */
    private static int parseTaskIndex(String command, String commandWord, int taskCount) throws AetherException {
        String arguments = getArguments(command, commandWord);
        if (arguments.isEmpty()) {
            throw new AetherException(
                    "Please give a task number after " + commandWord + ". Try: " + commandWord + " 1");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(arguments);
        } catch (NumberFormatException e) {
            throw new AetherException("The task number must be a whole number. Try: " + commandWord + " 1");
        }
        int index = taskNumber - 1;
        if (index < 0 || index >= taskCount) {
            throw new AetherException("That task number does not exist. Use list to see the current numbers.");
        }
        return index;
    }

    /**
     * Builds a numbered list of stored tasks with their type and done status.
     *
     * @param tasks the stored tasks
     * @return the formatted list, including a short header
     */
    private static String formatTaskList(ArrayList<Task> tasks) {
        StringBuilder list = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            list.append('\n');
            list.append(i + 1).append('.').append(tasks.get(i));
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
