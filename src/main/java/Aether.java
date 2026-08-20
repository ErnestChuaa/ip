/**
 * Entry point for the Aether chatbot.
 * Prints a greeting and a farewell, then exits.
 */
public class Aether {
    private static final String NAME = "Aether";
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
        String banner = "    _         _   _               \n"
                + "   / \\   ___ | |_| |__   ___ _ __ \n"
                + "  / _ \\ / _ \\| __| '_ \\ / _ \\ '__|\n"
                + " / ___ \\  __/| |_| | | |  __/ |   \n"
                + "/_/   \\_\\___|\\__|_| |_|\\___|_|   \n";

        System.out.println(LINE);
        System.out.print(banner);
        System.out.println("Hello! I'm " + NAME + ".");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
