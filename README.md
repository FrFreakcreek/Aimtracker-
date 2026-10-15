# Aim Tracker (Fabric, Minecraft 1.21.11)

Client-side mod: hotkey (default V) snaps/smoothly aims the camera at the nearest valid player.

## Get the jar from GitHub
1. Create a new GitHub repo and upload everything from this zip (keep the `.github` folder).
2. Open the **Actions** tab and wait for "Build mod jar" to finish (a few minutes).
3. Open the run and download the **aimtracker-jar** artifact. Unzip it for `aimtracker-1.0.0.jar`.

## Install
Put the jar in `.minecraft/mods/` with Fabric Loader and Fabric API for 1.21.11.
Config: `config/aimtracker.json` (mode TOGGLE/HOLD, radius, FOV, smoothing, etc.).

Only use this where it is allowed (singleplayer or servers whose rules permit it).
