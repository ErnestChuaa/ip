/**
 * A task that must be done before a given date or time, kept as a string.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates a deadline that starts as not done.
     *
     * @param description what the user needs to do
     * @param by when the task is due
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline in list form, for example {@code [D][ ] return book (by: Sunday)}.
     *
     * @return the type icon, shared task text, and due date
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
