package com.example.aimtracker;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

/** Registers the keybind so it shows up (and is rebindable) in Options > Controls. */
public final class KeybindRegistry {
    public static KeyBinding TRACK_KEY;

    public static void register() {
        // Since 1.21.9 categories are objects, and each may only be registered once.
        KeyBinding.Category category =
                KeyBinding.Category.create(Identifier.of(AimTrackerClient.MOD_ID, "main"));

        TRACK_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aimtracker.track",          // translation key
                InputUtil.Type.KEYSYM,           // keyboard key
                ModConfig.DEFAULT_KEY,           // default: V
                category));                      // controls menu category
    }

    private KeybindRegistry() {}
}
