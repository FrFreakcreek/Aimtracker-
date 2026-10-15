package com.example.aimtracker;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Entrypoint. Registration order matters: input -> target selection -> camera. */
public class AimTrackerClient implements ClientModInitializer {
    public static final String MOD_ID = "aimtracker";
    public static final Logger LOGGER = LoggerFactory.getLogger("AimTracker");

    @Override
    public void onInitializeClient() {
        LOGGER.info("[AimTracker] Loading...");
        ModConfig.load();
        KeybindRegistry.register();
        InputHandler.register();    // client tick: reads key, updates active flag
        TargetSelector.register();  // client tick: picks the target
        CameraHandler.register();   // every frame: rotates the camera
        LOGGER.info("[AimTracker] Loaded OK. Keybind and config ready.");
    }
}
