/**
 * A single tracked item with a description, done/not-done status, and type.
 * Type is stored as a letter ({@code T}, {@code D}, or {@code E}) rather than
 * subclasses so three task kinds can share one class.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    /** {@code T} for todo, {@code D} for deadline, {@code E} for event. */
    protected String typeIcon;
    /** Due date/time for deadlines; unused for other types. */
    protected String by;
    /** Start date/time for events; unused for other types. */
    protected String from;
    /** End date/time for events; unused for other types. */
    protected String to;

    /**
     * Creates a todo (no date/time) that starts as not done.
     *
     * @param description what the user needs to do
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.typeIcon = "T";
    }

    /**
     * Creates a deadline that starts as not done.
     *
     * @param description what the user needs to do
     * @param by when the task is due, kept as a string
     */
    public Task(String description, String by) {
        this(description);
        this.typeIcon = "D";
        this.by = by;
    }

    /**
     * Creates an event that starts as not done.
     *
     * @param description what the event is
     * @param from when the event starts, kept as a string
     * @param to when the event ends, kept as a string
     */
    public Task(String description, String from, String to) {
        this(description);
        this.typeIcon = "E";
        this.from = from;
        this.to = to;
    }

    /**
     * Returns {@code X} if this task is done, or a space if it is not.
     *
     * @return the character shown inside the task's checkbox
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Records this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Records this task as not yet completed.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task in list form, for example {@code [T][ ] borrow book}.
     *
     * @return the type, checkbox, description, and any date/time details
     */
    @Override
    public String toString() {
        String text = "[" + typeIcon + "][" + getStatusIcon() + "] " + description;
        if ("D".equals(typeIcon)) {
            text += " (by: " + by + ")";
        } else if ("E".equals(typeIcon)) {
            text += " (from: " + from + " to: " + to + ")";
        }
        return text;
    }
}
