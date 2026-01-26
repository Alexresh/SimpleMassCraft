# SimpleMassCraft

<img src="src/main/resources/assets/simplemasscraft/icon.png" width="120" align="right" alt="SimpleMassCraft Icon">

**Automate repetitive crafting with a single Alt+Click**  
A lightweight Fabric mod that enables mass crafting directly from Minecraft's recipe book and stonecutter interfaces.

[![Fabric API](https://img.shields.io/badge/Fabric-1.21.4-blue?logo=fabric)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE.txt)

---

## ✨ Features

- **One-click mass crafting**: Hold `Alt` + click any recipe in the recipe book to start automatic crafting
- **Stonecutter support**: Same Alt+click workflow works in stonecutter GUI
- **Configurable delay**: Adjust crafting speed via in-game ModMenu or config file
- **Client-side only**: No server installation required – works in singleplayer and on any server **(ask the admin)**
- **Lightweight**: Minimal performance impact, no FPS drops

---

## 📥 Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.4
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download `SimpleMassCraft-x.x.jar` from [Releases](https://github.com/Alexresh/SimpleMassCraft/releases)
4. Place the JAR file in your `.minecraft/mods` folder
5. Launch Minecraft with the Fabric profile

> ✅ **Works on servers without server-side installation** (pure client-side mod)

---

## ⚙️ Configuration

Adjust crafting delay via either method:

### Via ModMenu (in-game)
1. Open Minecraft Settings → **Mods** → **SimpleMassCraft** → **Config**
2. Set `Mass craft delay` (0 = superfast, 1 = 1 tick delay between crafts)

### Via config file
Edit `.minecraft/config/simplemasscraft.json5`:
```json
{
    "massCraftDelay": 0
}
