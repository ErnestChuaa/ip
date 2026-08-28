package aether;

import java.nio.file.Path;

import aether.exception.AetherException;
import aether.parser.Command;
import aether.parser.CommandType;
import aether.parser.Parser;
import aether.storage.Storage;
import aether.task.Task;
import aether.task.TaskList;
import aether.task.TaskStatus;
import aether.ui.Ui;

/**
 * Coordinates the chatbot's user interface, command parsing, task list, and persistent storage.
 */
public class Aether {
    /** Storage location used by the desktop application. */
    private static final Path DEFAULT_STORAGE_PATH = Path.of("data", "aether.txt");

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    /**
     * Creates an Aether chatbot that saves tasks at the supplied path.
     * If saved data cannot be loaded, the chatbot reports the problem and starts with an empty list.
     *
     * @param filePath the path used to save tasks between sessions
     */
    public Aether(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();
        tasks = loadTasks();
    }

    /** Runs the chatbot until the user enters {@code bye}. */
    public void run() {
        ui.showWelcome();
        while (true) {
            try {
                Command command = parser.parse(ui.readCommand());
                if (command.getType() == CommandType.BYE) {
                    break;
                }
                processCommand(command);
            } catch (AetherException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showGoodbye();
        ui.close();
    }

    /** Starts Aether with its standard project-relative data file. */
    public static void main(String[] args) {
        new Aether(DEFAULT_STORAGE_PATH).run();
    }

    /** Executes one validated, non-exit command. */
    private void processCommand(Command command) throws AetherException {
        switch (command.getType()) {
        case LIST:
            ui.showTaskList(tasks.formatTaskList());
            break;
        case FIND:
            ui.showTaskList(tasks.formatMatchingTasks(command.getArguments()));
            break;
        case TODO:
        case DEADLINE:
        case EVENT:
            addTask(parser.createTask(command));
            break;
        case MARK:
            markTask(command);
            break;
        case UNMARK:
            unmarkTask(command);
            break;
        case DELETE:
            deleteTask(command);
            break;
        default:
            throw new IllegalStateException("Unexpected command type: " + command.getType());
        }
    }

    /** Adds a task, saving it before showing the confirmation. */
    private void addTask(Task task) throws AetherException {
        tasks.addTask(task);
        try {
            saveTasks();
        } catch (AetherException e) {
            tasks.removeLastTask();
            throw e;
        }
        ui.showTaskAdded(task, tasks.getTaskCount());
    }

    /** Marks a task as done and restores its previous state if saving fails. */
    private void markTask(Command command) throws AetherException {
        int index = parser.parseTaskIndex(command, tasks.getTaskCount());
        TaskStatus previousStatus = tasks.getTaskStatus(index);
        Task task = tasks.markTask(index);
        try {
            saveTasks();
        } catch (AetherException e) {
            tasks.setTaskStatus(index, previousStatus);
            throw e;
        }
        ui.showTaskMarked(task);
    }

    /** Marks a task as not done and restores its previous state if saving fails. */
    private void unmarkTask(Command command) throws AetherException {
        int index = parser.parseTaskIndex(command, tasks.getTaskCount());
        TaskStatus previousStatus = tasks.getTaskStatus(index);
        Task task = tasks.unmarkTask(index);
        try {
            saveTasks();
        } catch (AetherException e) {
            tasks.setTaskStatus(index, previousStatus);
            throw e;
        }
        ui.showTaskUnmarked(task);
    }

    /** Deletes a task and restores it at its original position if saving fails. */
    private void deleteTask(Command command) throws AetherException {
        int index = parser.parseTaskIndex(command, tasks.getTaskCount());
        Task task = tasks.deleteTask(index);
        try {
            saveTasks();
        } catch (AetherException e) {
            tasks.restoreTask(index, task);
            throw e;
        }
        ui.showTaskDeleted(task, tasks.getTaskCount());
    }

    /** Loads saved tasks, reporting loading errors through the UI. */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (AetherException e) {
            ui.showError(e.getMessage());
            return new TaskList();
        }
    }

    /** Saves the current task list through the storage component. */
    private void saveTasks() throws AetherException {
        storage.save(tasks.asList());
    }
}
