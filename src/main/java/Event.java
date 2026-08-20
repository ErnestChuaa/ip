/**
 * A task that starts at one date/time and ends at another, both kept as strings.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event that starts as not done.
     *
     * @param description what the event is
     * @param from when the event starts
     * @param to when the event ends
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in list form, for example
     * {@code [E][ ] project meeting (from: Mon 2pm to: 4pm)}.
     *
     * @return the type icon, shared task text, and start/end times
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
