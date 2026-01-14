# Screen Saver

A minimalist screen saver built with Java Swing. Displays animated symbols that fade in and out on a black fullscreen background.

## Features

- Fullscreen display with undecorated window
- Randomly positioned symbols (#, @, $, *, +, %)
- Smooth fade-in/fade-out animations
- Subtle glow effect
- Variable symbol sizes (20-60px)
- 60 FPS rendering

## Requirements

- Java 23 or later

## Running

### From IntelliJ IDEA

Open the project and run `ScreenSaver.main()`

### From Command Line

```bash
# Compile
javac -d out src/ScreenSaver.java

# Run
java -cp out ScreenSaver
```

## Controls

- **ESC** - Exit
- **Mouse click** - Exit