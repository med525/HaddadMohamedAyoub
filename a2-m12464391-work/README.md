# Task 2

See https://www.sosy-lab.org/Teaching/2024-SS-SEP/tasks/2_beesweeper.html
for more details.

## Requirements

This project requires Java 21.
We provide a gradle wrapper to automatically download and use the correct version of Gradle.

## Usage

Run `./gradlew run --console=plain` to run the shell.
This will also build the current state of the program.


## Development

We provide different Gradle plugins to support development.
These are also used for grading the task.
You can run all of them with `./gradlew check`.

If you want to run one of the plugins individually, run:

* `./gradlew test` to run unit tests with [JUnit](https://junit.org/junit5/).
* `./gradlew checkstyleMain` to run [CheckStyle](https://checkstyle.sourceforge.io/) with the Google Java Style options.
* `./gradlew spotbugsMain` to run [SpotBugs](https://spotbugs.github.io/).
* `./gradlew spotlessCheck` to run a [SpotLess](https://github.com/diffplug/spotless/) check.

All results will be available in `build/reports/`.
