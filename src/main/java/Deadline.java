import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A task that must be done before a specific date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);
    protected LocalDate by;

    /**
     * Creates a deadline that starts as not done.
     *
     * @param description what the user needs to do
     * @param by the date when the task is due
     */
    public Deadline(String description, LocalDate by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Returns this deadline in list form, for example {@code [D][ ] return book (by: Oct 15 2019)}.
     *
     * @return the type icon, shared task text, and due date
     */
    @Override
    public String toString() {
        return "[" + type.getIcon() + "]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
