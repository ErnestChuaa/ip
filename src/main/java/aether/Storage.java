package aether;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Reads and writes Aether tasks in a file relative to the project directory.
 * Each field that can contain user text is Base64 encoded, so descriptions may
 * safely contain the {@code |} separator used by the file format.
 */
public class Storage {
    private static final String SEPARATOR = " | ";
    private final Path filePath;

    /**
     * Creates storage that uses the specified relative or absolute file path.
     *
     * @param filePath where tasks are saved
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all saved tasks. A missing data file represents an empty task list.
     *
     * @return the tasks stored in the data file
     * @throws AetherException if the data file cannot be read or has an invalid format
     */
    public ArrayList<Task> load() throws AetherException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            ArrayList<Task> tasks = new ArrayList<>();
            for (int index = 0; index < lines.size(); index++) {
                tasks.add(parseTask(lines.get(index), index + 1));
            }
            return tasks;
        } catch (IOException e) {
            throw new AetherException("I could not read the saved tasks. Check data/aether.txt and try again.");
        }
    }

    /**
     * Saves every task, creating the data directory when it is not present yet.
     *
     * @param tasks the current task list
     * @throws AetherException if the tasks cannot be saved
     */
    public void save(List<Task> tasks) throws AetherException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(formatTask(task));
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new AetherException("I could not save the tasks. Check that the data folder is writable.");
        }
    }

    /**
     * Recreates one task from a single saved line.
     *
     * @param line one line of the data file
     * @param lineNumber the line number, used in an error message
     * @return the reconstructed task
     * @throws AetherException if the line is malformed
     */
    private Task parseTask(String line, int lineNumber) throws AetherException {
        String[] fields = line.split("\\|", -1);
        for (int index = 0; index < fields.length; index++) {
            fields[index] = fields[index].trim();
        }

        try {
            if (fields.length < 3) {
                throw new IllegalArgumentException();
            }
            Task task;
            switch (fields[0]) {
            case "T":
                requireFieldCount(fields, 3);
                task = new Todo(decode(fields[2]));
                break;
            case "D":
                requireFieldCount(fields, 4);
                task = new Deadline(decode(fields[2]), LocalDate.parse(decode(fields[3])));
                break;
            case "E":
                requireFieldCount(fields, 5);
                task = new Event(decode(fields[2]), LocalDate.parse(decode(fields[3])),
                        LocalDate.parse(decode(fields[4])));
                break;
            default:
                throw new IllegalArgumentException();
            }
            if (fields[1].equals("1")) {
                task.markAsDone();
            } else if (!fields[1].equals("0")) {
                throw new IllegalArgumentException();
            }
            return task;
        } catch (IllegalArgumentException | DateTimeParseException e) {
            throw new AetherException("Saved task data is corrupted at line " + lineNumber
                    + ". Repair or remove data/aether.txt, then restart Aether.");
        }
    }

    /** Ensures that a saved record has exactly the expected number of fields. */
    private void requireFieldCount(String[] fields, int expectedCount) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException();
        }
    }

    /** Returns the saved representation of a task. */
    private String formatTask(Task task) {
        String done = task.status == TaskStatus.COMPLETED ? "1" : "0";
        if (task instanceof Todo) {
            return "T" + SEPARATOR + done + SEPARATOR + encode(task.description);
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D" + SEPARATOR + done + SEPARATOR + encode(task.description)
                    + SEPARATOR + encode(deadline.by.toString());
        }
        Event event = (Event) task;
        return "E" + SEPARATOR + done + SEPARATOR + encode(task.description)
                + SEPARATOR + encode(event.from.toString()) + SEPARATOR + encode(event.to.toString());
    }

    /** Encodes user-provided text so separators and line breaks cannot corrupt the file format. */
    private String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /** Decodes user-provided text from the file format. */
    private String decode(String text) {
        return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
    }
}
