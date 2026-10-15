package com.example.aimtracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** JSON-backed config, stored at config/aimtracker.json. Edit the file, then restart the game. */
public class ModConfig {
    public enum Mode { TOGGLE, HOLD }

    // Defaults as constants so they are easy to change in one place.
    public static final int DEFAULT_KEY = GLFW.GLFW_KEY_V;

    public Mode mode = Mode.TOGGLE;
    public double maxRadius = 32.0;        // blocks
    public double fovLimitDegrees = 90.0;  // full cone width; 360 = no limit
    public double smoothing = 0.25;        // 0..1 per tick; 1.0 = instant snap
    public double aimHeight = 0.9;         // fraction of target height (0.9 ~ head)
    public boolean requireLineOfSight = true;
    public boolean ignoreTeammates = true;
    public List<String> friends = new ArrayList<>(); // player names to ignore

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH =
            FabricLoader.getInstance().getConfigDir().resolve("aimtracker.json");
    private static ModConfig instance = new ModConfig();

    public static ModConfig get() { return instance; }

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                ModConfig loaded = GSON.fromJson(Files.readString(PATH), ModConfig.class);
                if (loaded != null) instance = loaded;
            }
            Files.writeString(PATH, GSON.toJson(instance)); // write defaults / normalize
        } catch (IOException | RuntimeException e) {
            System.err.println("[AimTracker] Config error, using defaults: " + e);
        }
    }
}
