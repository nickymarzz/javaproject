# Contributing

Thank you for your interest in contributing to this project.

## Before You Start

- Read the `README.md` for setup and run instructions
- Check existing issues and pull requests before starting new work
- Open an issue for large features or significant behavior changes before implementing them

## Development Setup

1. Clone the repository.
2. Make sure you have a compatible Java Development Kit installed.
3. Compile the project from the repository root:

```bash
javac -sourcepath src -d bin src/main/*.java src/entity/*.java src/object/*.java src/tile/*.java src/module-info.java
```

4. Run the game:

```bash
java -cp "bin;res;lib/*" main.Main
```

## Contribution Guidelines

- Keep changes focused and scoped to a single problem when possible
- Prefer clear, readable Java code over clever implementations
- Avoid unrelated formatting-only changes
- Update documentation when behavior, setup, or project structure changes
- Add or update tests when they provide meaningful regression coverage

## Commit and Pull Request Tips

- Use descriptive commit messages
- Reference related issues when applicable
- Include a short summary of what changed and why
- Describe manual testing steps for gameplay, UI, or database-related changes
- Attach screenshots or recordings for visible UI changes when useful

## Reporting Bugs

When reporting bugs, include:

- A clear summary of the problem
- Steps to reproduce the issue
- Expected behavior
- Actual behavior
- Environment details such as Java version and operating system

## Suggesting Features

Feature requests are welcome. Please explain the problem the idea solves, the proposed behavior, and any trade-offs or limitations you already considered.

## Code of Conduct

By participating in this project, you agree to follow the `CODE_OF_CONDUCT.md`.
