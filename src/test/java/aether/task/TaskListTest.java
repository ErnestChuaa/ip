package aether.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class TaskListTest {
    @Test
    void taskOperationsChangeOnlyTheRequestedTaskAndPreserveOrder() {
        Todo readBook = new Todo("read book");
        Deadline returnBook = new Deadline("return book", LocalDate.of(2019, 10, 15));
        Event meeting = new Event("project meeting", LocalDate.of(2019, 10, 16), LocalDate.of(2019, 10, 17));
        TaskList tasks = new TaskList(readBook, returnBook, meeting);

        assertSame(returnBook, tasks.markTask(1));
        assertEquals(TaskStatus.COMPLETED, tasks.getTaskStatus(1));
        assertEquals(TaskStatus.PENDING, tasks.getTaskStatus(0));
        assertSame(returnBook, tasks.unmarkTask(1));
        assertEquals(TaskStatus.PENDING, tasks.getTaskStatus(1));

        assertSame(returnBook, tasks.deleteTask(1));
        assertEquals(2, tasks.getTaskCount());
        assertEquals(List.of(readBook, meeting), tasks.asList());
    }

    @Test
    void restoreTaskAndSetTaskStatusUndoFailedChangesAtTheirOriginalIndex() {
        Todo first = new Todo("first");
        Todo second = new Todo("second");
        TaskList tasks = new TaskList(first, second);

        Task deleted = tasks.deleteTask(0);
        tasks.restoreTask(0, deleted);
        tasks.setTaskStatus(1, TaskStatus.COMPLETED);

        assertEquals(List.of(first, second), tasks.asList());
        assertEquals(TaskStatus.COMPLETED, tasks.getTaskStatus(1));
        assertEquals("[T][X] second", tasks.asList().get(1).toString());
    }

    @Test
    void formatTaskListNumbersTasksInInsertionOrder() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Deadline("return book", LocalDate.of(2019, 10, 15)));

        assertEquals("Here are the tasks in your list:\n"
                + "1.[T][ ] read book\n"
                + "2.[D][ ] return book (by: Oct 15 2019)", tasks.formatTaskList());
    }

    @Test
    void formatMatchingTasksFiltersDescriptionsCaseInsensitively() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Deadline("return book", LocalDate.of(2019, 10, 15)));
        tasks.addTask(new Todo("write report"));
        tasks.markTask(1);

        assertEquals("Here are the matching tasks in your list:\n"
                + "1.[T][ ] read book\n"
                + "2.[D][X] return book (by: Oct 15 2019)", tasks.formatMatchingTasks("BOOK"));
        assertEquals("Here are the matching tasks in your list:", tasks.formatMatchingTasks("missing"));
    }

    @Test
    void asListCannotBeChangedOutsideTaskList() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read book"));

        List<Task> taskView = tasks.asList();

        assertThrows(UnsupportedOperationException.class, () -> taskView.add(new Todo("borrow book")));
        assertEquals(1, tasks.getTaskCount());
    }
}
