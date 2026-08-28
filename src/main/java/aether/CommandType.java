package aether;

/** Represents the commands that Aether understands. */
public enum CommandType {
    /** Shows every stored task. */
    LIST("list"),
    /** Marks a task as completed. */
    MARK("mark"),
    /** Marks a task as pending. */
    UNMARK("unmark"),
    /** Removes a task. */
    DELETE("delete"),
    /** Adds a todo. */
    TODO("todo"),
    /** Adds a deadline. */
    DEADLINE("deadline"),
    /** Adds an event. */
    EVENT("event"),
    /** Ends the chatbot session. */
    BYE("bye");

    private final String commandWord;

    CommandType(String commandWord) {
        this.commandWord = commandWord;
    }

    /** Returns the word the user types to invoke this command. */
    public String getCommandWord() {
        return commandWord;
    }
}
