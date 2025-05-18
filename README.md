# Tank Wars Game

A 2-player tank combat game with multiple game modes, power-ups, and split-screen capabilities.

## Game Features

- 3 unique game modes:
  - Level 1: Open Arena - Classic tank combat
  - Level 2: Maze - Navigate through destructible walls
  - Level 3: Split Screen - Large map with split-screen view
- Power-up system:
  - Rapid Fire - Increases firing rate
  - Big Shot - Larger bullets with increased damage
  - Slow Enemy - Reduces enemy tank's speed
- Destructible environment
- Real-time minimap
- Sound effects and background music
- Health and lives system

## Technical Details

### Version of Java Used
Java 17 or later

### IDE Used
IntelliJ IDEA (Community or Ultimate Edition)

### Steps to Import Project into IDE

1. Clone the repository:
```bash
git clone https://github.com/yourusername/tankgame-ryklin2.git
```

2. Open IntelliJ IDEA
3. Select `File -> Open`
4. Navigate to the cloned repository and select the `TankGame` folder
5. Wait for IntelliJ to import and index the project

### Steps to Build the Project

1. In IntelliJ IDEA:
   - Go to `File -> Project Structure`
   - Ensure SDK is set to Java 17 or later
   - Mark `src` folder as Sources
   - Mark `resources` folder as Resources
2. Build the project:
   - Select `Build -> Build Project`
   - Or use the keyboard shortcut `Ctrl+F9` (Windows/Linux) or `Cmd+F9` (Mac)

### Steps to Run the Project

1. Locate `src/tankrotationexample/Launcher.java`
2. Right-click on the file
3. Select `Run 'Launcher.main()'`

Alternative method:
1. Build the JAR file:
   - `File -> Project Structure -> Artifacts`
   - Click `+` -> `JAR` -> `From modules with dependencies`
   - Select `Launcher` as main class
   - Click `OK`
2. Build the artifact:
   - `Build -> Build Artifacts`
3. Run the JAR:
```bash
java -jar TankGame.jar
```

## Game Controls

|   Action      | Player 1 | Player 2 |
|---------------|----------|----------|
| Forward       | W        | ↑        |
| Backward      | S        | ↓        |
| Rotate left   | A        | ←        |
| Rotate Right  | D        | →        |
| Shoot         | SPACE    | NUMPAD 0 |

## Game Mechanics

### Health System
- Each tank starts with 100 health points and 3 lives
- Health regenerates to full when losing a life
- Game ends when a tank loses all lives

### Power-ups
- **Rapid Fire**: Reduces shooting cooldown by 50%
- **Big Shot**: Doubles bullet size and damage
- **Slow Enemy**: Reduces enemy tank's movement and rotation speed

### Maps
1. **Open Arena**: Basic map with minimal obstacles
2. **Maze**: Complex layout with destructible walls
3. **Split Screen**: Large map with split views for each player

## Development

### Project Structure
```
TankGame/
├── src/                    # Source code
├── resources/             # Game assets (images, sounds)
├── jar/                   # Compiled game
└── docs/                  # Documentation
```

### Key Classes
- `Launcher`: Main entry point and game state manager
- `GameWorld`: Core game logic and rendering
- `Tank`: Player tank logic and controls
- `PowerUp`: Power-up system
- `SoundPlayer`: Audio management

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
