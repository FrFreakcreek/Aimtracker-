package com.example.aimtracker;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/** Picks the valid player closest to the crosshair each tick. */
public final class TargetSelector {
    public static volatile AbstractClientPlayerEntity target = null;

    /** Aim point on a player: feet position raised by aimHeight * body height. */
    public static Vec3d aimPoint(AbstractClientPlayerEntity p, double aimHeight) {
        return new Vec3d(p.getX(), p.getY() + p.getHeight() * aimHeight, p.getZ());
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            target = null;
            if (!InputHandler.active || client.player == null || client.world == null) return;

            ModConfig cfg = ModConfig.get();
            ClientPlayerEntity me = client.player;
            Vec3d eye = me.getEyePos();
            Vec3d look = me.getRotationVec(1.0f);
            double halfFov = Math.toRadians(cfg.fovLimitDegrees / 2.0);
            double best = Double.MAX_VALUE;

            for (AbstractClientPlayerEntity p : client.world.getPlayers()) {
                if (p == me || p.isSpectator() || !p.isAlive()) continue;
                if (me.distanceTo(p) > cfg.maxRadius) continue;
                if (cfg.ignoreTeammates && me.isTeammate(p)) continue;
                if (cfg.friends.contains(p.getName().getString())) continue;
                if (cfg.requireLineOfSight && !me.canSee(p)) continue; // block raycast

                // Angle between current view direction and direction to target.
                Vec3d dir = aimPoint(p, cfg.aimHeight).subtract(eye).normalize();
                double angle = Math.acos(Math.max(-1.0, Math.min(1.0, look.dotProduct(dir))));
                if (cfg.fovLimitDegrees < 360.0 && angle > halfFov) continue; // outside cone

                if (angle < best) { best = angle; target = p; }
            }
        });
    }

    private TargetSelector() {}
}
