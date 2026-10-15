package com.example.aimtracker;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;

/** Polls the keybind once per client tick and updates the tracking flag. */
public final class InputHandler {
    public static volatile boolean active = false;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) { active = false; return; }
            boolean before = active;

            if (ModConfig.get().mode == ModConfig.Mode.HOLD) {
                active = KeybindRegistry.TRACK_KEY.isPressed();
                while (KeybindRegistry.TRACK_KEY.wasPressed()) { /* drain queued presses */ }
            } else {
                while (KeybindRegistry.TRACK_KEY.wasPressed()) active = !active;
            }

            if (active != before) { // action bar notification
                client.player.sendMessage(
                        Text.translatable(active ? "aimtracker.on" : "aimtracker.off"), true);
            }
        });
    }

    private InputHandler() {}
}
