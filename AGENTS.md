# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Introductory undergraduate, comfortable with basic Java and OOP
* IDE and level of expertise: Cursor on Windows, beginner-to-intermediate

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. This project is developed on Windows with JDK 25, so no extra version-switch command is needed. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.

## UI testing

After each code update that can change chatbot behaviour:

1. Update `test/ui-test-plan.md` if needed (new commands, changed wording, or extra coverage).
2. Run `python test/run_ui_tests.py` to execute the plan and report the console session.

Include tests for incorrect input (empty descriptions, unknown commands, missing `/by` `/from` `/to`, invalid task numbers). Interleave valid and invalid commands so a rejected input cannot still change the task list.

## Error handling

Invalid user input must not crash the chatbot. Throw `AetherException` (a checked exception) with a specific message that explains the problem and how to correct it. Catch `AetherException` in the main command loop, print the message, and keep waiting for the next command. Do not catch generic `Exception`, so programming bugs still surface. When adding a new command, throw `AetherException` for empty arguments and other malformed input for that command.
