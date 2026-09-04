package aether;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AetherTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponseProcessesCommandsAndKeepsWorkingAfterInvalidInput() {
        Aether aether = new Aether(temporaryDirectory.resolve("aether.txt"));

        assertTrue(aether.getWelcomeMessage().contains("Hello! I'm Aether."));
        assertTrue(aether.getResponse("todo read book").contains("[T][ ] read book"));
        assertTrue(aether.getResponse("todo").contains("description of a todo cannot be empty"));
        assertEquals("Here are the tasks in your list:\n1.[T][ ] read book", aether.getResponse("list"));
        assertEquals("Bye. Hope to see you again soon!", aether.getResponse("bye"));
    }

    @Test
    void failedLoadPreventsChangesFromOverwritingSavedData() throws IOException {
        Path dataFile = temporaryDirectory.resolve("aether.txt");
        String originalData = "T | 0 | cmVhZCBib29r\ncorrupted record\n";
        Files.writeString(dataFile, originalData, StandardCharsets.UTF_8);
        Aether aether = new Aether(dataFile);

        assertTrue(aether.getResponse("todo new task").contains(
                "cannot save changes because the saved tasks could not be loaded"));
        assertEquals("Here are the tasks in your list:", aether.getResponse("list"));
        assertEquals(originalData, Files.readString(dataFile, StandardCharsets.UTF_8));
    }
    @Test
    void sortOrdersTasksByDateAndSavesTheNewOrder() {
        Path dataFile = temporaryDirectory.resolve("aether.txt");
        Aether aether = new Aether(dataFile);
        aether.getResponse("todo buy milk");
        aether.getResponse("deadline submit report /by 2026-09-12");
        aether.getResponse("event workshop /from 2026-09-05 /to 2026-09-06");
        aether.getResponse("deadline renew pass /by 2026-09-05");

        assertEquals("I've sorted the tasks by date.\n"
                + "Here are the tasks in your list:\n"
                + "1.[E][ ] workshop (from: Sep 05 2026 to: Sep 06 2026)\n"
                + "2.[D][ ] renew pass (by: Sep 05 2026)\n"
                + "3.[D][ ] submit report (by: Sep 12 2026)\n"
                + "4.[T][ ] buy milk", aether.getResponse("sort"));

        Aether reloadedAether = new Aether(dataFile);
        assertEquals("Here are the tasks in your list:\n"
                + "1.[E][ ] workshop (from: Sep 05 2026 to: Sep 06 2026)\n"
                + "2.[D][ ] renew pass (by: Sep 05 2026)\n"
                + "3.[D][ ] submit report (by: Sep 12 2026)\n"
                + "4.[T][ ] buy milk", reloadedAether.getResponse("list"));
    }
}
