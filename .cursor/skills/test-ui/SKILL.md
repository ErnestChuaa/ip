---
name: test-ui
description: >-
  Runs Aether console UI tests from test/ui-test-plan.md by sending commands
  and checking each response against the expected output. Use after each code
  update to chatbot commands or printed output, when implementing a Duke/Aether
  increment, or when the user asks to test the UI.
---

# test-ui

Run the Aether chatbot against the cases in `test/ui-test-plan.md`.

The skill accepts lists of commands and expected outputs. For each command, it should run the program and check the output against the expected output.

## When to run

After each code update that can change chatbot behaviour:

1. Update `test/ui-test-plan.md` if needed (new commands, changed wording, extra coverage).
2. Follow this skill to run the plan.

## Test plan format

Cases live in `test/ui-test-plan.md`. Each test case must specify:

- **Aim** of the test case
- **Inputs** (commands, one per line)
- **Expected output** (full console transcript, including the greeting)

## How to run

From the repository root:

```
python .cursor/skills/test-ui/scripts/run_ui_tests.py
```

The script:

1. Compiles every `.java` file under `src/main/java` into `out/` using JDK 25.
2. For each test case, starts `Aether`, sends the input commands, and checks the output after each command (and after the greeting) against the expected transcript.
3. After testing, prints a record of the console input and output for the session.
4. If a test case failed, terminates the test session immediately, and reports the actual and expected outputs.

Do not continue to later test cases after a failure. Fix the code or the plan, then run this skill again.

## After a run

- If every case passed, report that and include the console session record.
- If a case failed, stop. Show the failing case name, the command (or greeting) that mismatched, and the actual vs expected output. Do not mark the increment complete until the tests pass.
