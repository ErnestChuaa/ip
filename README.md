# Aether

Aether is a personal chatbot project, renamed from the Duke starter template. Given below are instructions on how to use it.

## Commands

Dates in `deadline` and `event` commands use the ISO-8601 `yyyy-MM-dd` format.
For example, `deadline return book /by 2019-10-15` is shown as `Oct 15 2019`.

```
todo DESCRIPTION
deadline DESCRIPTION /by yyyy-MM-dd
event DESCRIPTION /from yyyy-MM-dd /to yyyy-MM-dd
list
find KEYWORD
mark TASK_NUMBER
unmark TASK_NUMBER
delete TASK_NUMBER
bye
```

`find` searches task descriptions without regard to letter case. For example, `find BOOK` matches
both `read book` and `return book`.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/aether/Aether.java` file, right-click it, and choose `Run Aether.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
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
   Bye. Hope to see you again soon!
   ____________________________________________________________
   ```

## Compiling and running from the terminal

From the project root, compile the Java files into the `out` directory:

```powershell
$aetherSourceFiles = Get-ChildItem -Recurse src\main\java -Filter *.java |
    ForEach-Object { $_.FullName }
javac -d out $aetherSourceFiles
```

Run Aether using the compiled files in `out`:

```powershell
java -cp out aether.Aether
```

## Building and running with Gradle

The project includes the Gradle Wrapper, so no separate Gradle installation is needed.
Use these commands from the project root:

```powershell
.\gradlew.bat test
.\gradlew.bat build
.\gradlew.bat run
```

`test` runs the JUnit tests. `build` compiles the code and runs those tests. `run` starts the chatbot.
`build` also runs Checkstyle, which verifies that the Java source follows the project's coding standard.

To run the style checks directly:

```powershell
.\gradlew.bat checkstyleMain checkstyleTest
```

`checkstyleMain` checks production code and `checkstyleTest` checks test code.

## Packaging as a JAR

Create the executable JAR with:

```powershell
.\gradlew.bat shadowJar
```

The generated file is `build\libs\aether.jar`. Copy that file to an empty folder and run it from that folder:

```powershell
java -jar "aether.jar"
```

The JAR is generated output, so do not commit it. Attach `aether.jar` to a GitHub release instead.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
