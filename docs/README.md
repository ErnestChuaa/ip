# Aether User Guide

Aether is a desktop task chatbot for students who prefer typing concise commands. It stores todos,
deadlines, and events locally, so the task list is available when the application is opened again.

## Quick start

1. Install JDK 25.
1. Obtain `aether.jar`, or build it with `./gradlew.bat shadowJar`.
1. Run `java -jar aether.jar` from an empty folder.
1. Enter a command in the text field and press `Enter` or `Send`.

Dates use the `yyyy-MM-dd` format. For example, `deadline submit report /by 2026-09-12`.

## Commands

| Command | Format | Example |
| --- | --- | --- |
| Add a todo | `todo DESCRIPTION` | `todo buy milk` |
| Add a deadline | `deadline DESCRIPTION /by DATE` | `deadline submit report /by 2026-09-12` |
| Add an event | `event DESCRIPTION /from DATE /to DATE` | `event workshop /from 2026-09-05 /to 2026-09-06` |
| List tasks | `list` | `list` |
| Find tasks | `find KEYWORD` | `find report` |
| Sort tasks | `sort` | `sort` |
| Mark done | `mark TASK_NUMBER` | `mark 2` |
| Mark not done | `unmark TASK_NUMBER` | `unmark 2` |
| Delete a task | `delete TASK_NUMBER` | `delete 2` |
| Exit Aether | `bye` | `bye` |

## Sorting tasks

`sort` arranges deadlines by their due date and events by their start date, earliest first. Tasks
with the same date remain in their current relative order. Todos have no date, so they appear after
all dated tasks. The new order is saved automatically.

## Error handling

Aether keeps running after invalid input and explains how to correct it. For example, `deadline
submit report` reports that the `/by` date is required, and `mark abc` explains that a task number
must be a whole number. Rejected commands never change the task list.
