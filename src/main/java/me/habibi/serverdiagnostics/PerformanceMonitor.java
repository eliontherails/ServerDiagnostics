package me.habibi.serverdiagnostics;

import org.bukkit.plugin.java.JavaPlugin;

public class PerformanceMonitor {

    private final JavaPlugin plugin;

    private long lastTickTime;
    private double currentMspt;

    public PerformanceMonitor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        lastTickTime = System.nanoTime();

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {

            long now = System.nanoTime();

            long tickTime = now - lastTickTime;

            lastTickTime = now;

            currentMspt = tickTime / 1_000_000.0;

        }, 1L, 1L);
    }

    public double getCurrentMspt() {
        return currentMspt;
    }
}