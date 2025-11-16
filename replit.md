# Condor Chico - libGDX Game

## Overview
Condor Chico is a goalkeeper game built with libGDX framework where you play as Claudio Bravo (Chilean goalkeeper) catching falling soccer balls.

## Project Type
Desktop GUI game application using:
- **Language**: Java 8
- **Framework**: libGDX 1.14.0
- **Build System**: Gradle 8.14.3
- **Graphics Backend**: LWJGL3 (Lightweight Java Game Library)
- **Platform**: Desktop (Linux with X11/VNC on Replit)

## Project Structure
- `core/` - Main game logic shared across platforms
  - Game screens (Main Menu, Game, Pause, Game Over)
  - Game entities (Archer, Balls, Prizes)
  - Game system (spawning, collision detection)
- `lwjgl3/` - Desktop launcher using LWJGL3
- `assets/` - Game assets (images, sounds)
- `gradle/` - Gradle wrapper files

## Game Features
- Control goalkeeper Claudio Bravo
- Catch falling soccer balls of different types
- Collect power-ups (extra lives, bonus points)
- Increasing difficulty over time
- Score tracking with high score

## Controls
- **A / D**: Move left/right
- **SPACE**: Dash (speed boost)
- **ESC / P**: Pause game
- **Touch/Click**: Start game from menu

## Replit Setup Notes

### Key Configuration Changes Made
1. Fixed main class name in `lwjgl3/build.gradle` from `com.Juego.lwjgl3.Lwjgl3Launcher` to `io.github.some_example_name.lwjgl3.Lwjgl3Launcher`
2. Enabled Gradle daemon in `gradle.properties` for better performance
3. Changed logging level from `quiet` to `lifecycle` for better visibility
4. Installed X11 system libraries required for LWJGL3 graphics:
   - xorg.libX11
   - xorg.libXcursor
   - xorg.libXrandr
   - xorg.libXxf86vm
   - xorg.libXi
   - mesa
   - libGL

### Workflow Configuration
- **Name**: Game
- **Command**: `DISPLAY=:0 ./gradlew lwjgl3:run`
- **Output Type**: VNC (Virtual Desktop)
- The game runs in a virtual X11 display accessible through Replit's VNC viewer

## Running the Game
The game automatically runs when you start the Replit. Access it through the VNC/Desktop view in Replit.

To manually run:
```bash
DISPLAY=:0 ./gradlew lwjgl3:run
```

## Building
```bash
./gradlew build
```

## Creating JAR
```bash
./gradlew lwjgl3:jar
```
The JAR will be in `lwjgl3/build/libs/`

## Recent Changes
- 2025-11-16: Initial Replit setup from GitHub import
  - Configured main class name
  - Installed X11 dependencies
  - Set up VNC workflow
  - Verified game runs successfully
