# Tic Tac Toe – XOXO

A premium, modern, offline-first Android casual game built with native **Kotlin**, **Jetpack Compose**, **Material 3**, and **MVVM Architecture**. Features an original electric-neon visual aesthetic, custom canvas symbol animations, a Minimax AI with Alpha-Beta pruning, haptic and synthesized sound feedback, lifetime statistics, and full settings persistence.

---

## 🎮 Game Features

- **Single Player vs AI**:
  - Play as **X** (moves first) or **O** (moves second).
  - 3 AI difficulty tiers:
    - **Easy**: Casual, random legal moves.
    - **Medium**: Seizes immediate winning moves and blocks human wins with strategic positioning.
    - **Hard**: Mathematically unbeatable **Minimax algorithm with Alpha-Beta pruning** and depth-weighted scoring.
  - Realistic thinking delay (350–600ms) with visual indicator; player input is locked during AI calculation.
- **Two Players (Pass & Play)**:
  - Local multiplayer on the same device.
  - Custom player names with input sanitization and length validation.
  - Clear active player highlighting and turn indicators.
- **Visual Design & Aesthetics**:
  - Deep navy/purple surface background (`#0B0E1B`).
  - Electric cyan (`#00F2FE`) and vibrant neon pink (`#FF2A6D`) palette.
  - Smooth rounded surfaces, subtle depth glow, and tactile game-style buttons.
  - Custom Compose `Canvas` vector drawing for symbols:
    - **X**: Animated drawing of dual rounded crossed neon strokes.
    - **O**: Animated 360-degree sweep circular stroke.
  - Glowing animated winning line across any of the 8 winning combinations (3 rows, 3 columns, 2 diagonals).
  - Celebratory confetti particle explosion on victory.
- **Sound & Haptics Engine**:
  - **Procedural Audio Synthesizer**: Low-latency PCM dual-tones and victory arpeggios created in real-time with zero external audio assets needed. Guarantees 100% offline play without file-missing crash risks.
  - **Tactile Haptic Feedback**: Click ticks on cell placement and multi-stage vibration on victory.
- **Lifetime Statistics & Records**:
  - Persisted using **Jetpack DataStore Preferences**.
  - Tracks total matches, total wins, losses, draws, and win rate percentage.
  - Individual breakdown for Easy, Medium, and Hard single-player modes, plus local two-player stats.
  - Reset statistics with a protective confirmation dialog.
- **Personalized Settings**:
  - Sound effects toggle.
  - Haptic feedback toggle.
  - Visual animations toggle (respects reduced animation preference).
  - Theme selection: **Dark Neon** (recommended), **Crisp Light**, or **System Default**.
- **Privacy & Security**:
  - **100% Offline**: Requires zero network access, zero third-party telemetry, zero user login or accounts.
  - **Zero Dangerous Permissions**: Only requests standard `VIBRATE` for game haptics.

---

## 🏗️ Architecture & Technology Stack

```
com.antigravity.tictactoe/
├── TicTacToeApp.kt                     # Application class & DI singletons
├── MainActivity.kt                      # Edge-to-edge Activity with Splash & Theme host
│
├── domain/
│   ├── model/
│   │   ├── Player.kt                    # Player enum (X, O) with opponent switch
│   │   ├── Board.kt                     # Immutable 3x3 board representation
│   │   ├── WinningCombination.kt        # 8 winning lines with coordinate mappings
│   │   ├── GameMode.kt                  # SinglePlayer and TwoPlayers sealed interface
│   │   ├── GameDifficulty.kt            # Easy, Medium, Hard
│   │   ├── GameStatus.kt                # Ongoing, Won(winner, combination), Draw
│   │   ├── Statistics.kt                # Lifetime stats and breakdown records
│   │   └── AppSettings.kt               # Audio, haptic, animation, and theme preferences
│   ├── ai/
│   │   └── TicTacToeAiEngine.kt         # Heuristic & Minimax + Alpha-Beta engine
│   └── game/
│       └── TicTacToeGameEngine.kt       # Pure Kotlin game state machine & rule validator
│
├── data/
│   ├── preferences/
│   │   └── PreferencesRepository.kt     # App settings persistence via DataStore
│   └── statistics/
│       └── StatisticsRepository.kt      # Lifetime match records via DataStore
│
├── sound/
│   └── SoundManager.kt                  # AudioTrack synthesized neon game audio
├── haptics/
│   └── HapticsManager.kt                # Vibration feedback for touches, moves & wins
│
└── ui/
    ├── theme/
    │   ├── Color.kt                     # Electric cyan, neon pink, deep navy tokens
    │   ├── Type.kt                      # Bold gaming typography
    │   ├── Shape.kt                     # Rounded shapes
    │   └── Theme.kt                     # Material 3 Dark / Light / System theme wrapper
    ├── components/
    │   ├── SymbolDrawables.kt           # Canvas vector rendering of neon X & O
    │   ├── BoardCanvas.kt               # 3x3 interactive tiles & winning line overlay
    │   ├── ConfettiParticleSystem.kt    # Lightweight canvas particle explosion
    │   ├── GameButton.kt                # Tactile gradient button with spring press
    │   ├── GlowCard.kt                  # Rounded container with neon border gradient
    │   └── ScoreBoard.kt                # Dynamic match scoreboard & turn banner
    ├── navigation/
    │   ├── Screen.kt                    # Navigation routes
    │   └── NavGraph.kt                  # Slide/fade navigation graph
    └── screens/
        ├── home/                        # Lobby with animated floating X/O background
        ├── singleplayer/                # Symbol selection & difficulty selection
        ├── multiplayer/                 # Two player name configuration
        ├── game/                        # Gameplay canvas, score, and ResultDialog
        ├── statistics/                  # Lifetime stats & win-rate progress
        ├── settings/                    # Sound, haptics, animations & theme options
        └── howtoplay/                   # Illustrated game rules and AI guide
```

---

## 🧠 AI Difficulty Algorithms

### 1. Easy Mode
Selects random valid moves from remaining unoccupied cells. Perfect for a quick, casual match.

### 2. Medium Mode
Executes a multi-tier heuristic:
1. **Immediate Win**: Scans if the AI has 2 in a line and completes it.
2. **Immediate Block**: Scans if the player has 2 in a line and blocks the threat.
3. **Strategic Preference**: 70% preference to claim the center cell (4) or corners (0, 2, 6, 8).
4. **Random Fallback**: Chooses among remaining legal moves.

### 3. Hard Mode (Minimax with Alpha-Beta Pruning)
Recursively explores the entire game decision tree:
- **Terminal State Evaluation**:
  - AI Win: `+10 - depth` (incentivizes winning as quickly as possible).
  - Human Win: `-10 + depth` (incentivizes delaying defeat as long as possible).
  - Draw: `0`.
- **Alpha-Beta Pruning**: Discards branches that cannot yield a better outcome than previously evaluated alternatives, optimizing execution time.
- **Unbeatable Guarantee**: When playing against another optimal player, the match is mathematically guaranteed to result in a Draw. Unit tests verify this against all 9 opening moves.

---

## 🛠️ How to Open and Build

### Prerequisites
- **Android Studio Ladybug (2024.2+)** or later.
- **JDK 21** (bundled with Android Studio JBR).
- **Android SDK API 35**.

### Opening in Android Studio
1. Clone or copy this repository to your machine.
2. In Android Studio, select **File > Open** and choose the `Tic tac toe` root folder.
3. Allow Gradle to sync dependencies.

### Command Line Build Instructions

#### Run Unit Tests
```bash
./gradlew testDebugUnitTest
```

#### Build Debug APK
```bash
./gradlew assembleDebug
```
The debug APK is generated at:
`app/build/outputs/apk/debug/app-debug.apk`

#### Build Release APK
```bash
./gradlew assembleRelease
```
The optimized release APK is generated at:
`app/build/outputs/apk/release/app-release.apk`

#### Generate Signed Android App Bundle (AAB) for Google Play
1. Generate or place your release keystore file.
2. Create a `keystore.properties` file in the root project:
   ```properties
   storeFile=/path/to/release.keystore
   storePassword=your_store_password
   keyAlias=your_key_alias
   keyPassword=your_key_password
   ```
3. Run:
   ```bash
   ./gradlew bundleRelease
   ```
4. Upload the generated `.aab` file from `app/build/outputs/bundle/release/` to the Google Play Console.
5. In **Store presence > Main store listing**, upload the high-resolution app icon from:
   [`playstore_assets/playstore_icon_512x512.png`](file:///c:/Users/Admin/.gemini/antigravity/scratch/Tic%20tac%20toe/playstore_assets/playstore_icon_512x512.png) (512x512 PNG, 32-bit).

---

## 🎨 Customization Guide

- **Theme & Palette**: Edit `ui/theme/Color.kt` to customize colors (e.g. `NeonCyan`, `NeonPink`, `NeonGold`).
- **Sound Synthesis**: Check `sound/SoundManager.kt` to tweak frequencies, durations, or decay envelopes for custom audio tones.
- **AI Thinking Speed**: Adjust the delay range in `GameViewModel.kt` (`(350L..600L).random()`).

---

## 📄 License
This project is open-source and free to use for personal or commercial distribution.
