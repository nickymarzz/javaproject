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
- Eclipse project configuration

## Project Structure

```text
javaproject/
|-- src/
|   |-- main/          # Game loop, UI, input, DB integration, entry point
|   |-- entity/        # Player and NPC entities
|   |-- object/        # Collectible objects
|   |-- tile/          # Tile and map rendering
|   `-- module-info.java
|-- res/               # Images, maps, and database config resources
|-- lib/               # External libraries, including MySQL JDBC driver
|-- bin/               # Compiled classes and copied runtime resources
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

Update `res/config/db.properties` with your local MySQL credentials:

```properties
db.url=jdbc:mysql://localhost:3306/game_resources?useSSL=false&serverTimezone=UTC
db.user=root
db.password=your_password_here
```

Replace `your_password_here` with the actual password for your MySQL user.

## Build And Run

### Compile

From the project root, run:

```bash
javac -sourcepath src -d bin src/main/*.java src/entity/*.java src/object/*.java src/tile/*.java src/module-info.java
```

### Run

```bash
java -cp "bin;res;lib/*" main.Main
```

### Windows Shortcut

You can also use:

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

- Main entry point: `src/main/Main.java`
- Keyboard handling: `src/main/KeyHandler.java`
- Game state and loop: `src/main/Panel.java`
- UI rendering: `src/main/UI.java`
- Database connection management: `src/main/DatabaseManager.java`
- Database operations: `src/main/GameDataClient.java`

## Contributing

Contributions are welcome. Please review the following repository documents before opening a pull request:

- `CONTRIBUTING.md`
- `CODE_OF_CONDUCT.md`
- `SECURITY.md`

## License

This project is licensed under the MIT License. See `LICENSE` for details.
