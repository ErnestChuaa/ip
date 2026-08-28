import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A task that starts on one date and ends on another date.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);
    protected LocalDate from;
    protected LocalDate to;

    /**
     * Creates an event that starts as not done.
     *
     * @param description what the event is
     * @param from the date when the event starts
     * @param to the date when the event ends
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in list form, for example
     * {@code [E][ ] project meeting (from: Oct 15 2019 to: Oct 16 2019)}.
     *
     * @return the type icon, shared task text, and start/end dates
     */
    @Override
    public String toString() {
        return "[" + type.getIcon() + "]" + super.toString()
                + " (from: " + from.format(DISPLAY_FORMAT) + " to: " + to.format(DISPLAY_FORMAT) + ")";
    }
}
