package aether.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Owns the ordered collection of tasks and operations performed on that collection. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates a task list containing zero or more supplied tasks.
     *
     * @param tasks tasks to place in the list initially
     */
    public TaskList(Task... tasks) {
        this.tasks = new ArrayList<>(List.of(tasks));
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks tasks loaded from storage
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task at the end of the list. */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /** Removes and returns the last task, used to undo a failed save after adding a task. */
    public Task removeLastTask() {
        return tasks.remove(tasks.size() - 1);
    }

    /** Marks and returns the task at a 0-based index. */
    public Task markTask(int index) {
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /** Marks as pending and returns the task at a 0-based index. */
    public Task unmarkTask(int index) {
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /** Removes and returns the task at a 0-based index. */
    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

    /** Restores a task to a 0-based index after a failed save. */
    public void restoreTask(int index, Task task) {
        tasks.add(index, task);
    }

    /** Returns the status of the task at a 0-based index. */
    public TaskStatus getTaskStatus(int index) {
        return tasks.get(index).getStatus();
    }

    /** Restores the status of the task at a 0-based index after a failed save. */
    public void setTaskStatus(int index, TaskStatus status) {
        tasks.get(index).setStatus(status);
    }

    /** Returns the number of stored tasks. */
    public int getTaskCount() {
        return tasks.size();
    }

    /** Returns an immutable view of the tasks for storage. */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }

    /** Returns the numbered task list in the form shown to the user. */
    public String formatTaskList() {
        StringBuilder list = new StringBuilder("Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            list.append('\n').append(index + 1).append('.').append(tasks.get(index));
        }
        return list.toString();
    }

    /**
     * Returns tasks whose descriptions contain the keyword, numbered in matching order.
     * Matching ignores letter case so that, for example, {@code find BOOK} finds {@code read book}.
     *
     * @param keyword the text to search for in task descriptions
     * @return the numbered matching tasks in the form shown to the user
     */
    public String formatMatchingTasks(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        StringBuilder matches = new StringBuilder("Here are the matching tasks in your list:");
        int matchNumber = 1;
        for (Task task : tasks) {
            String normalizedDescription = task.getDescription().toLowerCase(Locale.ROOT);
            if (normalizedDescription.contains(normalizedKeyword)) {
                matches.append('\n').append(matchNumber).append('.').append(task);
                matchNumber++;
            }
        }
        return matches.toString();
    }
}
