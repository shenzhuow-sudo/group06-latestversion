# The Hunt

The Hunt is a Java Swing typing game created for CS2212 Group 06. Players type falling words to defeat enemies, earn coins, buy power-ups, track progress, and view high scores through a desktop UI.

## Features

- Main menu, login, player creation, tutorial, shop, dashboard, gameplay, game over, and high score screens
- JSON-backed local persistence for player accounts and parent control data
- Level loading from `levels.json`
- Sound effects and background music
- Dark and light theme support

## Tech Stack

- Java 24
- Swing / AWT
- Jackson for JSON serialization and parsing
- JUnit 5 for tests

## Project Structure

```text
The hunt/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java
│   │   │   ├── UI/
│   │   │   └── ca/uwo/cs2212/demo/
│   │   └── resources/
│   │       ├── assets/
│   │       └── levels.json
│   └── test/
│       └── java/
├── data/
│   ├── players.json
│   ├── parent_accounts.json
│   └── parent_links.json
├── lib/
├── out/
│   ├── main/
│   └── test/
└── README.md
```

## Entry Point

The application starts from [`src/main/java/Main.java`](src/main/java/Main.java), which opens the main menu UI.

## Requirements

- Java 24 installed and available on `PATH`
- macOS, Windows, or Linux capable of running Java Swing applications
- Run commands from the project root

The project has been compiled in this repository with:

```bash
java -version
```

Expected runtime in this workspace:

```text
java version "24.0.1"
```

## Dependencies

Third-party jars are stored in [`lib`](lib):

- `jackson-core`
- `jackson-annotations`
- `jackson-databind`
- `junit-jupiter`
- `junit-platform-*`
- `apiguardian-api`
- `opentest4j`

## Build

Compile main sources:

```bash
cd "/Users/ddh/IdeaProjects/The hunt"
javac -cp "lib/*:lib/jackson/*" -d out/main $(find src/main/java -name "*.java")
```

Compile tests:

```bash
cd "/Users/ddh/IdeaProjects/The hunt"
javac -cp "out/main:lib/*:lib/jackson/*" -d out/test $(find src/test/java -name "*.java")
```

## Run

Launch the desktop application:

```bash
cd "/Users/ddh/IdeaProjects/The hunt"
java -cp "out/main:src/main/resources:lib/*:lib/jackson/*" Main
```

## Keyboard Controls

The desktop UI supports keyboard navigation in addition to mouse input.

Main menu:

- `Up` / `Down`: move the current menu selection
- `Enter`: open the currently selected menu item
- `L`: open Login
- `T`: open Tutorial
- `H`: open High Scores
- `P`: open Parent Controls
- `Esc`: exit the application

Gameplay:

- `Enter`: submit the typed word
- `Tab`: clear / skip the current input
- `Space`: pause or resume gameplay
- `Esc`: exit the current gameplay session
- `1` / `2` / `3`: activate equipped power-ups
- Mistyped words reduce player health
- Enemies leaving the screen do not reduce player health
- Each gameplay round uses a fixed `40`-second time limit

Other screens:

- `Esc` returns to the most natural previous screen for the current context
- Examples: login screens return to main menu, shop returns to dashboard, and high scores returns to the screen that opened it

## Save And Resume

Player progress is saved locally in `data/players.json`.

- Completing a game session updates saved statistics, scores, recent activity, and unlocked level progress
- Returning from Game Over to the Dashboard shows a confirmation popup indicating that the last game's stats were saved
- `Continue Game` resumes the level currently selected on the Dashboard
- If that selected level has an in-progress checkpoint, the game resumes from that level's saved word-progress state
- `Start New Game`, `Restart Current Level`, and `Start Next Level` always begin that level from the start
- Health loss is driven by mistyped submissions rather than missed enemies

## Tests

Test sources are located in [`src/test/java`](src/test/java):

- `GameEngineTest`
- `SessionResultTest`
- `WordGeneratorTest`

This repository currently includes the test source and compile path setup, but no dedicated test runner script. The test classes can be compiled with the command above and run from your IDE, or through a JUnit console launcher if you add one.

## Data Files

Runtime JSON data is stored in [`data`](data):

- `data/players.json`: saved player profiles, progress, statistics, inventory, and selected character
- `data/parent_accounts.json`: parent / teacher login accounts
- `data/parent_links.json`: links between parent accounts and player accounts

Example shape for `players.json`:

```json
{
  "players": [
    {
      "username": "...",
      "passwordHash": "...",
      "statistics": {},
      "progress": {},
      "inventory": {},
      "selectedCharacter": {}
    }
  ]
}
```

## Resources

Static resources are stored in [`src/main/resources`](src/main/resources):

- `src/main/resources/assets/sounds`
- `src/main/resources/assets/tutorial`
- `src/main/resources/levels.json`

## IntelliJ IDEA

The project includes IDEA project files and is configured to use:

- `out/main` for main compiled classes
- `out/test` for test compiled classes

You can run the app in IntelliJ by opening the project and running `Main.main()`.

## Troubleshooting

- If the app cannot load data, check that the `data` folder and JSON files exist.
- If tutorial images or sound effects do not load, confirm `src/main/resources/assets` is present.
- If launching from command line, include `src/main/resources` on the classpath.
- If compilation fails, confirm Java 24 is installed and the `lib` jars are present.

## Team

CS2212 Group 06

- Kaiwen Li
- Robert Niu
- Yangyue Qin
- Shenzhuo Wei
- Qixuan Zhu
