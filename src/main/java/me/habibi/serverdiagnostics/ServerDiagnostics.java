package me.habibi.serverdiagnostics;

import org.bukkit.plugin.java.JavaPlugin;

public final class ServerDiagnostics extends JavaPlugin {

    private long startTime;
    private PerformanceMonitor performanceMonitor;

    public PerformanceMonitor getPerformanceMonitor() {
        return performanceMonitor;
    }
    @Override
    public void onEnable() {

        startTime = System.currentTimeMillis();

        getLogger().info("ServerDiagnostics has been enabled!");

        performanceMonitor = new PerformanceMonitor(this);
        performanceMonitor.start();

        getCommand("serverdiag").setExecutor(new DiagnosticCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("ServerDiagnostics has been disabled!");
    }

    public long getStartTime() {
        return startTime;
    }
}