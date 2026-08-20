/**
 * Shared fields and behaviour for every task type.
 * Subclasses add a type icon and any date/time details via {@link #toString()}.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task that starts as not done.
     *
     * @param description what the user needs to do
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
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
     * Returns the checkbox and description, for example {@code [ ] read book}.
     * Subclasses prepend a type icon and may append date/time details.
     *
     * @return the checkbox and description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
