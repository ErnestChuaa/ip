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
}
