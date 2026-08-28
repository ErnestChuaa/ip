package aether.task;

/**
 * Shared fields and behavior for every task type.
 * Subclasses add a type icon and any date/time details via {@link #toString()}.
 */
public class Task {
    protected String description;
    protected TaskStatus status;
    protected TaskType type;

    /**
     * Creates a task that starts as not done.
     *
     * @param description what the user needs to do
     */
    public Task(String description) {
        this(description, TaskType.TODO);
    }

    /**
     * Creates a task of the specified type that starts as not done.
     *
     * @param description what the user needs to do
     * @param type the kind of task
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.status = TaskStatus.PENDING;
        this.type = type;
    }

    /**
     * Returns {@code X} if this task is done, or a space if it is not.
     *
     * @return the character shown inside the task's checkbox
     */
    public String getStatusIcon() {
        return (status == TaskStatus.COMPLETED ? "X" : " ");
    }

    /**
     * Records this task as completed.
     */
    public void markAsDone() {
        status = TaskStatus.COMPLETED;
    }

    /**
     * Records this task as not yet completed.
     */
    public void markAsNotDone() {
        status = TaskStatus.PENDING;
    }

    /** Returns whether this task is pending or completed. */
    public TaskStatus getStatus() {
        return status;
    }

    /** Returns the user-provided description of this task. */
    public String getDescription() {
        return description;
    }

    /** Restores this task's status, for example after an unsuccessful save. */
    public void setStatus(TaskStatus status) {
        this.status = status;
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
