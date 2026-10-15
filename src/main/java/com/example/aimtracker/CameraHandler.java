package com.example.aimtracker;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Rotates the local player toward the target once per client tick, after TargetSelector.
 * The game interpolates the player's view between ticks, so this looks smooth at any FPS.
 */
public final class CameraHandler {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(CameraHandler::update);
    }

    private static void update(MinecraftClient client) {
        AbstractClientPlayerEntity t = TargetSelector.target;
        ClientPlayerEntity me = client.player;
        if (!InputHandler.active || t == null || me == null || !t.isAlive()) return;
        if (client.currentScreen != null) return; // don't fight menus

        ModConfig cfg = ModConfig.get();

        // Vector from our eyes to the aim point on the target.
        Vec3d d = TargetSelector.aimPoint(t, cfg.aimHeight).subtract(me.getEyePos());
        double dist = d.length();
        if (dist < 1.0e-4) return;

        // Vector -> angles. Minecraft yaw 0 faces +Z; pitch is negative when looking up.
        float targetYaw = (float) Math.toDegrees(Math.atan2(-d.x, d.z));
        float targetPitch = (float) -Math.toDegrees(Math.asin(d.y / dist));

        // wrapDegrees keeps the delta in [-180, 180], avoiding the 360-degree snap glitch.
        float dyaw = MathHelper.wrapDegrees(targetYaw - me.getYaw());
        float dpitch = targetPitch - me.getPitch();

        // Tiny deadzone: stop nudging once we're basically on target (prevents micro-jitter).
        if (Math.abs(dyaw) < 0.05f && Math.abs(dpitch) < 0.05f) return;

        // Fraction of the remaining angle covered each tick: 1.0 = instant snap.
        float alpha = (float) MathHelper.clamp(cfg.smoothing, 0.0, 1.0);

        // Use the same path as real mouse movement. It rotates the view AND shifts the
        // "previous" rotation by the same amount, so the game's frame interpolation stays
        // smooth. (changeLookDirection multiplies its inputs by 0.15 internally.)
        me.changeLookDirection(dyaw * alpha / 0.15, dpitch * alpha / 0.15);
    }

    private CameraHandler() {}
}
