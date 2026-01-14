# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Java Swing screen saver application that displays animated symbols with fade-in/fade-out effects on a fullscreen black background. Press ESC or click to exit.

## Build & Run

This is an IntelliJ IDEA project using JDK 23.

**From IntelliJ:**
- Run `ScreenSaver.main()` directly

**From command line:**
```bash
# Compile
javac -d out src/ScreenSaver.java

# Run
java -cp out ScreenSaver
```

## Architecture

Single-file application (`src/ScreenSaver.java`) with nested classes:

- `ScreenSaver` (JFrame) - Main window, handles keyboard/mouse exit events
- `ScreenPanel` (JPanel) - Animation canvas with two timers:
  - Symbol spawner (200ms interval) - maintains 15-30 active symbols
  - Render loop (16ms / ~60 FPS) - updates and repaints
- `Symbol` - Individual animated character with position, size, lifecycle (5-8 sec), and alpha-based fade transitions