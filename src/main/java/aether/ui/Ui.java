package aether.ui;

import java.util.Scanner;

import aether.task.Task;

/** Handles all text displayed to the user and reads commands from the console. */
public class Ui {
    private static final String NAME = "Aether";
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER = "    _         _   _               \n"
            + "   / \\   ___ | |_| |__   ___ _ __ \n"
            + "  / _ \\ / _ \\| __| '_ \\ / _ \\ '__|\n"
            + " / ___ \\  __/| |_| | | |  __/ |   \n"
            + "/_/   \\_\\___|\\__|_| |_|\\___|_|   \n";

    private final Scanner scanner;

    /** Creates a user interface that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Shows the chatbot greeting. */
    public void showWelcome() {
        showMessage(BANNER + "Hello! I'm " + NAME + ".\nWhat can I do for you?");
    }

    /** Reads one complete command line from the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Shows an error message while keeping the chatbot ready for another command. */
    public void showError(String message) {
        showMessage(message);
    }

    /** Shows the current numbered task list. */
    public void showTaskList(String formattedTaskList) {
        showMessage(formattedTaskList);
    }

    /** Shows confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage("Got it. I've added this task:\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list.");
    }

    /** Shows confirmation that a task was marked as done. */
    public void showTaskMarked(Task task) {
        showMessage("Nice! I've marked this task as done:\n  " + task);
    }

    /** Shows confirmation that a task was marked as not done. */
    public void showTaskUnmarked(Task task) {
        showMessage("OK, I've marked this task as not done yet:\n  " + task);
    }

    /** Shows confirmation that a task was deleted. */
    public void showTaskDeleted(Task task, int taskCount) {
        showMessage("Noted. I've removed this task:\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list.");
    }

    /** Shows the farewell message. */
    public void showGoodbye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    /** Closes the input stream after the chatbot exits. */
    public void close() {
        scanner.close();
    }

    /** Prints text between the chatbot's horizontal divider lines. */
    private void showMessage(String text) {
        System.out.println(LINE);
        System.out.println(text);
        System.out.println(LINE);
    }
}
