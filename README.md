# Sejong Student Simulator

Sejong Student Simulator is a Java desktop game built with Swing. The project combines simple 2D exploration, item collection, NPC interaction, and a short quiz mechanic, while also persisting game session and resource data to a MySQL database.

## Overview

This project demonstrates:

- A Java Swing-based game loop and rendering pipeline
- Tile-based world navigation and collision handling
- Interactive objects and NPC dialogue
- Quiz-based progression with pass/fail outcomes
- MySQL-backed tracking for sessions, collected resources, and quiz results

## Features

- Title screen with `NEW GAME` and `EXIT` options
- Real-time player movement in a 2D game world
- Collectible items such as coffee, cheat sheets, and pencils
- Inventory display during gameplay
- Dialogue interactions with NPCs
- Quiz flow with tracked score and final result screen
- Database persistence for:
  - overall application sessions
  - game-specific sessions
  - collection events
  - resource totals
  - quiz results

## Technology Stack

- Java
- Java Swing / AWT
- JDBC
- MySQL
- Maven for build and dependency management

## Project Structure

```text
javaproject/
|-- src/
|   `-- main/
|       |-- java/com/sejong/simulator/  # Game source code
|       `-- resources/                  # Images, maps, and database config resources
|-- lib/               # Fallback external libraries for manual builds
|-- pom.xml            # Maven build configuration
|-- DB.txt             # Database schema/setup script
`-- run.bat            # Convenience script to compile and run
```

## Prerequisites

Before running the project, make sure you have:

- A Java Development Kit installed
- A local MySQL server available
- Permission to create databases and tables in MySQL

The repository already includes the MySQL JDBC driver in `lib/mysql-connector-java-8.0.28.jar`.

## Setup

### 1. Clone The Repository

```bash
git clone <your-repository-url>
cd javaproject
```

### 2. Create The Database

Run the SQL statements in `DB.txt` against your local MySQL server.

The script creates the `game_resources` database and related tables used by the application.

### 3. Configure Database Access

Copy the template configuration file and rename it to `db.properties`:

```bash
cp src/main/resources/config/db.properties.example src/main/resources/config/db.properties
```

Then, open `src/main/resources/config/db.properties` and update it with your local MySQL credentials:

```properties
db.url=jdbc:mysql://localhost:3306/game_resources?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user=root
db.password=your_password_here
```

Replace `your_password_here` with the actual password for your MySQL user.
*(Note: The `db.properties` file is intentionally ignored by Git to prevent accidentally pushing your password.)*

## Build And Run

### Using Maven (Recommended)

From the project root, run:

```bash
mvn clean package
java -jar target/sejong-student-simulator-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Using Windows Shortcut (No Maven Required)

Simply double-click or run:

```bat
run.bat
```

## Controls

### Menu Navigation

- `W` / `S`: move the menu selection
- `Enter`: confirm the selected option

### Gameplay

- `W`: move up
- `A`: move left
- `S`: move down
- `D`: move right
- `Enter`: interact / advance dialogue
- `P`: pause or resume the game

### Quiz

- `W` / `S`: change the selected answer
- `Enter`: submit the selected answer

## Database Notes

The application uses two levels of session tracking:

- `game_sessions`: overall application session records
- `games`: game-specific session records

It also stores:

- resource collection events in `collection_events`
- running resource totals in `game_resources`
- quiz marks and pass/fail state in database tables

If the database is not configured correctly, the game may still launch, but database-backed features will fail when session or resource operations are triggered.

## Development Notes

- Main entry point: `src/main/java/com/sejong/simulator/main/Main.java`
- Keyboard handling: `src/main/java/com/sejong/simulator/main/KeyHandler.java`
- Game state and loop: `src/main/java/com/sejong/simulator/main/Panel.java`
- UI rendering: `src/main/java/com/sejong/simulator/main/UI.java`
- Database connection management: `src/main/java/com/sejong/simulator/main/DatabaseManager.java`
- Database operations: `src/main/java/com/sejong/simulator/main/GameDataClient.java`

## Contributing

Contributions are welcome. Please review the following repository documents before opening a pull request:

- `CONTRIBUTING.md`
- `CODE_OF_CONDUCT.md`
- `SECURITY.md`

## License

This project is licensed under the MIT License. See `LICENSE` for details.
