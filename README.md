# Obsidian Client Installer

The official installer for the free and open source Obsidian Client,
written in Java using the JavaFX library for the GUI.

## Build & Run

To build the installer, you just need to have a Java 17 JDK installed,
then you can simply run `./gradlew make`.

This will build 3 native binaries (Linux x64, macOS x64 and Windows x64),
containing the Java application and a minimal JRE.

You will find the created binaries in `build/make/`.

If you only want to run the installer,
you can also simply run `./gradlew run`.

All commands will automatically download the dependencies for you.

## Setup Development Environment

### IntelliJ Idea
IntelliJ Idea supports Gradle out of the box, you can just import/open the `build.gradle` file.

### Eclipse
Eclipse doesn't support Gradle out of the box, so you have to download the official Gradle plugin first:
<https://marketplace.eclipse.org/content/buildship-gradle-integration>.
Then you just have to open the `build.gradle` file.

## Contributing

If you want to help to improve the Obsidian Client project,
please consider improving the main project rather than just the installer.
See <https://github.com/obsidian-client/obsidian-client> for more details.