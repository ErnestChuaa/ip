package aether.exception;

/**
 * A user-facing error, such as an unknown command or a missing task description.
 * The chatbot catches this, prints the message, and keeps running.
 */
public class AetherException extends Exception {
    /**
     * Creates an error with a message that can be shown to the user.
     *
     * @param message what went wrong and how to correct it
     */
    public AetherException(String message) {
        super(message);
    }
}
