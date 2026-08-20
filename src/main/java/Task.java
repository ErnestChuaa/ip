/**
 * A single to-do item with a text description and a done/not-done status.
 * Keeping both pieces of data in one object avoids parallel arrays in {@link Aether}.
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
     * Returns this task in list form, for example {@code [X] read book}.
     *
     * @return the checkbox and description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
