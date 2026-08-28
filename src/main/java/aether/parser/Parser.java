package aether.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import aether.exception.AetherException;
import aether.task.Deadline;
import aether.task.Event;
import aether.task.Task;
import aether.task.Todo;

/** Converts raw user input into validated commands, tasks, and task indexes. */
public class Parser {
    /** Commands the user can type; shown in error messages as a hint. */
    private static final String COMMAND_HINT =
            "Try: list, find, todo, deadline, event, mark, unmark, delete, or bye.";

    /**
     * Identifies the command word and its arguments.
     *
     * @param userInput the complete line typed by the user
     * @return the recognised command and its arguments
     * @throws AetherException if the input is empty, unknown, or has unexpected arguments
     */
    public Command parse(String userInput) throws AetherException {
        String trimmed = userInput.trim();
        if (trimmed.isEmpty()) {
            throw new AetherException("Please type a command. " + COMMAND_HINT);
        }

        String[] parts = trimmed.split("\\s+", 2);
        String commandWord = parts[0];
        String arguments = parts.length == 2 ? parts[1].trim() : "";
        CommandType type = findCommandType(commandWord);
        if (type == CommandType.FIND && arguments.isEmpty()) {
            throw new AetherException("The search keyword cannot be empty. Try: find book");
        }
        if ((type == CommandType.LIST || type == CommandType.BYE) && !arguments.isEmpty()) {
            throw unknownCommand();
        }
        return new Command(type, arguments);
    }

    /**
     * Creates the task described by a todo, deadline, or event command.
     *
     * @param command a parsed task-creation command
     * @return the requested task
     * @throws AetherException if a required description, date marker, or date is invalid
     */
    public Task createTask(Command command) throws AetherException {
        switch (command.getType()) {
        case TODO:
            return createTodo(command.getArguments());
        case DEADLINE:
            return createDeadline(command.getArguments());
        case EVENT:
            return createEvent(command.getArguments());
        default:
            throw new IllegalArgumentException("This command does not create a task.");
        }
    }

    /**
     * Converts a 1-based task number into a 0-based list index.
     *
     * @param command a mark, unmark, or delete command
     * @param taskCount the number of tasks currently in the list
     * @return the matching 0-based index
     * @throws AetherException if the number is missing, invalid, or outside the list
     */
    public int parseTaskIndex(Command command, int taskCount) throws AetherException {
        String arguments = command.getArguments();
        String commandWord = command.getType().getCommandWord();
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

    /** Returns the enum value for one recognised command word. */
    private CommandType findCommandType(String commandWord) throws AetherException {
        for (CommandType type : CommandType.values()) {
            if (type.getCommandWord().equals(commandWord)) {
                return type;
            }
        }
        throw unknownCommand();
    }

    /** Creates a todo after checking that it has a description. */
    private Todo createTodo(String description) throws AetherException {
        if (description.isEmpty()) {
            throw new AetherException("The description of a todo cannot be empty. Try: todo borrow book");
        }
        return new Todo(description);
    }

    /** Creates a deadline after separating its description and /by date. */
    private Deadline createDeadline(String arguments) throws AetherException {
        int byIndex = arguments.indexOf("/by");
        if (byIndex < 0) {
            throw new AetherException("A deadline needs a /by date. Try: deadline return book /by 2019-10-15");
        }
        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new AetherException(
                    "The description of a deadline cannot be empty. Try: deadline return book /by 2019-10-15");
        }
        if (by.isEmpty()) {
            throw new AetherException(
                    "The /by date of a deadline cannot be empty. Try: deadline return book /by 2019-10-15");
        }
        return new Deadline(description, parseDate(by, "/by date", "deadline return book /by 2019-10-15"));
    }

    /** Creates an event after separating its description, /from date, and /to date. */
    private Event createEvent(String arguments) throws AetherException {
        int fromIndex = arguments.indexOf("/from");
        int toIndex = arguments.indexOf("/to");
        if (fromIndex < 0 || toIndex < 0) {
            throw new AetherException("An event needs /from and /to dates. Try: event project meeting /from "
                    + "2019-10-15 /to 2019-10-16");
        }
        if (toIndex < fromIndex) {
            throw new AetherException("Put /from before /to. Try: event project meeting /from "
                    + "2019-10-15 /to 2019-10-16");
        }

        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + "/from".length(), toIndex).trim();
        String to = arguments.substring(toIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new AetherException("The description of an event cannot be empty. Try: event project meeting "
                    + "/from 2019-10-15 /to 2019-10-16");
        }
        if (from.isEmpty()) {
            throw new AetherException("The /from date of an event cannot be empty. Try: event project meeting "
                    + "/from 2019-10-15 /to 2019-10-16");
        }
        if (to.isEmpty()) {
            throw new AetherException("The /to date of an event cannot be empty. Try: event project meeting "
                    + "/from 2019-10-15 /to 2019-10-16");
        }
        LocalDate startDate = parseDate(from, "/from date", "event project meeting /from 2019-10-15 /to 2019-10-16");
        LocalDate endDate = parseDate(to, "/to date", "event project meeting /from 2019-10-15 /to 2019-10-16");
        return new Event(description, startDate, endDate);
    }

    /** Parses a date in the ISO-8601 format used by Aether commands. */
    private LocalDate parseDate(String dateText, String fieldName, String example) throws AetherException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new AetherException("The " + fieldName + " must be a valid date in yyyy-MM-dd format. Try: "
                    + example);
        }
    }

    /** Creates the standard message used when no supported command matches the input. */
    private AetherException unknownCommand() {
        return new AetherException("I don't recognise that command. " + COMMAND_HINT);
    }
}
