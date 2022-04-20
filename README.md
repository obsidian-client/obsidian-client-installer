# Obsidian Client Installer

The official installer for the free and open source Obsidian Client,
written in Java using the JavaFX library for the GUI.

## Build & Run

To build the installer, run

`./gradlew jar`

or for production purposes you should use this command instead

`./gradlew shadowJar`.

The output Java Archive will be in `build/libs/`.

If you just want to run the installer, use

`./gradlew run`.

All commands will automatically download the dependencies for you.

## Setup Development Environment

### IntelliJ Idea
IntelliJ Idea supports Gradle natively, you can just import/open the `build.gradle` file.

### Eclipse
Eclipse doesn't support Gradle out of the box, so you have to download the official Gradle plugin first:
<https://marketplace.eclipse.org/content/buildship-gradle-integration>.
Then you just have to open the `build.gradle` file.

## Contributing

If you want to help to improve the Obsidian Client project,
please consider improving the main project rather than just the installer.
See <https://github.com/obsidian-client/obsidian-client> for more details.