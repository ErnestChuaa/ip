# UI Test Plan

Console UI tests for Aether. After each code update that changes chatbot behaviour, update this file if needed and run the `test-ui` skill.

Dates and times are treated as plain strings.

## Test case: add a todo

**Aim:** A `todo` command stores a task with type `T`, no date/time, and not-done status.

**Inputs:**
```
todo borrow book
list
bye
```

**Expected output:**
```
____________________________________________________________
    _         _   _
   / \   ___ | |_| |__   ___ _ __
  / _ \ / _ \| __| '_ \ / _ \ '__|
 / ___ \  __/| |_| | | |  __/ |
/_/   \_\___|\__|_| |_|\___|_|
Hello! I'm Aether.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: add a deadline

**Aim:** A `deadline` command stores a task with type `D` and the `/by` value shown as a string.

**Inputs:**
```
deadline return book /by Sunday
list
bye
```

**Expected output:**
```
____________________________________________________________
    _         _   _
   / \   ___ | |_| |__   ___ _ __
  / _ \ / _ \| __| '_ \ / _ \ '__|
 / ___ \  __/| |_| | | |  __/ |
/_/   \_\___|\__|_| |_|\___|_|
Hello! I'm Aether.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: add an event

**Aim:** An `event` command stores a task with type `E` and the `/from` and `/to` values shown as strings.

**Inputs:**
```
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output:**
```
____________________________________________________________
    _         _   _
   / \   ___ | |_| |__   ___ _ __
  / _ \ / _ \| __| '_ \ / _ \ '__|
 / ___ \  __/| |_| | | |  __/ |
/_/   \_\___|\__|_| |_|\___|_|
Hello! I'm Aether.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: mixed list with mark

**Aim:** Todos, deadlines, and events share one list; `list` shows type icons and dates; `mark` keeps those details.

**Inputs:**
```
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
mark 1
mark 4
list
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
deadline do homework /by no idea :-p
list
bye
```

**Expected output:**
```
____________________________________________________________
    _         _   _
   / \   ___ | |_| |__   ___ _ __
  / _ \ / _ \| __| '_ \ / _ \ '__|
 / ___ \  __/| |_| | | |  __/ |
/_/   \_\___|\__|_| |_|\___|_|
Hello! I'm Aether.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] join sports club
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] join sports club
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
4.[T][X] join sports club
5.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 6 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 7 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] do homework (by: no idea :-p)
Now you have 8 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
4.[T][X] join sports club
5.[T][ ] borrow book
6.[D][ ] return book (by: Sunday)
7.[E][ ] project meeting (from: Mon 2pm to: 4pm)
8.[D][ ] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: mark and unmark keep type details

**Aim:** `mark` and `unmark` change only the done checkbox; type icon and date text stay the same.

**Inputs:**
```
deadline return book /by Sunday
mark 1
unmark 1
list
bye
```

**Expected output:**
```
____________________________________________________________
    _         _   _
   / \   ___ | |_| |__   ___ _ __
  / _ \ / _ \| __| '_ \ / _ \ '__|
 / ___ \  __/| |_| | | |  __/ |
/_/   \_\___|\__|_| |_|\___|_|
Hello! I'm Aether.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: empty todo and unknown command

**Aim:** A `todo` with no description, and a command that is not recognised, each print an error and do not change the list.

**Inputs:**
```
todo
blah
list
bye
```

**Expected output:**
```
____________________________________________________________
    _         _   _
   / \   ___ | |_| |__   ___ _ __
  / _ \ / _ \| __| '_ \ / _ \ '__|
 / ___ \  __/| |_| | | |  __/ |
/_/   \_\___|\__|_| |_|\___|_|
Hello! I'm Aether.
What can I do for you?
____________________________________________________________
____________________________________________________________
The description of a todo cannot be empty. Try: todo borrow book
____________________________________________________________
____________________________________________________________
I don't recognise that command. Try: list, todo, deadline, event, mark, unmark, or bye.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: interleaved valid and invalid commands

**Aim:** Rejected commands leave the list unchanged, so later `list` and `mark` still match the tasks that were actually added.

**Inputs:**
```
todo read book
todo
deadline return book
event project meeting /from Mon 2pm
mark abc
list
deadline return book /by Sunday
mark 2
mark 1
list
bye
```

**Expected output:**
```
____________________________________________________________
    _         _   _
   / \   ___ | |_| |__   ___ _ __
  / _ \ / _ \| __| '_ \ / _ \ '__|
 / ___ \  __/| |_| | | |  __/ |
/_/   \_\___|\__|_| |_|\___|_|
Hello! I'm Aether.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
The description of a todo cannot be empty. Try: todo borrow book
____________________________________________________________
____________________________________________________________
A deadline needs a /by date. Try: deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
An event needs /from and /to times. Try: event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
The task number must be a whole number. Try: mark 1
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: deadline and event field errors

**Aim:** Empty descriptions and empty `/by`, `/from`, or `/to` values are rejected without adding a task.

**Inputs:**
```
deadline /by Sunday
deadline return book /by
event /from Mon 2pm /to 4pm
event project meeting /from /to 4pm
event project meeting /from Mon 2pm /to
mark
unmark 1
list
bye
```

**Expected output:**
```
____________________________________________________________
    _         _   _
   / \   ___ | |_| |__   ___ _ __
  / _ \ / _ \| __| '_ \ / _ \ '__|
 / ___ \  __/| |_| | | |  __/ |
/_/   \_\___|\__|_| |_|\___|_|
Hello! I'm Aether.
What can I do for you?
____________________________________________________________
____________________________________________________________
The description of a deadline cannot be empty. Try: deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
The /by date of a deadline cannot be empty. Try: deadline return book /by Sunday
____________________________________________________________
____________________________________________________________
The description of an event cannot be empty. Try: event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
The /from time of an event cannot be empty. Try: event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
The /to time of an event cannot be empty. Try: event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
____________________________________________________________
Please give a task number after mark. Try: mark 1
____________________________________________________________
____________________________________________________________
That task number does not exist. Use list to see the current numbers.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
