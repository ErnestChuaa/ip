package aether;

/** Holds a command type and the arguments typed after its command word. */
public class Command {
    private final CommandType type;
    private final String arguments;

    /**
     * Creates a parsed command.
     *
     * @param type the recognised command type
     * @param arguments the remaining user input, without leading or trailing whitespace
     */
    public Command(CommandType type, String arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    /** Returns the recognised command type. */
    public CommandType getType() {
        return type;
    }

    /** Returns the user input after the command word. */
    public String getArguments() {
        return arguments;
    }
}
